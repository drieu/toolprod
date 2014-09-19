package fr.edu.admin

import fr.edu.toolprod.parser.ConfigParser
import fr.edu.toolprod.parser.HttpdParser
import org.apache.commons.logging.LogFactory
import org.springframework.web.multipart.MultipartHttpServletRequest
import toolprod.Portal
import toolprod.Server

class AdminController {

    def index() {
        println("Index action from AdminController !")
        redirect(action:'upload')
    }

    /**
     * Initialize datas.
     */
    def initData() {
        log.info("AdminController:initData() action from AdminController : initData()")
        if (request instanceof MultipartHttpServletRequest) {
            def message = ""
            boolean bResult = true
            request.getFiles("files[]").each { file ->
                log.debug("AdminController:initData() file to parse:" + file.originalFilename)
                ConfigParser configParser = new ConfigParser(file.inputStream)
                bResult = configParser.parse()
                message = configParser.result
                if (bResult) {
                    for(String p : configParser.sportals) {
                        Portal portal = Portal.findOrCreateByName(p)
                        portal.save(failOnError: true)
                    }
                    flash.message = "SUCCESS : " + message
                } else {
                    flash.error = "FAILED : " + message
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
     * method which call httpd parser.
     * @return
     */
    def init() {
        log.info("AdminController:init() action from AdminController : init()")
        List<String> portalsChoice = params.list('portalsChoice')
        log.info("Choix :" + portalsChoice.toString())
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
     * Upload a file and read content.
     * @return flash.message for success and error.
     */
    def upload() {
        log.info("upload()")
        if (request instanceof MultipartHttpServletRequest) {
            def file = request.getFile('appLst')
            if((file != null) && (!file.isEmpty())) {
                HttpdParser parser = new HttpdParser();
                boolean bResult = parser.parse(file.inputStream);
                if (bResult) {
                    flash.message = 'Import successfull'
                } else {
                    flash.error = 'Error when parsing file.'
                }
//                  Modules
//                for (String module in parser.modules) {
//                    println("Module :" + module)
//                }
                //parser.parseXml(file.inputStream)

            } else {
                flash.error = 'Import failed because file is null or is empty'
            }
        }
        redirect(action:'init')
    }


}
