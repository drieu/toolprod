package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.AppBean
import org.apache.commons.logging.LogFactory

/**
 * Parse XML tag.
 * User: drieu
 * Date: 18/07/14
 */
class XmlParser {


    private static final String SERVER_NAME = "ServerName"

    private static final String SERVER_PORT = "80"
    private static final String SERVER_LISTEN = "Listen"


    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final String EMPTY = ""
    private static final String SPACE = " "
    private static final Character COLON = ':';
    private static final Character SEMICOLON = ',';
    private static final String SLASH = '/'
    private static final log = LogFactory.getLog(this)

    /**
     * Extract server and port in ProxyPass
     * @param line (e.g : http://webX.fr:PORT/APPLI or https://webX.fr:PORT/APPLI )
     * @return AppBean or NULL if bad ProxyPass Line
     */
    def static parseProxyPass(String line) {
        log.info("Extract ProxyPass:" + line)
        AppBean appBean = null;

        if (line != null) {
            def params = line.tokenize()
            final int NB_LINE_ELEMENT = 3; // Number element in ProxyPass line.
            if (params.size() == NB_LINE_ELEMENT ) {
                def tmpApp = params.get(1);
                String appName = XmlParser.parseAppNameInProxyPass(tmpApp);
                String appUrl = params.get(2);

                // extract server and port from http://webX.fr:PORT/APPLI or https://webX.fr:PORT/APPLI
                String appServer = XmlParser.parseServerFromHttpProxyPass(appUrl);//TODO : ?????
                String appPort = XmlParser.parsePortFromHttpProxyPass(appUrl);

                appBean = new AppBean();
                appBean.name = appName;
                appBean.serverUrl = appUrl;
                appBean.serverPort = appPort;
            }
        }
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
            result = SERVER_PORT
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
        if ( (line!= null) && (line.contains("WebLogicPort")) && (!line.startsWith("#"))) {
            int pos = 0;
            int startOfWebLogicPort = line.indexOf("WebLogicPort");
            if (startOfWebLogicPort > -1) {
                pos = startOfWebLogicPort + "WebLogicPort".size();
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
        if ( (line!= null) && (line.contains("WebLogicHost"))) {
            line = line.trim()
            if (!line.startsWith("#")) {
                int pos = 0;
                int startOfWebLogicHost = line.indexOf("WebLogicHost");
                if (startOfWebLogicHost > -1) {
                    pos = startOfWebLogicHost + "WebLogicHost".size();
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
        if ( (line!= null) && (line.contains("WebLogicCluster"))) {
            line = line.trim()
            if (!line.startsWith("#")) {
                def params = line.tokenize(SEMICOLON)
                final String weblogicClusterLine = "WebLogicCluster" + SPACE

                log.debug("parseWebLogicCluster() params wbelo:" + params.toString())
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
        log.debug("parseWebLogicCluster() weblos:" + weblos.toString())
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
            if (result.startsWith("/")) {
                result = result.substring(1);
            }
            if (result.endsWith("/")) {
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
        String strProtocol = XmlParser.parseProtocol(myUrl)

        if ((myUrl != null) && (!strProtocol.isEmpty()) && (myUrl.contains(COLON.toString()))) {
            // extract after http://
            String str = EMPTY;
            int beginIndex = strProtocol.size();
            str = myUrl.substring(beginIndex);

            int pos = str.indexOf(COLON.toString());
            if (pos > 0) {
                // extract before :
                result = str.substring(0, pos);
            } else { //webX.fr/appli/
                pos = str.indexOf(SLASH);
                if (pos > 0) {
                    result = str.substring(0, pos);
                } else {
                    result = str;
                }
            }
        }
        return result;
    }


    /**
     * Extract server in HTTPS ProxyPass
     * @param myUrl e.g: http://webX.fr:PORT/APPLI
     * @return port e.g : PORT in http://webX.fr:PORT/APPLI
     */
    def static parsePortFromHttpProxyPass(String myUrl) {
        String strPort = EMPTY;
        String strProtocol = parseProtocol(myUrl)
        if ((myUrl != null) && (!strProtocol.isEmpty()) && (myUrl.contains(COLON.toString()))) {
            // extract after http://
            String str = EMPTY;
            int beginIndex = HTTP.size();
            str = myUrl.substring(beginIndex);

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
     * @return NAME or ""
     */
    def static parseLocationName(String line) {
        String name = EMPTY
        final int PARAM_SIZE = 2
        log.debug("ParseLocationName() line:" + line)
        if (line != null) {
            def params = line.tokenize()
            log.debug("ParseLocationName() number of param  :" + params.size())
            // PARAM_SIZE >= 2 in the following case <Location test >
            if ((params != null) && (params.size() >= PARAM_SIZE) ) {
                String location = params.get(0)
                String locationName = params.get(1)
                log.debug("ParseLocationName() location:" + location + " locationName:" + locationName)
                if ((location != null) && (location.contains("<Location")) && (locationName != null)) {
                    name = locationName;
                    int begin = 0;
                    if (locationName.startsWith("/")) {
                        begin = 1
                    }
                    if (locationName.endsWith(">")) {
                        name= locationName.substring(begin, (name.length() - 1))
                    }
                }
            }
        }
        log.debug("ParseLocationName() return name:" + name)

        return name
    }
}
