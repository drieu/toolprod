package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.AppBean
import fr.edu.toolprod.bean.ArenaBean
import org.apache.commons.logging.LogFactory
import toolprod.App

/**
 * Parse ARENA file and extract description, arena path ...
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
     * REDIRECTION_HUB.
     */
    private static final String SLASH = "/"

    /**
     * Tag Row start.
     */
    private static final String ROW_START = "<Row>"

    /**
     * Tag Row end.
     */
    private static final String ROW_END = "</Row>"

    /**
     * Tag Data start.
     */
    private static final String DATA_START = "<Data>"

    /**
     * Tag Data end.
     */
    private static final String DATA_END = "</Data"

    /**
     * REDIRECTION_HUB.
     */
    private static final String REDIRECTION_HUB = "/redirectionhub/"

    /**
     * applicationanme.
     */
    private static final String APPLICATION_NAME = "applicationname="



    /**
     * Logger.
     */
    private static final log = LogFactory.getLog(this)


    /**
     * Constructor.
     * @param input
     */
    ArenaParser(InputStream input){
        inputStream = input
    }

    /**
     * Parse Arena file.
     * @return
     */
    @Override
    def parse() {
        log.info("parse()")
        String line
        List<ArenaBean> arenaBeans = new ArrayList<>()
        if (inputStream == null) {
            log.warn("Cannot parse file because of null inputstream")
            return new ArrayList<>()
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))
        result = EMPTY
        try {
            ArenaBean arenaBean = new ArenaBean()
            while ((line = br.readLine()) != null) {
                line = line.trim()
                if (line.startsWith(ROW_START)) {
                    arenaBean = new ArenaBean()
                }
                if (line.startsWith(DATA_START)) {
                    String parseResult = extractData(line)
                    log.info("parseResult:" + parseResult)
                    if( !parseResult.isEmpty()) {
                        log.debug("parseResult:" + parseResult)
                        if (parseResult.startsWith(SLASH) && !parseResult.startsWith(REDIRECTION_HUB) && !parseResult.startsWith("/mdp/redirectionhub/") ) { // <Data>/redirectionhub/tiit.jsp?applicationname=titi</Data>
                            parseResult = parseResult.substring(1, parseResult.length())
                            int pos = parseResult.indexOf(SLASH)
                            if (pos > 0) {
                                parseResult = parseResult.substring(0, pos)
                            }
                            //log.info("parse() found appName in xml file appName:" + parseResult)
                            arenaBean.appName = parseResult

                        } else if (parseResult.startsWith("/mdp/redirectionhub/")) {
                            int pos = parseResult.indexOf(APPLICATION_NAME)
                            if (pos > 0) {
                                arenaBean.federationPath = parseResult
                                parseResult = parseResult.substring(pos + APPLICATION_NAME.length(), parseResult.length())
                                arenaBean.appName = parseResult.trim()
                                log.info("parse() found appName MDP in xml file appName:" + parseResult)
                            }


                        } else if(parseResult.startsWith(REDIRECTION_HUB)) { ///redirectionhub/redirect.jsp?applicationname=itsm7pro
                            int pos = parseResult.indexOf(APPLICATION_NAME)
                            if (pos > 0) {
                                arenaBean.fimPath = parseResult
                                parseResult = parseResult.substring(pos + APPLICATION_NAME.length(), parseResult.length())
                                arenaBean.appName = parseResult.trim()
                                log.info("parse() found appName REDIRECT_HUB in xml file appName:" + parseResult)
                            }

                        } else {
                            arenaBean.arenaPath = arenaBean.arenaPath + SLASH
                            arenaBean.arenaPath = arenaBean.arenaPath + parseResult
                        }
                    }
                }

                if ( line.startsWith(ROW_END)) {
                    if (arenaBean != null) {
                        final String name = arenaBean.appName
                        if (name != null) {
                            if (!name.contains("about:blank")) {
                                log.info("ADD name:" + name)
                                arenaBeans.add(arenaBean)
                                result = result + " " + name
                            }
                            log.info("parse() search for application END")
                        }
                    } else {
                        log.error("parse() arenaBean is null at </Row> !")
                    }
                    arenaBean = new ArenaBean()
                }
            }
        } catch (IOException e) {
            result += "Impossible de parser le fichier !<br/>"
            log.error("Failed to parse file : " + e.printStackTrace())
        } finally {
            closeResult = close()
            if (!closeResult.isEmpty()) {
                result += closeResult
            }
        }
        log.debug("parse() result=" + result)
        return arenaBeans
    }

    /**
     * Save applications.
     * @param arenaBeans List<ArenaBean>
     */
    def save(List<ArenaBean> arenaBeans) {
        log.info("save()")
        List<String> errorImports = new ArrayList<>()
        for(ArenaBean arenaBean : arenaBeans) {
            log.info("parse() search for application :" + arenaBean.appName)
            App app = App.findByName(arenaBean.appName)
            if (app != null) {
                log.info("Save ARENA path for application :" + arenaBean?.arenaPath)

                String path = arenaBean.arenaPath

                app.arenaPath = path
                int pos = path.lastIndexOf(SLASH)
                String desc = path.substring(pos + 1, path.length())

                app.description = desc
                app.save(failOnError: true)
            } else {
                // Save application only for FIM Application
                if (!arenaBean.fimPath.isEmpty()) {
                    app = new App()
                    app.name = arenaBean.appName
                    String path = arenaBean.arenaPath

                    app.arenaPath = path
                    int pos = path.lastIndexOf(SLASH)
                    String desc = path.substring(pos + 1, path.length())

                    app.description = desc + " ( FIM )"
                    app.urls.add(arenaBean.fimPath)
                    app.save(failOnError: true)


                } else if (!arenaBean.fimPath.isEmpty()) {
                    app = new App()
                    app.name = arenaBean.appName
                    String path = arenaBean.arenaPath

                    app.arenaPath = path
                    int pos = path.lastIndexOf(SLASH)
                    String desc = path.substring(pos + 1, path.length())

                    app.description = desc + " ( FEDERATION )"
                    app.urls.add(arenaBean.fimPath)
                    app.save(failOnError: true)



                } else {
                    if (!errorImports.contains(arenaBean.appName)) {
                        errorImports.add(arenaBean.appName + " chemin :" + arenaBean.arenaPath)
                    }
                }
            }
        }
        return errorImports
    }

    /**
     * Extract Data from line <Data>...</Data>
     * @param line
     * @return  String between xml tags or EMPTY.
     */
    def static extractData(String line) {
        String result = EMPTY
        if (line == null) {
            log.warn("extractData() line is null.Cannot extractData from XML file")
            return EMPTY
        }
        line = line.trim()
        if (line.startsWith(DATA_START)) {
            result = line.substring(DATA_START.length())

            int posEndData = result.indexOf(DATA_END)
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
