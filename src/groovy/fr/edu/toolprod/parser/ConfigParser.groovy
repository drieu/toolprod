package fr.edu.toolprod.parser

import org.apache.commons.logging.LogFactory

/**
 * Created with IntelliJ IDEA.
 * User: drieu
 * Date: 29/08/14
 * Time: 12:38
 * To change this template use File | Settings | File Templates.
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
                     sportals = parsePortal(line)
                 }
                 if (line.startsWith("group="))  {
                     parseGroup(line)
                 }
            }
            bResult = true
        } catch (IOException e) {
            bResult = false
            result += "Impossible de parser le fichier !<br/>"
            log.error("Failed to parse file : " + e.printStackTrace())
        } finally {
            closeResult = close()
            if (!closeResult.isEmpty()) {
                result += closeResult
                bResult = false
            }
        }
        log.info("result=" + result)
        return bResult
    }

    public parseGroup(String line) {

        int posEq = line.indexOf('=')
        if ( posEq > 0 ) {
            def strGrp = line.substring(0, posEq)

            int underscorePos = strGrp.indexOf('_')
            if ( underscorePos > 0 ) {
                def GroupName = strGrp.substring(underscorePos + 1, strGrp.length())
                log.info("Groupe name:" + GroupName)
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
        }
    }


    /**
     * Parse portals string.
     * @param line e.g:portals=portal1,portal2
     * @return
     */
    def parsePortal(String line) {
        List<String> sportals = new ArrayList<>()
        if (line != null && !line.isEmpty()) {
            log.debug("parsePortal() line=" + line)
            String str = line.substring("portals=".size(), line.size())
            log.debug("parsePortal() line without portals= :" + str)
            def lst = str.tokenize(",")
            for ( String p in lst) {
                log.debug("Add portal:" + p)
                sportals.add(p)
            }
        }
        return sportals
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
