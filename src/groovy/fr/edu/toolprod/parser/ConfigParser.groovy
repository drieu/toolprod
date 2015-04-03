package fr.edu.toolprod.parser

import org.apache.commons.logging.LogFactory
import toolprod.Ldap
import toolprod.MailType

/**
 * Parse config file to init toolprod parameters ( portals, group ...)
 */
class ConfigParser {

    private BufferedReader br;

    private InputStream inputStream

    /**
     * Logger.
     */
    private static final log = LogFactory.getLog(this)

    private static final String EMPTY = ""

    /**
     * Store the string result for parsing (e.g: Nothing to parse for this file).
     */
    String result = EMPTY

    /**
     * Store the string result for close..
     */
    private String closeResult = EMPTY

    /**
     * Config file contains groups and machines for these groups ( group_web=machine1, machine2 ...).
     * This map store the result of parsing group and machine.
     */
    Map<String, List<String>> machineByGroup = new HashMap<>()

    ConfigParser(InputStream input) {
        inputStream = input
    }

    /**
     * Parse config file initialize in constructor.
     */
    def parse() {
        String line
        boolean bResult = false
        br = new BufferedReader(new InputStreamReader(inputStream))
        result = EMPTY
        try {
            String ldapUser = EMPTY
            String ldapPwd = EMPTY
            String ldapHost = EMPTY
            String ldapPort = EMPTY

            while ((line = br.readLine()) != null) {
                 if (line.startsWith("group_"))  {
                     parseGroup(line)
                     bResult = true

                 } else if (line.startsWith("mail_type"))  {
                    parseMailType(line)
                    bResult = true

                 } else if (line.startsWith("ldap_user"))  {
                    ldapUser = parseLdap(line)
                    bResult = true

                 } else if (line.startsWith("ldap_pwd"))  {
                    ldapPwd = parseLdap(line)
                    bResult = true

                 } else if (line.startsWith("ldap_host"))  {
                    ldapHost = parseLdap(line)
                     bResult = true

                 } else if (line.startsWith("ldap_port"))  {
                    ldapPort = parseLdap(line)
                    bResult = true

                 }
            }
            if (!bResult) {
               result = "Nothing to parse for file."
            }
            // Save ldap config in database.
            if ( !ldapHost.isEmpty()) {
                Ldap ldap = Ldap.findByName("M7")
                if (ldap == null) {
                    ldap = new Ldap()
                    ldap.name = "M7"
                    ldap.host = ldapHost
                    ldap.port = ldapPort
                    ldap.user = ldapUser
                    ldap.pwd = ldapPwd
                    ldap.save()
                }
            }
        } catch (IOException e) {
            result += "Impossible de parser le fichier !<br/>"
            log.error("Failed to parse file : " + e.printStackTrace())
        } finally {
            closeResult = close()
            if (!closeResult.isEmpty()) {
                result += closeResult
                bResult = false
            }
        }
        log.debug("parse() result=" + result)
        return bResult
    }

    /**
     * Parse ldap line in config file.
     * @param line ( e.g : ldap_user=myuser )
     * @return value after =
     */
    public parseLdap(String line) {
        String result = EMPTY
        int posEq = line.indexOf('=')
        if ( posEq > 0 ) {
            result = line.substring(posEq + 1, line.length())
        }
        return result
    }

    /**
     * Parse line which contains mailType.
     * @param line e.g: group_frontaux=web1,web2,web3,web4,web5,web6
     * @return
     */
    public parseMailType(String line) {
        log.debug("parseMailType() line=" + line)

        int posEq = line.indexOf('=')
        if ( posEq > 0 ) {
            def str = line.substring(posEq + 1, line.length())
            def lst = str.tokenize(",")
            for(String value : lst) {
                int pos = value.indexOf(':')
                if ( pos > 0 && (!value.isEmpty())) {
                    def fullNameType = value.substring(0, pos)
                    def shortNameType = value.substring( pos + 1, value.length())
                    if ( !fullNameType.isEmpty() && !shortNameType.isEmpty()) {
                        MailType mailType = MailType.findByFullNameType(fullNameType)
                        if (mailType == null) {
                            mailType = new MailType()
                            mailType.fullNameType = fullNameType
                            mailType.shortNameType = shortNameType
                            mailType.save(failOnError: true, flush:true)
                        }
                    }
                }
            }
        }
    }


    /**
     * Parse line which contains group and regex for group.
     * @param line e.g: group_frontaux=web1,web2,web3,web4,web5,web6
     * @return
     */
    public parseGroup(String line) {

        log.debug("parseGroup() line=" + line)
        int posEq = line.indexOf('=')
        if ( posEq > 0 ) {
            def strGrp = line.substring(0, posEq)

            int underscorePos = strGrp.indexOf('_')
            if ( underscorePos > 0 ) {
                def GroupName = strGrp.substring(underscorePos + 1, strGrp.length())
                log.debug("parseGroup() Groupe name:" + GroupName)
                def strMachine = line.substring(posEq + 1, line.length())

                def lst = strMachine.tokenize(",")
                def tmpLst = machineByGroup.get(GroupName)
                if (tmpLst == null) {
                    machineByGroup.put(GroupName, lst);
                } else {
                    for(String str : lst) {
                        tmpLst.add(str)
                        machineByGroup.put(GroupName, tmpLst);
                    }
                }
            }
            result=line
        }
    }

    /**
     * Close inpustream fo file to parse.
     * @return String result EMPTY if there is no error or a message.
     */
    def close() {
        String result = EMPTY
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                result += "Impossible de parser le fichier !<br/>"
                log.error("Failed to parse file : " + e.printStackTrace())
            }
        }
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                result += "Impossible de parser le fichier !<br/>"
                log.error("Failed to parse file : " + e.getMessage())
                e.printStackTrace();
            }
        }
        return result
    }
}
