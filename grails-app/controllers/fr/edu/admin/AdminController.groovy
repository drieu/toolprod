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
            def serverName = request.getParameterValues("servername")

            if((serverName != null) && (file != null) && (!file.isEmpty())) {

                Server server = Server.findByName(serverName)
                if (server == null) {
                    //TODO : port
                    server = new Server(name: serverName,portNumber: 80, serverType: Server.TYPE.APACHE)
                    if( !server.save()) {
                        log.error("Can't save Server :" + server )
                        flash.error = 'Error when parsing file.'
                        throw new Exception("Can't save Server !")
                    } else {
                        log.info("Save successful :" + server);
                    }
                } else {
                    log.debug("Server " + serverName + " exists in database")
                }
                HttpdParser parser = new HttpdParser();
                boolean bResult = parser.parse(server, file.inputStream);
                if (bResult) {
                    flash.message = 'Import successfull'
                } else {
                    flash.error = 'Error when parsing file.'
                }
//                  Modules
//                for (String module in parser.modules) {
//                    println("Module :" + module)
//                }
                parser.parseLocationFromApacheConf(file.inputStream)

            } else {
                log.debug("init() servername:" + serverName)
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
