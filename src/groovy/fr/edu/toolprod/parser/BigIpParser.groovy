package fr.edu.toolprod.parser

import org.apache.commons.logging.LogFactory
import toolprod.Server
import toolprod.Vip

/**
 * parse BigIp.conf file.
 */
class BigIpParser extends Parser{

    /**
     * Result of parsing file.
     */
    String result = EMPTY

    /**
     * Store closeResult
     */
    private String closeResult = EMPTY

    /**
     * Logger.
     */
    private static final log = LogFactory.getLog(this)


    /**
     * Constructor.
     * @param input
     */
    BigIpParser(InputStream input){
        inputStream = input
    }

    /**
     * Parse inpustream.
     */
    def parse() {
        log.info("parse()")
        String line
        boolean bResult = false
        br = new BufferedReader(new InputStreamReader(inputStream))
        result = EMPTY
        String fullVipName = EMPTY
        String shortVipName = EMPTY
        Map<String,List<String>> map = new HashMap<>()
        try {
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ltm pool")) {
                    fullVipName = extractFullVipName(line)
                    shortVipName = extractShortVipName(fullVipName)
                }
                if ( (line.trim()).startsWith("/LB-PUBLIC/")) {
                    String serverName = extractServerName(line)
                    String serverPort = extractServerPort(line)
                    Server server = Server.findByNameAndPortNumber(serverName, serverPort.toInteger())
                    if (server == null) {
                        server = new Server()
                        server.name = serverName
                        server.portNumber = serverPort.toInteger()
                        server.machineHostName = serverName
                    }
                    Vip vip = Vip.findByTechnicalName(fullVipName)
                    if (vip == null) {
                        vip = new Vip()
                        vip.name = shortVipName
                        vip.technicalName = fullVipName
                    }
                    List<Server> servers = vip.servers
                    if (vip.servers == null) {
                        vip.servers = new ArrayList<>()
                    }
                    servers.add(server)

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
        bResult
    }

    /**
     * Extract server port
     * @param line e.g : /LB-PUBLIC/webX:8028 {
     * @return
     */
    def extractServerPort(String line) {
        String result = EMPTY
        int pos = line.indexOf(":")
        int slashPos = line.indexOf("{")
        if (pos >0 && slashPos>0) {
            result = line.substring(pos + 1, slashPos)
        }
        return result
    }

    /**
     * Extract server name from a line.
     *
     * @param line e.g : /LB-PUBLIC/webX:8028 {
     * @return webX.ac...
     */
    def extractServerName(String line) {
        String result = EMPTY
        int pos = line.indexOf("/")
        if (pos > 0) {
            result = result.substring(pos + 1)
            pos = line.indexOf("/")
            if (pos > 0) {
                result = result.substring(pos + 1)
                pos = line.indexOf(":")
                if (pos > 0) {
                    result = result.substring(0, pos)
                }
                result = result + ".ac-limoges.fr"
            }
        }
        result
    }


    /**
     * Extract short vip name.
     * @param line e.g :pool-appli.ac-limoges.fr_ssl_
     * return appli
     */
    def extractShortVipName(String line) {
        String result = EMPTY
        if (line != null) {
            int dashPos = line.indexOf("-")
            if (dashPos > 0) {
                result = result.substring(dashPos + 1)
                int pointPos = line.indexOf(".")
                if (pointPos > 0) {
                    result = result.substring(0, pointPos)
                }
            }
        }
        result
    }

    /**
     * Extract VIP NAME
     * @param line e.g : ltm pool /LB-PUBLIC/pool-appli.ac-limoges.fr_ssl_grpid_13 {
     * @return appli.ac-limoges.fr_ssl
     */
    def extractFullVipName(String line) {
        String result = EMPTY
        if (line != null) {
            int slashPos = line.indexOf('/')
            if (slashPos > 0) {
                result = line.substring(slashPos + 1)
                slashPos = result.indexOf('/')
                if (slashPos > 0) {
                    result = result.substring(slashPos + 1)
                    int posGrid = result.indexOf("_grpid")
                    if (posGrid > 0) {
                        result = result.substring(0, posGrid)
                    }
                }
            }
        }
        result
    }

}
