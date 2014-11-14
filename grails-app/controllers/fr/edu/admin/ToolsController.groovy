package fr.edu.admin

import fr.edu.toolprod.parser.HttpdParser
import org.springframework.web.multipart.MultipartHttpServletRequest
import toolprod.Status

class ToolsController {

    // Export service provided by Export plugin
    def exportService
    def grailsApplication  //inject GrailsApplication


    def index() {}

    def clearCheckTable() {
        log.info("clearCheck() : DELETE all data in Status table")
        Status.executeUpdate('delete from Status')
        flash.message = "SUCCESS : Données effacees de la table Status"
        redirect(controller:'tools',action:'checkApacheConf')
    }

    def checkApacheConf() {


        if(!params.max) {
            params.max = 10
        }

        if ((params.extension != null)) {
            log.info(params.get('zest'))
            def format=params.extension
            if ("xls".equals(params.extension)) {
                format="excel"
            }
            if(format && format != "html"){
                response.contentType = grailsApplication.config.grails.mime.types[format]
                response.setHeader("Content-disposition", "attachment; filename=check.${params.extension}")
                List fields = ["machineName", "fileName", "name"]
                Map labels = ["machineName": "Nom de machine", "fileName": "Nom de fichier", "name":"Valeur du ServerName"]

                Map formatters = new HashMap()
                Map parameters = new HashMap()
                exportService.export(format, response.outputStream,Status.list(params), fields, labels, formatters, parameters)

            }
        }
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


        List<Status> checks = Status.findAll()
        int count = checks.size()
        return [checks:checks, count:count]
    }

    def list = {

        if(!params.max) params.max = 10

        if(params?.format && params.format != "html"){
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=books.${params.extension}")

            exportService.export(params.format, response.outputStream,Status.list(params), [:], [:])
        }

        [ bookInstanceList: Status.list( params ) ]
    }

}
