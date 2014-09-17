package fr.edu.admin

import fr.edu.toolprod.parser.HttpdParser
import org.springframework.web.multipart.MultipartHttpServletRequest
import toolprod.Check
import toolprod.Portal

class ToolsController {

    def index() {}

    def checkApacheConf() {

        log.info("ToolsController:init() action from ToolsController : checkApacheConf()")
        if (request instanceof MultipartHttpServletRequest) {
            def message = ""
            boolean bResult = true
            log.info("clearCheck() : DELETE all data in Check table")
            Check.executeUpdate('delete from Check')
            request.getFiles("files[]").each { file ->
                log.debug("ToolsController:checkApacheConf() file to parse:" + file.originalFilename)

                def machineName = request.getParameterValues("machinename")
                log.info("Name of machine : " + machineName[0])
                if((machineName != null) && (file != null) && (!file.isEmpty())) {
                    HttpdParser parser = new HttpdParser(file.inputStream, machineName[0], null);
                    if (!parser.check(file.originalFilename)) {
                        bResult = false
                    }
                    message += parser.result
                } else {
                    bResult = false
                    log.debug("ToolsController:checkApacheConf() machineName:" + machineName)
                    message += 'Import failed because file is null or is empty'
                }
            }
            if (bResult) {
                flash.message = "SUCCESS : " + message
            } else {
                flash.error = "FAILED : " + message
            }
        }
        List<Check> checks = Check.findAll()
        return [checks:checks]
    }
}
