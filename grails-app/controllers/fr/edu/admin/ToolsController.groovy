package fr.edu.admin

import fr.edu.toolprod.parser.HttpdParser
import org.springframework.web.multipart.MultipartHttpServletRequest
import toolprod.Check
import toolprod.Portal

class ToolsController {

    def index() {}

    def clearCheckTable() {
        log.info("clearCheck() : DELETE all data in Check table")
        Check.executeUpdate('delete from Check')
        flash.message = "SUCCESS : Données effacees de la table Check"
        redirect(controller:'tools',action:'checkApacheConf')
    }

    def checkApacheConf() {

        log.info("ToolsController:init() action from ToolsController : checkApacheConf()")
        if (request instanceof MultipartHttpServletRequest) {
            def  machineName = request.getParameterValues("machinename")
            if (machineName != null && (machineName[0].size() != 0)) {
                log.info("Name of machine : " + machineName[0])
                def message = ""
                boolean bResult = true
                request.getFiles("files[]").each { file ->
                    log.debug("ToolsController:checkApacheConf() file to parse:" + file.originalFilename)


                    if((file != null) && (!file.isEmpty())) {
                        HttpdParser parser = new HttpdParser(file, machineName[0], null);
                        if (!parser.check(machineName[0], file.originalFilename)) {
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
            } else {
                flash.error = "FAILED : Vous devez définir un nom de machine"
            }
        }

        List<Check> checks = Check.findAll()
        int count = checks.size()
        return [checks:checks, count:count]
    }
}
