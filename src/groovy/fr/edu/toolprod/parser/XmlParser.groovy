package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.AppBean
import org.apache.commons.logging.LogFactory

/**
 * Parse XML tag.
 */
class XmlParser {


    private static final String SERVER_NAME = "ServerName"

    /**
     * Default port for the server web
     */
    private static final String DEFAULT_PORT = "80"

    private static final String SERVER_LISTEN = "Listen"

    private static final String HTTP = "http://";

    private static final String HTTPS = "https://";

    private static final String EMPTY = ""

    private static final String SPACE = " "

    private static final Character COLON = ':'

    private static final Character SEMICOLON = ','

    private static final String SLASH = "/"

    private static final String OPEN_BRACKET = "("

    private static final String CLOSE_BRACKET = ")"

    private static final String MORE_THAN = ">"

    private static final String PIPE = "|"

    private static final String HASH = "#"

    private static final log = LogFactory.getLog(this)

    private static final String HTTPD_FILENAME_START = "httpd.conf."

    private static final String WEBLOGIC_CLUSTER = "WebLogicCluster"

    private static final String WEBLOGIC_HOST = "WebLogicHost"

    private static final String WEBLOGIC_PORT = "WebLogicPort"

    /**
     * Extract server and port in ProxyPass
     * @param line (e.g : http://webX.fr:PORT/APPLI or https://webX.fr:PORT/APPLI )
     * @return AppBean or NULL if bad ProxyPass Line
     */
    def static parseProxyPass(String line, String fileName) {
        log.info("parseProxyPass() Extract ProxyPass:" + line)
        AppBean appBean = null;

        if (line != null) {
            def params = line.tokenize()
            final int NB_LINE_ELEMENT = 3; // Number element in ProxyPass line.
            if (params.size() == NB_LINE_ELEMENT ) {
                def tmpApp = params.get(1);
                String appName = parseAppNameInProxyPass(tmpApp);
                String appUrl = params.get(2);

                // extract server and port from http://webX.fr:PORT/APPLI or https://webX.fr:PORT/APPLI
                log.debug("parseProxyPass() appurl:" + appUrl)
                String appServer = parseServerFromHttpProxyPass(appUrl);
                log.debug("parseProxyPass() appServer:" + appServer)

                String appPort = parsePortFromHttpProxyPass(appUrl);
                log.debug("parseProxyPass() appPort:" + appPort)


                appBean = new AppBean();
                appBean.name = appName;
                if (appName.isEmpty()) {
                    log.warn("parseProxyPass() Name not found => Get the name in filename:" + fileName)
                    //Get the name in filename
                    appBean.name = fileName
                    if(appBean.name.contains(HTTPD_FILENAME_START)) {
                        appBean.name = appBean.name.substring(HTTPD_FILENAME_START.length(),appBean.name.length())
                    }
                }
                appBean.serverUrl = appUrl
                appBean.serverPort = appPort
                appBean.appServer = appServer
                appBean.appPort = appPort
            }
        }
        log.debug("parseProxyPass() Extract ProxyPass:" + appBean.toString())

        return appBean
    }

    /**
     * Extract module in LoadModule
     * @param str e.g:LoadModule access_module modules/mod_access.so
     * @return module (e.g:access_module)
     */
    def static parseLoadModule(String strLine) {
        String module = EMPTY
        if ((strLine != null) && (!strLine.isEmpty())) {
            def params = strLine.tokenize();
            final int NB_LINE_ELEMENT = 3; // Number element in LoadModule line.
            if (params.size() == NB_LINE_ELEMENT ) {
                module = params.get(1);
            }
        }
        module
    }



    /**
     * Extract name of Server in ServerName.
     * e.g : ServerName web...
     * @param line of httpd.conf
     * @return ServerName
     */
    def static parseServerName(String line) {
        String result = ""
        if ((line != null) && line.startsWith(SERVER_NAME)) {
            def params = line.tokenize()
            if ((params != null) && (params.size()>=2)) {
                result = params.get(1);
            }
        }
        return result
    }


    /**
     * Extract port number in Listen line.
     * e.g : Listen 8082
     * @param line of httpd.conf
     * @return String port number (or 80 if nothing set )
     */
    def static parseListen(String line) {
        String result = EMPTY
        if ((line != null) && line.startsWith(SERVER_LISTEN)) {
            result = DEFAULT_PORT
            def params = line.tokenize()
            if ((params != null) && (params.size()>=2)) {
                result = params.get(1);
            }
        }
        return result
    }

    /**
     * Extract weblogic port from WebLogicPort line.
     * e.g : WebLogicPort 10507
     * @param line of httpd.conf
     * @return String port number
     */
    def static parseWebLogicPort(String line) {
        def result = EMPTY
        if ( (line!= null) && (line.contains(WEBLOGIC_PORT)) && (!line.startsWith(HASH))) {
            int startOfWebLogicPort = line.indexOf(WEBLOGIC_PORT)
            if (startOfWebLogicPort > -1) {
                int pos = startOfWebLogicPort + WEBLOGIC_PORT.size()
                result = line.substring(pos, line.size())
                result = result.trim()
            }
        }
        log.debug("XmlParser:parseWebLogicPort() line:" + line + " result after parsing:" + result)
        result
    }

    /**
     * Extract weblogic host from WebLogicHost line.
     * e.g : WebLogicHost webapp5.ac-limoges.fr
     * @param line of httpd.conf
     * @return String Host
     */
    def static parseWebLogicHost(String line) {
        def result = EMPTY
        if ( (line!= null) && (line.contains(WEBLOGIC_HOST))) {
            line = line.trim()
            if (!line.startsWith(HASH)) {
                int startOfWebLogicHost = line.indexOf(WEBLOGIC_HOST);
                if (startOfWebLogicHost > -1) {
                    int pos = startOfWebLogicHost + WEBLOGIC_HOST.size();
                    result = line.substring(pos, line.size())
                    result = result.trim()
                }
            }
        }
        log.debug("XmlParser:parseWebLogicHost() line:" + line + " result after parsing:" + result)
        result
    }


    /**
     * Extract a line with server and port in WebLogicCluster
     * @param line (e.g: WebLogicCluster webgrh1.ac-limoges.fr:14012, webgrh2.ac-limoges.fr:14012)
     * @return List<String>{ web1.ac-limoges.fr:7777, web2.ac-limoges.fr:14012 }
     */
    def static parseWebLogicCluster(String line) {
        List<String> weblos = new ArrayList<>()
        log.debug("parseWebLogicCluster() line:" + line)
        if ( (line!= null) && (line.contains(WEBLOGIC_CLUSTER))) {
            line = line.trim()
            if (!line.startsWith(HASH)) {
                def params = line.tokenize(SEMICOLON)
                final String weblogicClusterLine = WEBLOGIC_CLUSTER + SPACE

                log.debug("parseWebLogicCluster() ================> params weblo for line:" + line + " params:" + params.toString())
                for(String weblo :params) {
                    log.debug("param :" + weblo)
                    weblo = weblo.trim()
                    if (weblo.contains(weblogicClusterLine)) {
                        int pos = 0;
                        int startOfWeblogicClusterLine = weblo.indexOf(weblogicClusterLine);
                        if (startOfWeblogicClusterLine > -1) {
                            pos = startOfWeblogicClusterLine + weblogicClusterLine.size();
                        }
                        weblo = weblo.substring(pos, weblo.size())
                    }
                    if (!weblo.isEmpty()) {
                        weblos.add(weblo)
                    }
                }
            }
        }
        //log.info("parseWebLogicCluster() result for line " + line  + " => weblos:" + weblos.toString())
        return weblos
    }


    /**
     * Extract app name without '/'
     * @param param e.g: '/app/'
     * @return empty or string(e.g:'app')
     */
    def static parseAppNameInProxyPass(String param) {
        String result = ""
        if ((param != null) && (param.size() > 1)) {
            result = param;
            if (result.startsWith(SLASH)) {
                result = result.substring(1);
            }
            if (result.endsWith(SLASH)) {
                result = result.substring(0, (result.length() - 1))
            }
        }
        result
    }



    /**
     * Extract server in HTTP ProxyPass
     * @param myUrl e.g: http://webX.fr:PORT/APPLI
     * @return server e.g : webX.fr in http://webX.fr:PORT/APPLI
     */
    def static parseServerFromHttpProxyPass(String myUrl) {
        String result = EMPTY;
        String strProtocol = parseProtocol(myUrl)

        if ((myUrl != null) && (!strProtocol.isEmpty()) && (myUrl.contains(COLON.toString()))) {
            // extract after http://
            int beginIndex = strProtocol.size();
            String str = myUrl.substring(beginIndex);

            log.debug("parseServerFromHttpProxyPass() str:" + str)
            int pos = str.indexOf(COLON.toString());
            if (pos > 0) {
                // extract before :
                result = str.substring(0, pos);
                log.debug("parseServerFromHttpProxyPass() extract before : result:" + str)

            } else { //webX.fr/appli/
                pos = str.indexOf(SLASH);
                if (pos > 0) {
                    result = str.substring(0, pos);
                } else {
                    result = str;
                }
                log.debug("parseServerFromHttpProxyPass() else : result:" + str)


            }
        }
        log.debug("parseServerFromHttpProxyPass() Protocol:" + strProtocol)
        return result;
    }


    /**
     * Extract server in HTTPS ProxyPass
     * @param myUrl e.g: http://webX.fr:PORT/APPLI
     * @return port e.g : PORT in http://webX.fr:PORT/APPLI
     */
    def static parsePortFromHttpProxyPass(String myUrl) {
        String strPort = DEFAULT_PORT;
        String strProtocol = parseProtocol(myUrl)
        if ((myUrl != null) && (!strProtocol.isEmpty()) && (myUrl.contains(COLON.toString()))) {
            // extract after http://
            int beginIndex = HTTP.size();
            String str = myUrl.substring(beginIndex);

            int pos = str.indexOf(COLON.toString());
            if (pos > 0) {
                // extract before :
                str = str.substring(pos+1);
                if ((str != null) && (str.contains(SLASH))) {
                    pos = str.indexOf(SLASH);
                    if (pos > 0) {
                        strPort = str.substring(0, pos);
                    }
                }
            }
        }
        return strPort;
    }

    /**
     * Extract protocol HTTP or HTTP from url.
     * @param myUrl e.g:http://webX.fr:PORT/APPLI
     * @return HTTP or HTTP. If no protocol set, return ""
     */
    def static String parseProtocol(String myUrl) {
        String strProtocol = EMPTY
        log.debug("parseProtocol() myUrl:" + myUrl)
        if ( myUrl != null ) {
            if (myUrl.startsWith(HTTP)) {
                strProtocol = HTTP;
            } else if (myUrl.startsWith(HTTPS)) {
                strProtocol = HTTPS;
            } else {
                log.debug("parseProtocol() : Can't extract server because no protocol is set.")
                strProtocol = EMPTY;
            }
        }
        log.debug("parseProtocol() strProtocol:" + strProtocol)
        strProtocol
    }

    /**
     * Parse Name contains in <Location /NAME> line.
     * It will delete / and > in /NAME>
     * @param line e.g: <Location /NAME>
     * @param fileName
     * @return NAME or name from httpd.conf.name ( see getNameFromFileName )
     */
    def static parseLocationName(String line, String filename) {
        log.debug("ParseLocationName() line:" + line)
        String name = parseLocation(line)
        if (name.isEmpty()) {
            name = getNameFromFileName(filename)
        } else {
            int pos = name.indexOf(SLASH)
            if (pos > 0) {  // e.g : app/racvision => delete all after /
                name = name.substring(0, pos)
            }
        }
        log.debug("ParseLocationName() return name:" + name)

        return name
    }

    /**
     * Parse Location and return app name
     * See parseLocationName to obtain a default name from file.
     * @param line  e.g : <Location /NAME>
     * @return EMPTY if error.
     */
    def static parseLocation(String line) {
        String name = EMPTY
        final int MIN_NB_PARAM = 2
        if (line != null) {
            def params = line.tokenize()
            log.debug("ParseLocationName() number of param  :" + params.size())
            // PARAM_SIZE >= 2 in the following case <Location test >
            if ((params != null) && (params.size() >= MIN_NB_PARAM) ) {
                String location = params.get(0)
                String locationName = params.get(1)
                String lastParam = params.get(params.size() - 1)
                lastParam = lastParam?.trim()
                log.debug("ParseLocationName() location:" + location + " locationName:" + locationName + " lastparam : " + lastParam)
                if ((location != null) && (location.contains("<Location")) && (locationName != null)) {
                    name = locationName;
                    int begin = 0;
                    if (locationName.startsWith(SLASH)) {
                        begin = 1
                    }
                    if (locationName.endsWith(MORE_THAN)) {
                        name = locationName.substring(begin, (name.length() - 1))
                    }
                    if (lastParam.equals(MORE_THAN)) {
                        name = locationName.substring(begin, name.length())
                    }

                }
            }
        }
        if (name == null) {
            name = EMPTY
        }
        return name
    }

    /**
     * Get name from http.conf.name file.
     * @param filename Name of the file ( e.g : httpd.conf.name ).
     * @return
     */
    public static String getNameFromFileName(String filename) {
        log.warn("getNameFromFileName() Name not found => Get the name in filename:" + filename)
        //Get the name in filename
        String name = filename
        if (name.contains(HTTPD_FILENAME_START)) {
            name = name.substring(HTTPD_FILENAME_START.length(), name.length())
        }
        name
    }

    /**
     * Parse shortUrl app in Location tag.
     * e.g : <Location /NAME/racvision> will return racvision.
     * e.g : app/(collecte|etablissement|racvision) will return a list : {collecte;etablissement;racvision}
     * @param e.g : line <Location /NAME/racvision>
     * @return  list od short url.
     */
    public static List<String> parseLocationPathAfterName(String line) {
        log.debug("parseLocationPathAfterName() line:" + line)
        List<String> shortUrls = new ArrayList<>()
        String name = parseLocation(line)
        if (!name.isEmpty()) {
            int pos = name.indexOf(SLASH)
            if (pos > 0) {  // e.g : app/racvision => delete all after /
                name = name.substring(pos, name.length() - 1)
                if (name.contains(OPEN_BRACKET)) {
                    name = name.trim()  // delete space
                    name = name.replace(OPEN_BRACKET, EMPTY)
                    name = name.replace(CLOSE_BRACKET, EMPTY)
                    for(String str : name.split(PIPE)) {
                        shortUrls.add(name)
                    }
                } else {
                    shortUrls.add(name)
                }
            }
        }
        log.debug("parseLocationPathAfterName() return name:" + name)
        return shortUrls
    }
}
