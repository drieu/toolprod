package fr.edu.admin

import fr.edu.toolprod.parser.ConfigParser
import fr.edu.toolprod.parser.HttpdParser
import org.apache.commons.logging.LogFactory
import org.springframework.web.multipart.MultipartHttpServletRequest
import toolprod.Machine
import toolprod.MachineGroup
import toolprod.Portal
import toolprod.Server

/**
 * Admin Controller.
 */
class AdminController {

    def index() {
        println("Index action from AdminController !")
        redirect(action:'init')
    }

    /**
     * Initialize datas like portals, machine Group.
     */
    def initData() {
        log.info("AdminController:initData()")
        boolean bResult = false
        if (request instanceof MultipartHttpServletRequest) {
            def message = ""
            request.getFiles("files[]").each { file ->
                log.info("AdminController:initData() file to parse:" + file.originalFilename)
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
                                    log.info("AdminController:initData() Add machine name:" + name + " in group:" + groupName)
                                }
                                log.info("AdminController:initData() Save group:" + groupName + " OK")
                                machineGroup.save(failOnError: true)
                            }
                        }
                    }
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

        def portals = Portal.findAll()
        def machinesGroups = MachineGroup.findAll()
        return [ portals: portals, machinesGroups: machinesGroups ]
    }

    /**
     * Method which call httpd parser.
     * @return
     */
    def init() {
        log.info("AdminController:init() action from AdminController : init()")
        List<String> portalsChoice = params.list('portalsChoice')
        log.debug("Choix :" + portalsChoice.toString())
        def portals = Portal.findAll()

        if (request instanceof MultipartHttpServletRequest) {
            def message = ""
            boolean bResult = true
            request.getFiles("files[]").each { file ->
                log.debug("AdminController:init() file to parse:" + file.originalFilename)

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
                    log.debug("AdminController:init() machineName:" + machineName)
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
