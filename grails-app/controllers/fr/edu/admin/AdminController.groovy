package fr.edu.admin

import fr.edu.toolprod.parser.BigIpParser
import fr.edu.toolprod.parser.ConfigParser
import fr.edu.toolprod.parser.HttpdParser
import org.apache.commons.logging.LogFactory
import org.springframework.web.multipart.MultipartHttpServletRequest
import toolprod.Machine
import toolprod.MachineGroup
import toolprod.Portal
import toolprod.Server
import toolprod.Vip

/**
 * Admin Controller.
 */
class AdminController {

    def index() {
        log.info("index() Index action from AdminController !")
        redirect(action:'init')
    }

    /**
     * Initialize datas Step 1.
     */
    def initData() {
        log.info("initData()")
        flash.clear()
        boolean bResult = false
        if (request instanceof MultipartHttpServletRequest) {
            def message = ""
            request.getFiles("files[]").each { file ->
                log.info("initData() file to parse :" + file.originalFilename)
                if (!file.originalFilename.isEmpty()) {
                    ConfigParser configParser = new ConfigParser(file.inputStream)
                    bResult = configParser.parse()
                    message = configParser.result
                    if (bResult) {
                        for(String p : configParser.sportals) {
                            Portal.findOrSaveByName(p)
                        }
                        Map<String, List<String>> machineByGroup = configParser.machineByGroup
                        for (String groupName : machineByGroup.keySet()) {

                            MachineGroup machineGroup = MachineGroup.findByGroupName(groupName)
                            if (machineGroup == null) {
                                machineGroup = new MachineGroup()
                                machineGroup.groupName = groupName
                                List<String> machines = machineByGroup.get(groupName)
                                for (String name : machines) {
                                    machineGroup.regex.add(name)
                                    log.info("initData() Add machine name:" + name + " in group:" + groupName)
                                }
                                log.info("initData() Save group:" + groupName + " OK")
                                machineGroup.save(failOnError: true, flush:true)
                            }
                        }
                    }
                } else {
                    message = "File is empty !"
                }
            }

            if (bResult) {
                flash.message = "SUCCESS"
            } else {
                flash.error = "FAILED : " + message
            }
        }

        def portals = Portal.findAll()
        def machinesGroups = MachineGroup.findAll()
        return [ portals: portals, machinesGroups: machinesGroups ]
    }

    def initPortals() {
        log.info("initPortals()")
        boolean bResult = false
        if (request instanceof MultipartHttpServletRequest) {
            def message = ""
            request.getFiles("files[]").each { file ->
                log.info("initPortals() file to parse :" + file.originalFilename)
                if (!file.originalFilename.isEmpty()) {
                    BigIpParser configParser = new BigIpParser(file.inputStream)
                    configParser.parse()
                } else {
                    message = "File is empty !"
                }
            }
            flash.clear()
            if (bResult) {
                flash.message = "SUCCESS"
            } else {
                flash.error = "FAILED : " + message
            }

        }
        def portals = Vip.findAll()
        return [ portals: portals]
    }

    /**
     * Method which call httpd parser.
     * @return
     */
    def init() {
        log.info("init()")
        List<String> portalsChoice = params.list('portalsChoice')
        log.debug("init() portal choice :" + portalsChoice.toString())
        def portals = Portal.findAll()
        flash.clear()
        if (request instanceof MultipartHttpServletRequest) {
            def message = ""
            boolean bResult = true
            request.getFiles("files[]").each { file ->
                log.debug("init() file to parse:" + file.originalFilename)

                def machineName = request.getParameterValues("machinename")
                log.info("Name of machine : " + machineName[0])
                if((machineName != null) && (file != null) && (!file.isEmpty())) {
                    HttpdParser parser = new HttpdParser(file, machineName[0], portalsChoice);
                    if (!parser.parse()) {
                        bResult = false
                    }
                    message += parser.result
                } else {
                    bResult = false
                    log.debug("init() machineName:" + machineName)
                    message += 'Import failed because file is null or is empty'
                }
            }
            if (bResult) {
                flash.message = "SUCCESS : " + message
            } else {
                flash.error = "FAILED : " + message
            }
        }


        return [ portals:portals ]
    }

    /**
     * Redirect to info.gsp for having informations about plugins, versions ...
     */
    def info() {
    }

}
