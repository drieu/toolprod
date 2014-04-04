package fr.edu.toolprod.parser

import org.apache.commons.logging.LogFactory
import toolprod.App
import toolprod.Machine
import toolprod.Server

/**
 * Parse httpd.conf file.
 * User: drieu
 * Date: 27/03/14
 * Time: 09:15
 */
class HttpdParser {


    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final Character COLON = ':';
    private static final String SLASH = '/'
    private static final String EMPTY = ""


    private static final String PROXY_PASS = "ProxyPass"
    private static final String SPACE = ' '
    private static final String LOAD_MODULE = "LoadModule"
    private static final String SERVER_NAME = "ServerName"

    private static final log = LogFactory.getLog(this)

    /**
     * Modules Apache list.
     */
    def modules = []

    /**
     * Parse file line by line
     * @param inputStream
     * @return true if no error occurs during parsing.
     */
    def parse(InputStream inputStream) {
        boolean bResult = false;
        BufferedReader br;

        def serverName = EMPTY
        def appName = EMPTY
        def appUrl = EMPTY
        def appServer = EMPTY
        def appPort = EMPTY

        log.info("Start parsing file ")
        if (inputStream != null) {
            try {
                br = new BufferedReader(new InputStreamReader(inputStream))
                String strLine

                while ((strLine = br.readLine()) != null) {

                    //If ProxyPass               /appli http://webX.fr:PORT/APPLI
                    if (strLine.startsWith(PROXY_PASS + SPACE)) {
                        def params = strLine.tokenize()
                        final int NB_LINE_ELEMENT = 3; // Number element in ProxyPass line.
                        if (params.size() == NB_LINE_ELEMENT ) {
                            def tmpApp = params.get(1);
                            appName = tmpApp.substring(1); // don't want the / in /appli
                            appUrl = params.get(2);

                            // extract server and port from http://webX.fr:PORT/APPLI or https://webX.fr:PORT/APPLI
                            appServer = extractServerFromHttpProxyPass(appUrl);
                            appPort = extractPortFromHttpProxyPass(appUrl);



                            App lineApp = App.findByName(appName)
                            if ( lineApp == null ) {
                                App app = new App(name: appName, description: "TEST", url: appUrl )
                                if (!app.save()) {
                                    log.info("Can't save appli:" + appName + " url:" + appUrl + " server:" + appServer + " port:" + appPort);
                                } else {
                                    log.info("Save App OK appli:" + appName + " url:" + appUrl + " server:" + appServer + " port:" + appPort)
                                    Machine machine = Machine.findByName(appServer)
                                    if (machine == null) {
                                        machine = new Machine(name: appServer, ipAddress: "127.0.0.1")
                                    }
                                    Set<App> machineApps = machine.apps
                                    if (machineApps == null) {
                                        machineApps = new HashSet<>()
                                    }
                                    machineApps.add(app)
                                    machine.apps = machineApps
                                    if (!machine.save()) {
                                        log.error("Can't Save machine " + appServer)
                                    } else {
                                        log.info("Save machine " + appServer)
                                    }
                                }
                            } else {
                                log.debug("Application " + appName + " still exists in database.")
                            }
                        }
                    }

                    // If LoadModule
                    if (strLine.startsWith(LOAD_MODULE + SPACE)) {
                        modules = getApacheModules(strLine)
                    }
                    // If ServerName
                    if (strLine.startsWith(SERVER_NAME)) {
                        def params = strLine.tokenize()
                        serverName = params.get(1);
                    }

                }
                bResult = true;
            } catch (IOException e) {
                bResult = false;
                log.error("Failed to parse file : " + e.printStackTrace())
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error("Failed to parse file : " + e.printStackTrace())
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        log.error("Failed to parse file : " + e.getMessage())
                        e.printStackTrace();
                    }
                }
            }
        } else {
            log.error("Can't parse a null file.")
        }
        log.info("End of parsing file ")
        return bResult
    }

    /**
     * Extract server in HTTP ProxyPass
     * @param myUrl e.g: http://webX.fr:PORT/APPLI
     * @return server e.g : webX.fr in http://webX.fr:PORT/APPLI
     */
    def extractServerFromHttpProxyPass(String myUrl) {
        String result = EMPTY;
        String strProtocol = extractProtocol(myUrl)

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
    def extractPortFromHttpProxyPass(String myUrl) {
        String strPort = EMPTY;
        String strProtocol = extractProtocol(myUrl)
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
    private String extractProtocol(String myUrl) {
        String strProtocol = EMPTY
        if ( myUrl != null ) {
            if (myUrl.startsWith(HTTP)) {
                strProtocol = HTTP;
            } else if (myUrl.startsWith(HTTPS)) {
                strProtocol = HTTPS;
            } else {
                log.warning("extractServerFromHttpsProxyPass : Can't extract server because no protocol is set.")
                strProtocol = EMPTY;
            }
        }
        strProtocol
    }

    /**
     *
     * @param str e.g:LoadModule access_module modules/mod_access.so
     */
    def getApacheModules(String strLine) {
        if ((strLine != null) && (!strLine.isEmpty())) {
            def params = strLine.tokenize();
            final int NB_LINE_ELEMENT = 3; // Number element in LoadModule line.
            if (params.size() == NB_LINE_ELEMENT ) {
                String module = params.get(1);
                if ((module != null) && (!module.isEmpty())) {
                    modules.add(module);
                }
            }
        }
        modules
    }



    /**
     * Extract xml part of http.conf and create a new xml with it.
     * @param inputStream
     * @return xml string (e.g:<xml></xml>)
     */
    def extractXmlFromApacheConf(InputStream inputStream) {
        def xml = "" //TODO : unused

        BufferedReader br;
        if (inputStream != null) {
            try {

                br = new BufferedReader(new InputStreamReader(inputStream))
                String strLine

                boolean bXml = false

                String name
                String weblo //TODO put a list
                boolean bCT = false

                while ((strLine = br.readLine()) != null) {
                    if (bXml) {
                        xml += strLine
                    }
                    if ( (strLine.startsWith("<Location" + SPACE))) {
                        def params = strLine.tokenize()
                        String xmlStart = params.get(0)

                        //extract name for Location
                        name = extractLocationName(strLine)
                        if (xmlStart != null) {
                            xml += strLine
                            String str = xmlStart.substring(1) //e.g:<Location
                            bXml = true
                        }
                    }

                    if (bXml) {
                        if (strLine.contains("AuthName CT")) {
                            bCT = true
                        }
                        if (strLine.contains("WebLogicCluster")) {
                            def params = strLine.tokenize()
                            weblo = params.get(1)
                        }
                    }

                    if (strLine.startsWith("</Location>")) {
                        xml += strLine

                        log.debug("name:" + name)
                        log.debug("weblo:" + weblo)
                        log.debug("CT:" + bCT)
                        bXml = false
                    }
                }
            } catch (IOException e) {
                log.error("Failed to parse file : " + e.printStackTrace())
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error("Failed to parse file : " + e.printStackTrace())
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        log.error("Failed to parse file : " + e.getMessage())
                        e.printStackTrace();
                    }
                }
            }
        }
        return xml
    }

    /**
     * Extract Name contains in <Location /NAME> line.
     * It will delete / and > in /NAME>
     * @param line e.g: <Location /NAME>
     * @return NAME or ""
     */
    def extractLocationName(String line) {
        String name = EMPTY

        if (line != null) {
            def params = line.tokenize()
            String location = params.get(0)
            String locationName = params.get(1)
            if ((location != null) && (location.contains("<Location")) && (locationName != null)) {
                name = locationName
                if (locationName.startsWith("/")) {
                    if (locationName.endsWith(">")) {
                        name= locationName.substring(1, (name.length() - 1))
                    }
                }
            }
        }
        return name
    }
}
