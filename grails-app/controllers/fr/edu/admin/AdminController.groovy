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
        log.info("Init action from AdminController : init()")
        if (request instanceof MultipartHttpServletRequest) {
            def file = request.getFile('appLst')
            def machineName = request.getParameterValues("machinename")
            log.info("Name of machine : " + machineName[0])
            if((machineName != null) && (file != null) && (!file.isEmpty())) {


                HttpdParser parser = new HttpdParser(file.inputStream, machineName[0]);

                boolean bResult = parser.parse();
                if (bResult) {
                    flash.message = parser.result
                } else {
                    flash.error = parser.result
                }

            } else {
                log.debug("init() machineName:" + machineName)
                flash.error = 'Import failed because file is null or is empty'
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
