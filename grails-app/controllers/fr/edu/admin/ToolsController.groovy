package fr.edu.admin

import fr.edu.toolprod.bean.MultipartFileBean
import fr.edu.toolprod.parser.HttpdParser
import grails.converters.JSON
import grails.plugin.mail.MailService
import org.apache.directory.api.ldap.model.cursor.EntryCursor
import org.apache.directory.api.ldap.model.entry.Attribute
import org.apache.directory.api.ldap.model.message.SearchScope
import org.apache.directory.ldap.client.api.LdapConnection
import org.apache.directory.ldap.client.api.LdapNetworkConnection
import org.springframework.web.multipart.MultipartHttpServletRequest
import toolprod.App
import toolprod.Archive
import toolprod.Ldap
import toolprod.Machine
import toolprod.MailType
import toolprod.Server
import toolprod.Status
import org.apache.directory.api.ldap.model.entry.DefaultEntry

import java.text.SimpleDateFormat;

class ToolsController {

    def mailService

    private static final String EMPTY = ""

    /**
     * LDAP name.
     */
    private static final String LDAP_NAME = "M5"


    private static final String PARAM_UID = "uid"

    private static final String PARAM_RNE = "rne"

    private static final String PARAM_TYPE = "type"

    private static final String PARAM_SHORTNAME_TYPE = "shortNameType"

    List<String> machineDiffs = new ArrayList<>()
    List<String> appDiffs = new ArrayList<>()
    List<String> disappearApps = new ArrayList<>()

    def index() {}

    /**
     * Delete all data in status table.
     */
    def clearCheckTable() {
        log.info("clearCheck() : DELETE all data in Status table")
        Status.executeUpdate('delete from Status')
        flash.message = "SUCCESS : Données effacees de la table Status"
        redirect(controller:'tools',action:'checkApacheConf')
    }

    /**
     * Check Apache conf.
     */
    def checkApacheConf() {
        flash.clear()
        log.info("ToolsController:init() action from ToolsController : checkApacheConf()")
        if (request instanceof MultipartHttpServletRequest) {
            def  machineName = request.getParameterValues("machinename")
            if (machineName != null && (machineName[0].size() != 0)) {
                log.info("Name of machine : " + machineName[0])
                def message = EMPTY
                boolean bResult = true
                request.getFiles("files[]").each { file ->
                    log.info("ToolsController:checkApacheConf() file to parse:" + file.originalFilename)

                    if((file != null) && (!file.isEmpty())) {
                        MultipartFileBean f = new MultipartFileBean()
                        f.inputStream = file.inputStream
                        f.originalFilename = file.originalFilename
                        HttpdParser parser = new HttpdParser(f, machineName[0]);
                        if (!parser.check(machineName[0], f.originalFilename)) {
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

    /**
     * Generate mail name automatically
     */
    def mail() {
        log.info("mail()")
        def types = new ArrayList<String>()

        String rne = params.get(PARAM_RNE)
        String type = params.get(PARAM_SHORTNAME_TYPE)
        String pwd = params.get("pwd")

        if (type == null) {
            type = "TYPE"
        }
        if (rne == null) {
            rne = "RNE"
        }

        String uid = type + rne
        String mail = type + "." + rne + "@ac-limoges.fr"

        log.debug("mail() Type:" + type)
        log.debug("mail() RNE:" + rne)
        log.debug("mail() PWS:" + pwd)

        [ types: types, mail: mail, uid: uid, rne: rne, type: type, pwd: pwd ]
    }

    /**
     * generate an ldif file with params
     * @return
     */
    def downloadUID() {

        log.info("downloadUID()")
        String uid = params.get(PARAM_UID)
        String rne = params.get(PARAM_RNE)
        String type = params.get(PARAM_TYPE)
        String mail = params.get("mail")
        String pwd = params.get("pwd")


        String fileName = uid.toString() + ".ldif"
        File file = new File(fileName)

        String content = EMPTY

        content +=  "dn: uid=" + uid + ",ou=fonctionnelles,ou=ac-limoges,ou=education,o=gouv,c=fr"
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

        content +=  "userPassword: " + pwd
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

        content +=  "rne: " + rne
        content +=  System.getProperty("line.separator")

        content += "iplanet-am-modifiable-by: cn=top-level admin role,o=gouv,c=fr"
        content +=  System.getProperty("line.separator")

        content +=  "iplanet-am-user-login-status: active"

        file.write(content)

        //file.write("iplanet-am-modifiable-by: cn=top-level admin role,o=gouv,c=fr" + System.getProperty("line.separator") + "iplanet-am-user-login-status: active")

        render(file: file, contentType: 'text/plain', fileName: fileName)
    }

    /**
     * Call in mail.gsp to chek if a mail exists in LDAP
     * @return JSON data.
     */
    def ajaxCheckMailInLDAP = {
        log.info("ajaxCheckMailInLDAP()")
        def mailType
        boolean bCheck = true
        String mail = EMPTY

        // Init JSON data
        def data = [:]
        data.put("id", "1")

        if (params.id == null) {
            bCheck = false
            log.warn("ajaxCheckMailInLDAP() params.id is null !")
        }
        if (params.rne == null) {
            bCheck = false
            log.warn("ajaxCheckMailInLDAP() params.rne is null !")
        }

        if (bCheck) {
            mailType = MailType.findByShortNameType(params.id)
            String rne = params.rne
            if (mailType != null) {
                log.debug("ajaxCheckMailInLDAP() rne:" + rne)
                log.debug("ajaxCheckMailInLDAP() parameter :" + mailType.shortNameType)
                if (!rne.isEmpty()) {
                    mail = mailType.shortNameType + "." + rne + "@ac-limoges.fr" //TODO
                    if (checkMail(mail)) {
                        data.put("text", "KO")

                    } else {
                        data.put("text", "OK")

                    }
                } else {
                    log.warn("rne must not be empty : can't chek mail !")
                    data.put("text", "EMPTY")
                }
            }
        }
        data.put("mail", mail)
        log.info("ajaxCheckMailInLDAP() END" + mail)
        render data as JSON
    }

    /**
     * Check mail by executing a query in ldap.
     * @param mail
     * @return
     */
    boolean checkMail(String mail) {
        boolean check = false
        if (!mail.isEmpty()) {
            log.info("Connecting to LDAP ...")
            Ldap ldap = Ldap.findByName(LDAP_NAME)
            if (ldap != null) {
                Attribute result = null;
                LdapConnection connection = new LdapNetworkConnection( ldap.host, Integer.valueOf(ldap.port) );
                //LdapConnection connection = new LdapNetworkConnection( "ldap-m7.ac-limoges.fr", 389 );
                connection.bind( ldap.user, ldap.pwd );
                //connection.bind( "cn=Directory Manager", "P1n0r1mix" );
                String searchStr = "(|(mail=*" + mail + "*)(mailAlternateAddress=*" + mail + "*)(mailEquivalentAddress=*" + mail + "*))";
                log.info("Search mail :" + searchStr)
                EntryCursor cursor = connection.search( "ou=fonctionnelles, ou=ac-limoges, ou=education, o=gouv, c=fr", searchStr, SearchScope.ONELEVEL, "*" );
                while ( cursor.next() )
                {
                    DefaultEntry entry = (DefaultEntry)cursor.get()
                    log.info("ENTRY:" + entry.get("mailAlternateAddress"))
                    result = entry.get("mail")
                    if (result != null) {
                        log.info("Found result in ldap :" + result)
                        check = true;
                    }
                }
                connection.unBind()
                connection.close()
                if (check) {
                    log.info("Nothing found in LDAP for mail:" + result)
                }
                log.info("Disconnect to LDAP ...")
            }
        }
        return check
    }

    /**
     * Check difference between old import ( server, application ... )
     */
    def checkAll() {

        Archive arch = Archive.findByName("OLD")
        int count = 0
        machineDiffs = new ArrayList<>()
        appDiffs = new ArrayList<>()
        disappearApps = new ArrayList<>()
        Archive archive = Archive.findByName("OLD")
        if(archive != null) {
            int countNow = App.count()
            int archCount = archive.apps.size()
            if ( archCount != countNow ) {
                count = countNow - archCount
            }

            for(Machine m :Machine.findAll()) {
                if (!archive.machines.contains(m.name)) {
                   machineDiffs.add(m.name)
                }
            }

            for(App a:App.findAll()) {
                List<String> archApps = archive.apps
                if (!archApps.contains(a.name)) {
                    appDiffs.add(a.name)
                }
            }

            for(String str : arch.apps) {
                if (App.findByName(str) == null) {
                    disappearApps.add(str)
                }
            }
        }

       [ count : count, machineDiffs: machineDiffs, appDiffs: appDiffs, disappearApps: disappearApps, archive:arch ]
    }

    /**
     * Send mail with difference from previous archives
     */
    def sendMail() {
        String dest = params.get("dest")

        if (dest != null) {
            if (dest.contains(";")) {
                dest = dest.replace(";",",")
            }
            String[] destArr = dest.split(",")
            List<String> lst = new ArrayList<>()
            for (String str : destArr) {
                lst.add(str)
            }


            Archive archive = Archive.findByName("OLD")
            mailService.sendMail {
                to lst.toArray()
                from "Toolprod@ac-limoges.fr"
                subject "TOOLPROD : Récapitulatif des modifications des configurations Apache"
                html g.render(template:'/tools/message', model:[count:0, machineDiffs : machineDiffs, appDiffs: appDiffs, disappearApps : disappearApps, archDate:archive?.date])
            }
            flash.message = "Mail envoyé !"
        } else {
            flash.error = "le mail n'est pas envoyé car le destinataire est vide !"
        }
        redirect(action:"checkAll")
    }

    /**
     * Make an archive of the current configuration.
     * We delete old archive to replace by the new one.
     * @return
     */
    def archive() {

        Archive archive = Archive.findByName("OLD")
        if (archive == null) {
            archive = new Archive()
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        archive.date = sdf.format(date);
        archive.name = "OLD"
        archive.countApp = App.count()

        // Machines list
        List<String> machines = new ArrayList<>()
        for(Machine m :Machine.findAll()) {
            if (!machines.contains(m.name)) {
                machines.add(m.name)
            }
        }
        archive.machines = machines

        // Web server list
        List<String> servers = new ArrayList<>()
        for(Server s :Server.findAllByServerType(Server.TYPE.APACHE)) {
            if (!servers.contains(s.name)) {
                servers.add(s.name)
            }
        }
        archive.apacheWebServer = servers

        // Weblogic list
        List<String> weblos = new ArrayList<>()
        for(Server w :Server.findAllByServerType(Server.TYPE.WEBLOGIC)) {
            if (!weblos.contains(w.name)) {
                weblos.add(w.name)
            }
        }
        archive.apacheWebServer = weblos


        //Application list
        List<String> apps = new ArrayList<>()
        for(App w :App.findAll()) {
            if (!apps.contains(w.name)) {
                apps.add(w.name)
            }
        }
        archive.apps = apps

        archive.save(failOnError: true)
        flash.message = "Archive effectuée !"
        redirect(action:"checkAll")
    }

}
