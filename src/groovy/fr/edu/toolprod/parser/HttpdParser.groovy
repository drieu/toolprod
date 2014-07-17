package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.AppBean
import fr.edu.toolprod.bean.ServerBean
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
    private static final String SERVER_MODULE = "LoadModule"
    private static final String SERVER_NAME = "ServerName"
    private static final String SERVER_PORT = "Listen"
    private static final String MACHINE_DEFAULT_IP = "127.0.0.1"

    private static final log = LogFactory.getLog(this)

    private Machine machine

    private Server server

    private InputStream inputStream

    private BufferedReader br;

    private String result = ""

    String getResult() {
        return result
    }

    HttpdParser(InputStream input, String machineName) {
        inputStream = input;
        defineMachine(machineName);
        if (machine == null) {
            throw new IllegalArgumentException("Machine must exist !")
        }
    }

    private def defineMachine(String machineName) {
        if (machineName == null || machineName.isEmpty()) {
            throw new IllegalArgumentException("MachineName must not be null !")
        }
        machine = Machine.findOrCreateByName(machineName)
        machine.save(failOnError: true)
    }

    /**
     * Extract name of Server in ServerName.
     * e.g : ServerName web...
     * @param line of httpd.conf
     * @return ServerName
     */
    def extractListen(String line) {
        String result = "80"
        if (line.startsWith(SERVER_PORT)) {
            def params = line.tokenize()
            if (params != null) {
                result = params.get(1);
            }
        }
        return result
    }

    /**
     * Extract name of Server in ServerName.
     * e.g : ServerName web...
     * @param line of httpd.conf
     * @return ServerName
     */
    def extractServerName(String line) {
        String result = ""
        if (line.startsWith(SERVER_NAME)) {
            def params = line.tokenize()
            if (params != null) {

                result = params.get(1);

            }
        }
        return result
    }

    /**
     * Extract module in LoadModule
     * @param str e.g:LoadModule access_module modules/mod_access.so
     * @return module (e.g:access_module)
     */
    def extractLoadModule(String strLine) {
        String module = ""
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
     * Extract server and port in ProxyPass
     * @param line (e.g : http://webX.fr:PORT/APPLI or https://webX.fr:PORT/APPLI )
     * @return AppBean
     */
    def extractProxyPass(String line) {
        AppBean appBean = null;
        def params = line.tokenize()
        final int NB_LINE_ELEMENT = 3; // Number element in ProxyPass line.
        if (params.size() == NB_LINE_ELEMENT ) {
            def tmpApp = params.get(1);
            String appName = extractAppNameInProxyPass(tmpApp);
            String appUrl = params.get(2);

            // extract server and port from http://webX.fr:PORT/APPLI or https://webX.fr:PORT/APPLI
            String appServer = extractServerFromHttpProxyPass(appUrl);
            String appPort = extractPortFromHttpProxyPass(appUrl);

            appBean = new AppBean();
            appBean.name = appName;
            appBean.serverUrl = appUrl;
            appBean.serverPort = appPort;
        }
        return appBean
    }

    /**
     * Extract a line with server and port in WebLogicCluster
     * @param line (e.g: WebLogicCluster webgrh1.ac-limoges.fr:14012, webgrh2.ac-limoges.fr:14012)
     * @return
     */
    def extractWebLogicCluster(String line) {
        String result = ""
        if (line.contains("WebLogicCluster")) {
            def params = line.tokenize(",")
            log.debug("params wbelo:" + params.toString())
            for(String weblo :params) {
                log.debug("weblo:" + weblo)

                final String weblogicClusterLine = "WebLogicCluster "
                if (weblo.contains(weblogicClusterLine)) {
                    weblo = weblo.substring(weblogicClusterLine.size() + 1,weblo.size())
                    result =weblo
                }
            }
        }
        return result
    }

    def saveWeblo(List<String> weblos, AppBean appBean) {
        App app = App.findOrCreateByNameAndDescriptionAndUrl(appBean.name,"TODO","http://test.com");
        app.save(failOnError: true)
        log.info("App find or create:" + app)
        for(String str : weblos) {
            log.info("parse weblo string:" + str)
            def params = str.tokenize(":")
            log.info(params.toString())
            if (params.size() == 2) {
                String machinName = params.get(0)
                String portTest = params.get(1)

                Server server = Server.findOrCreateByNameAndPortNumberAndServerType(machinName,portTest,Server.TYPE.WEBLOGIC)
                server.addToLinkApps(appBean.name)
                server.save(failOnError: true)
                log.info("Server find or create:" + server)


                Machine machine = Machine.findOrCreateByName(machinName)
                machine.addApplication(app)
                machine.addServer(server)
                machine.save(failOnError: true)
                app.addServer(server)
                log.info("Machine find or create:" + machine)
            }

        }
    }

    def parse() {
        boolean bResult = true;
        result = "";

        String strLine
        String serverName
        String port = 80
        List<String> modules = []

        def appName = EMPTY
        def appUrl = EMPTY
        def appServer = EMPTY
        def appPort = EMPTY

        ServerBean serverBean = new ServerBean();
        List<AppBean> appBeans = new ArrayList<>();

        try {

            boolean bXml = false

            String name
            List<String> weblos = new ArrayList<>() //TODO put a list
            boolean bCT = false

            br = new BufferedReader(new InputStreamReader(inputStream))

            while ((strLine = br.readLine()) != null) {

                // If ServerName
                if (strLine.startsWith(SERVER_NAME)) {
                    serverBean.name = extractServerName(strLine)
                }

                // If port
                if (strLine.startsWith(SERVER_PORT)) {
                    serverBean.portNumber = extractListen(strLine)
                }

                // If LoadModule
                if (strLine.startsWith(SERVER_MODULE + SPACE)) {
                    def tmp = extractLoadModule(strLine);
                    if (tmp != null && !tmp.isEmpty()) {
                        serverBean.addToModules(tmp);
                    }
                }

                // If ProxyPass
                if (strLine.startsWith(PROXY_PASS + SPACE)) {
                    AppBean appBean = extractProxyPass(strLine)
                    if (appBean != null) {
                        appBeans.add(appBean);
                    }
                }


                if ( (strLine.startsWith("<Location" + SPACE))) {
                    def params = strLine.tokenize()
                    String xmlStart = params.get(0)

                    //extract name for Location
                    name = extractLocationName(strLine)
                    if (xmlStart != null) {
                        String str = xmlStart.substring(1) //e.g:<Location
                        bXml = true
                    }
                }

                if (bXml) {
                    if (strLine.contains("AuthName CT")) {
                        bCT = true
                    }

                    // If WebLogicCluster
                    String weblo = extractWebLogicCluster(strLine)
                    if (!weblo.isEmpty()) {
                        weblos.add(weblo)
                    }

                }

                if (strLine.startsWith("</Location>")) {
                    log.debug("name:" + name + " weblo:" + weblos.toString() + " CT:" + bCT)
                    AppBean appBean = new AppBean(name:name, serverUrl:"http://test.com", serverPort:80);
                    appBeans.add(appBean);
                    saveWeblo(weblos, appBean)

                    bXml = false
                }
            }

        } catch (IOException e) {
            bResult = false
            result += "Impossible de parser le fichier !<br/>"
            log.error("Failed to parse file : " + e.printStackTrace())
        } finally {
            String closeResult = close()
            if (!closeResult.isEmpty()) {
                result = closeResult
                bResult = false
            }
        }
        log.info("ServerBean info:" + serverBean.toString());

        // Create Server
        Server server;
        if ( (serverBean.name == null)) {
            log.warn("No existing server name found.Create Default server APACHE with name :" + machine.name)
            server = new Server(name:machine.name, portNumber: 80, serverType: Server.TYPE.APACHE )
            // TODO ?
        } else {
            server = Server.saveServer(serverBean)
        }

        if (server == null) {
            result = "Import du fichier impossible: Pas de serveur web Apache associé au fichier."
            return false;
        }

        // SAVE
        if (!machine.getServers()?.contains(server)) {
            machine.addServer(server);
            machine.save();
            log.info("Save OK server " + serverName + " in machine " + machine.name);
        }

        log.info("Number of application found in this file :" + appBeans.size())

        for (AppBean appBean : appBeans) {

            if (appBean.description == null) {
                log.info("Initialize description to EMPTY")
                appBean.description = "EMPTY";
            }
            App myApp = App.findOrCreateByNameAndDescriptionAndUrl(appBean.name,appBean.description,appBean.serverUrl)
            log.debug("appBean:" + myApp)
            result = result + appBean.name + "\n"

            if (myApp != null) {
                if (!server.linkToApps.contains(appBean.name)) {
                    log.debug("Save application:" + appBean.name + " in the app list of web server:" + server.name )
                    server.addToLinkApps(appBean.name);
                    server.save(failOnError: true);
                } else {
                    log.debug("Nothing to save application:" + appBean.name + " still exist in the app list of web server:" + server.name )

                }

                // Add to the machine app list only if it's a local application ( same name of machine in URL )
                if (myApp.url.contains(machine.name)) {
                    machine.addApplication(myApp)
                }
                machine.addServer(server)
                if (!machine.save()) {
                    log.error("Can't Save machine " + machine)
                } else {
                    log.info("Save machine OK:" + machine)
                }
            }
        }
        if (bResult) {
            result += " Import SUCCESS"
        } else {
            result += " Import FAILED"
        }

    }



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

    /**
     * Extract app name without '/'
     * @param param e.g: '/app/'
     * @return empty or string(e.g:'app')
     */
    def extractAppNameInProxyPass(String param) {
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
                log.warn("extractServerFromHttpsProxyPass : Can't extract server because no protocol is set.")
                strProtocol = EMPTY;
            }
        }
        strProtocol
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
