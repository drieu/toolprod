package fr.edu.admin

import fr.edu.toolprod.parser.HttpdParser
import org.springframework.web.multipart.MultipartHttpServletRequest

class AdminController {

    def index() {
        println("Index action from AdminController !")
        redirect(action: 'upload')
    }

    def init() {
        println("Init action from AdminController !")
    }

    /**
     * Upload a file and read content.
     * @return flash.message for success and error.
     */
    def upload() {
        if (request instanceof MultipartHttpServletRequest) {
            def file = request.getFile('appLst')
            if((file != null) && (!file.isEmpty())) {
                HttpdParser parser = new HttpdParser();
                boolean bResult = parser.parse(file.inputStream);
                if (bResult) {
                    println("Extract from ProxyPass appli:" + parser.appName + " url:" + parser.appUrl + " server:" + parser.appServer + " port:" + parser.appPort);
                }
//                  Modules
//                for (String module in parser.modules) {
//                    println("Module :" + module)
//                }
                parser.parseXml(file.inputStream)

            } else {
                flash.message = 'failed'
            }
        }
        redirect(action:'init')
    }


}
