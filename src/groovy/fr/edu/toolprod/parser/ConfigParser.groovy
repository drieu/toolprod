package fr.edu.toolprod.parser

import org.apache.commons.logging.LogFactory
import toolprod.MailType

/**
 * Parse config file to init toolprod parameters ( portals, group ...)
 */
class ConfigParser {

    private BufferedReader br;

    private InputStream inputStream

    private static final String EMPTY = ""

    String result = ""

    private String closeResult = EMPTY

    private static final log = LogFactory.getLog(this)

    public List<String> sportals;

    Map<String, List<String>> machineByGroup = new HashMap<>()

    ConfigParser(InputStream input) {
        inputStream = input
    }

    /**
     * Parse file initialize in constructor
     */
    def parse() {
        String line
        boolean bResult = false
        br = new BufferedReader(new InputStreamReader(inputStream))
        result = ""
        sportals = new ArrayList<>()
        try {
            while ((line = br.readLine()) != null) {
                 if (line.startsWith("portals=")) {
                     result = line
                     parsePortal(line)
                 }
                 if (line.startsWith("group_"))  {
                     parseGroup(line)
                 }
                if (line.startsWith("mail_type"))  {
                    parseMailType(line)
                }
            }
            bResult = true
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
                        MailType mailType = new MailType()
                        mailType.fullNameType = fullNameType
                        mailType.shortNameType = shortNameType
                        mailType.save(failOnError: true)
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
     * Parse portals string.
     * @param line e.g:portals=portal1,portal2
     * @return
     */
    public parsePortal(String line) {
        sportals = new ArrayList<>()
        if (line != null && !line.isEmpty()) {
            log.debug("parsePortal() line=" + line)
            String str = line.substring("portals=".size(), line.size())
            def lst = str.tokenize(",")
            for ( String p in lst) {
                log.debug("parsePortal() portal:" + p)
                sportals.add(p)
            }
        }
    }

    /**
     * Close inpustream fo file to parse.
     * @return String result EMPTY if there is no error or a message.
     */
    def close() {
        String result = ""
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
