package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.ArenaBean
import org.apache.commons.logging.LogFactory
import toolprod.App

/**
 * Created with IntelliJ IDEA.
 * User: drieu
 * Date: 17/04/15
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
class ArenaParser extends Parser{

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
    ArenaParser(InputStream input){
        inputStream = input
    }


    @Override
    def parse() {
        log.info("parse()")
        String line
        boolean bResult = false
        br = new BufferedReader(new InputStreamReader(inputStream))
        result = EMPTY
        try {
            ArenaBean arenaBean = new ArenaBean()
            while ((line = br.readLine()) != null) {
                line = line.trim()
                if (line.startsWith("<Row>")) {
                    arenaBean = new ArenaBean()
                }
                if (line.startsWith("<Data>")) {
                    String parseResult = extractData(line)
                    if( !parseResult.isEmpty()) {
                        log.info("parseResult:" + parseResult)
                        if (parseResult.startsWith("/") && !parseResult.startsWith("/redirectionhub/")) {
                            parseResult = parseResult.substring(1, parseResult.length())
                            int pos = parseResult.indexOf("/")
                            if (pos > 0) {
                                parseResult = parseResult.substring(0, pos)
                            }
                            log.info("parse() found appName in xml file appName:" + parseResult)
                            arenaBean.appName = parseResult
                        } else if(parseResult.startsWith("/redirectionhub/")) { ///redirectionhub/redirect.jsp?applicationname=itsm7pro
                            int pos = parseResult.indexOf("applicationname=")
                            if (pos > 0) {
                                parseResult = parseResult.substring(pos+"applicationname=".length(), parseResult.length())
                                arenaBean.appName = parseResult.trim()
                                log.info("parse() found appName in xml file appName:" + parseResult)
                            }
                        } else {
                            arenaBean.arenaPath.add(parseResult)
                        }
                    }
                }

                if ( line.startsWith("</Row>")) {
                    if (arenaBean != null) {
                        log.info("parse() search for application :" + arenaBean.appName)
                        App app = App.findByName(arenaBean.appName)
                        if (app != null) {
                            log.info("Save ARENA path for application :" + arenaBean.appName)
                            app.arenaPath = arenaBean.arenaPath
                            app.description = arenaBean.arenaPath.last()
                            app.save()
                        }
                    } else {
                        log.error("parse() arenaBean is null at </Row> !")
                    }
                    arenaBean = new ArenaBean()
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
     * Extract Data from line <Data>...</Data>
     * @param line
     * @return  String between xml tags or EMPTY.
     */
    def extractData(String line) {
        String result = EMPTY
        line = line.trim()
        if (line.startsWith("<Data>")) {
            result = line.substring("<Data>".length())

            int posEndData = result.indexOf("</Data")
            if (posEndData > 0) {
                result = result.substring(0, posEndData)
                if (result.size() != 1) {
                    return result
                } else {
                    result = EMPTY
                }
            }
        }
        return result
    }
}
