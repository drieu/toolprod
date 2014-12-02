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

    /**
     * Generate mail name automatically
     */
    def mail() {
        def types = new ArrayList<String>()

        String rne = params.get("rne")
        String type = params.get("type")
        types.add("intendance")
        types.add("magasin")

        log.info("Type:" + type)
        log.info("RNE:" + rne)

        if (type == null) {
            type = "TYPE"
        }
        if (rne == null) {
            rne = "RNE"
        }

        String uid = type + rne
        String mail = type + "." + rne + "@ac-limoges.fr"

        [ types: types, mail: mail, uid: uid, rne: rne, type: type ]
    }

    def downloadUID() {

        String uid = params.get("uid")
        String rne = params.get("rne")
        String type = params.get("type")
        String mail = params.get("mail")


        String fileName = uid.toString() + ".ldif"
        File file = new File(fileName)

        String content = ""

        content +=  "dn: uid=" + uid + ",ou=fonctionnelles,ou=ac-limoges,ou=education,o=gouv,c=fr"
        content +=  System.getProperty("line.separator")

        content +=  "sunUCDefaultApplication: mail"
        content +=  System.getProperty("line.separator")

        content +=  "preferredLanguage: fr"
        content +=  System.getProperty("line.separator")

        content +=  "sunUCTimeFormat: 24"
        content +=  System.getProperty("line.separator")

        content +=  "sunUCDateFormat: D/M/Y"
        content +=  System.getProperty("line.separator")

        content +=  "sunUCDateDelimiter: /"
        content +=  System.getProperty("line.separator")


        content +=  "sunUCTheme: theme_dark_blue"
        content +=  System.getProperty("line.separator")

        content +=  "mailHost: store72.ac-limoges.fr"
        content +=  System.getProperty("line.separator")

        content +=  "mailMessageStore: popfonct"
        content +=  System.getProperty("line.separator")

        content +=  "psRoot: ldap://ldap-m7.ac-limoges.fr:389/piPStoreOwner=" + uid + ",o=ac-limoges.fr,o=PiServerDb"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: sunUCPreferences"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: top"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: InetOrgPerson"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: mailrecipient"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: nsLicenseUser"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: organizationalPerson"
        content +=  System.getProperty("line.separator")


        content +=  "objectClass: person"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: educationnationale"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: nsMessagingServerUser"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: inetsubscriber"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: ipuser"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: inetuser"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: iplanet-am-user-service"
        content +=  System.getProperty("line.separator")


        content +=  "objectClass: iplanet-am-managed-person"
        content +=  System.getProperty("line.separator")


        content +=  "objectClass: userpresenceprofile"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: inetlocalmailrecipient"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: inetmailuser"
        content +=  System.getProperty("line.separator")

        content +=  "objectClass: icscalendaruser"
        content +=  System.getProperty("line.separator")

        content +=  "givenName: " + type
        content +=  System.getProperty("line.separator")

        content +=  "sn: " + rne
        content +=  System.getProperty("line.separator")

        content +=  "userPassword: TO_DEFINE"
        content +=  System.getProperty("line.separator")

        content +=  "ou: " + rne
        content +=  System.getProperty("line.separator")

        content +=  "mailUserStatus: active"
        content +=  System.getProperty("line.separator")

        content +=  "inetUserStatus: active"
        content +=  System.getProperty("line.separator")

        content +=  "mail: " + mail
        content +=  System.getProperty("line.separator")

        content +=  "uid: " + uid
        content +=  System.getProperty("line.separator")

        content +=  "mailDeliveryOption: mailbox"
        content +=  System.getProperty("line.separator")

        content +=  "nsLicensedFor: mail"
        content +=  System.getProperty("line.separator")

        content +=  "cn: " + type + " " + rne
        content +=  System.getProperty("line.separator")

        content += "iplanet-am-modifiable-by: cn=top-level admin role,o=gouv,c=fr"
        content +=  System.getProperty("line.separator")

        content +=  "iplanet-am-user-login-status: active"

        file.write(content)

        //file.write("iplanet-am-modifiable-by: cn=top-level admin role,o=gouv,c=fr" + System.getProperty("line.separator") + "iplanet-am-user-login-status: active")

        render(file: file, contentType: 'text/plain', fileName: fileName)
    }

}
