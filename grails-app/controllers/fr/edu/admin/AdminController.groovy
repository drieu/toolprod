package fr.edu.admin

import fr.edu.toolprod.bean.AppBean
import fr.edu.toolprod.bean.ArenaBean
import fr.edu.toolprod.bean.MultipartFileBean
import fr.edu.toolprod.parser.ArenaParser
import fr.edu.toolprod.parser.BigIpParser
import fr.edu.toolprod.parser.ConfigParser
import fr.edu.toolprod.parser.HttpdParser
import fr.edu.toolprod.parser.Parser
import org.springframework.web.multipart.MultipartHttpServletRequest
import toolprod.MachineGroup
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
                flash.message = "import successful."
            } else {
                flash.error = "FAILED : " + message
            }
        }

        def machinesGroups = MachineGroup.findAll()
        return [ machinesGroups: machinesGroups ]
    }

    /**
     * Show web page to init VIP.
     * @return
     */
    def initPortals() {
        log.info("initPortals()")
        flash.clear()
        boolean bResult = false
        if (request instanceof MultipartHttpServletRequest) {
            def message = ""
            request.getFiles("files[]").each { file ->
                log.info("initPortals() file to parse :" + file.originalFilename)
                if (!file.originalFilename.isEmpty()) {
                    BigIpParser configParser = new BigIpParser(file.inputStream)
                    bResult = configParser.parse()
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
     * Init data step 3 : Method which call httpd parser.
     * @return
     */
    def init() {
        log.info("init()")
        flash.clear()
        if (request instanceof MultipartHttpServletRequest) {
            def message = ""
            boolean bResult = true
            request.getFiles("files[]").each { file ->
                log.debug("init() file to parse:" + file.originalFilename)

                def machineName = request.getParameterValues("machinename")
                log.info("Name of machine : " + machineName[0])
                if((machineName != null) && (file != null) && (!file.isEmpty())) {
                    MultipartFileBean f = new MultipartFileBean()
                    f.inputStream = file.inputStream
                    f.originalFilename = file.originalFilename
                    HttpdParser parser = new HttpdParser(f, machineName[0]);
                    bResult = parser.parse()
                    if (!parser.save()) {
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
    }

    /**
     * Init data from the ARENA xml files .
     */
    def initFromArena() {
        log.info("initFromArena()")
        List<String> errs = new ArrayList<>()
        flash.clear()
        if (request instanceof MultipartHttpServletRequest) {
            def message = ""
            boolean bResult = true
            request.getFiles("files[]").each { file ->
                log.debug("init() file to parse:" + file.originalFilename)
                Parser parser = new ArenaParser(file.inputStream)
                List<ArenaBean> arenaBeans = parser.parse()
                errs = parser.save(arenaBeans)
                message = parser.result
                if (message.isEmpty()) {
                    message += " 0 application initialisée."
                }
            }
            if (bResult) {
                flash.message = "SUCCESS : " + message
            } else {
                flash.error = "FAILED : " + message
            }
        }
        [errs:errs]
    }

    /**
     * Redirect to info.gsp for having informations about plugins, versions ...
     */
    def info() {
    }

}
