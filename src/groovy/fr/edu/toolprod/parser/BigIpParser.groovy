package fr.edu.toolprod.parser

import org.apache.commons.logging.LogFactory
import toolprod.App
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


    private BufferedReader br;
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
        String type = EMPTY
        Map<String,List<String>> map = new HashMap<>()
        try {
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ltm pool")) {
                    fullVipName = extractFullVipName(line)
                    shortVipName = extractShortVipName(line)
                    if (line.contains("_ssl")) {
                        type="ssl"
                    }
                    if (line.contains("_http")) {
                        type="http"
                    }
                }
                if ( (line.trim()).startsWith("/LB-PUBLIC/")) {
                    String serverName = extractServerName(line)
                    if ( !serverName.isEmpty()) {
                        String serverPort = extractServerPort(line)
                        if (serverPort.isEmpty()) {
                            serverPort = 80
                        }
                        log.info("line:" + line)
                        log.info("Server name:" + serverName)
                        log.info("Server port:" + serverPort)
                        log.info("fullVipName:" + fullVipName)
                        log.info("shortVipName:" + shortVipName)
                        Vip vip = Vip.findByTechnicalName(fullVipName)
                        if (vip == null) {
                            log.info("Create new VIP ...")
                            vip = new Vip()
                            vip.name = shortVipName
                            vip.technicalName = fullVipName
                            vip.type = type
                            vip.save(failOnError: true)
                        }

                        Server server = Server.findByMachineHostNameAndPortNumber(serverName, serverPort.toInteger())
                        if (server == null) {
                            server = new Server()
                            server.name = serverName
                            server.portNumber = serverPort.toInteger()
                            server.machineHostName = serverName
                            server.serverType = Server.TYPE.APACHE
                            server.save(failOnError: true)
                        } else {
                            for(String appName : server.linkToApps) {
                                App app = App.findByName(appName)
                                if (app != null) {

                                }
                            }
                        }

                        if (vip.servers == null) {
                            vip.servers = new ArrayList<>()
                        }
                        if (!vip.servers.contains(server)) {
                            vip.servers.add(server)
                            vip.save(failOnError: true)
                        }
                    }
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
     * Extract server port ( WARN { at the end )
     * @param line e.g : /LB-PUBLIC/webX:8028 {
     * @return
     */
    def extractServerPort(String line) {
        String result = EMPTY
        if ( line == null) {
            return EMPTY
        }
        int pos = line.indexOf(":")
        int slashPos = line.indexOf("{")
        if (pos >0 && slashPos>0) {
            result = line.substring(pos + 1, slashPos)
            result = result.trim()
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
        if (line == null) {
            return EMPTY
        }
        log.info("extractServerName() line:" + line)
        int pos = line.indexOf("/")
        if (pos >= 0) {
            result = line.substring(pos + 1)
            pos = result.indexOf("/")
            if (pos > 0) {
                result = result.substring(pos + 1)
                pos = result.indexOf(":")

                if (pos > 0) {
                    result = result.substring(0, pos)
                    result = result + ".ac-limoges.fr"
                } else {
                    result = EMPTY
                }
            } else {
                result = EMPTY
            }
        }
        result
    }


    /**
     * Extract short vip name.
     * @param line e.g : ltm pool /LB-PUBLIC/pool-appli.ac-limoges.... {
     * return appli
     */
    def extractShortVipName(String line) {
        String result = EMPTY
        if (line == null) {
            return EMPTY
        }
        if (line != null) {
            int dashPos = line.indexOf("pool-")
            int size = "pool-".length()
            int pos = dashPos + size
            if (dashPos > 0 && pos < line.length()) {
                result = line.substring(pos)
                int pointPos = result.indexOf(".")
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
     * @return e.g : appli.ac-limoges.fr_ssl
     */
    def extractFullVipName(String line) {
        String result = EMPTY
        if (line == null) {
            return EMPTY
        }
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
                    } else {
                        result = EMPTY
                    }
                } else {
                    result = EMPTY
                }
            }
        }
        result
    }

}
