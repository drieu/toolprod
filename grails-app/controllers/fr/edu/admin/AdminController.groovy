package fr.edu.admin

import fr.edu.toolprod.parser.HttpdParser
import org.apache.commons.logging.LogFactory
import org.springframework.web.multipart.MultipartHttpServletRequest
import toolprod.Server

class AdminController {


    def index() {
        println("Index action from AdminController !")
        redirect(action:'upload')
    }

    def init() {
        log.info("AdminController:init() action from AdminController : init()")
        if (request instanceof MultipartHttpServletRequest) {
            def message = ""
            boolean bResult = true
            request.getFiles("files[]").each { file ->
                log.debug("AdminController:init() file to parse:" + file.originalFilename)

                def machineName = request.getParameterValues("machinename")
                log.info("Name of machine : " + machineName[0])
                if((machineName != null) && (file != null) && (!file.isEmpty())) {

                    HttpdParser parser = new HttpdParser(file.inputStream, machineName[0]);
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
