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
        machine = Machine.findByName(machineName)
        if (machine == null) {
            machine = new Machine(name: machineName, ipAddress: "127.0.0.1")
            machine.save()
            log.info("Save OK machine:" + machine)
        } else {
            log.debug("Machine still exists :" + machine)
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
            br = new BufferedReader(new InputStreamReader(inputStream))

            while ((strLine = br.readLine()) != null) {
                // If ServerName
                if (strLine.startsWith(SERVER_NAME)) {
                    def params = strLine.tokenize()
                    serverName = params.get(1);
                    serverBean.name = serverName;
                }
                // If port
                if (strLine.startsWith(SERVER_PORT)) {
                    def params = strLine.tokenize()
                    def portTmp = params.get(1);
                    if (portTmp != null) {
                        port = portTmp;
                    }
                    serverBean.portNumber = port;
                }

                // If LoadModule
                if (strLine.startsWith(SERVER_MODULE + SPACE)) {
                    def tmp = getApacheModules(strLine);
                    if (tmp != null && !tmp.isEmpty()) {
                        serverBean.addToModules(tmp);
                    }
                }

                if (strLine.startsWith(PROXY_PASS + SPACE)) {
                    def params = strLine.tokenize()
                    final int NB_LINE_ELEMENT = 3; // Number element in ProxyPass line.
                    if (params.size() == NB_LINE_ELEMENT ) {
                        def tmpApp = params.get(1);
                        appName = extractAppNameInProxyPass(tmpApp);
                        appUrl = params.get(2);

                        // extract server and port from http://webX.fr:PORT/APPLI or https://webX.fr:PORT/APPLI
                        appServer = extractServerFromHttpProxyPass(appUrl);
                        appPort = extractPortFromHttpProxyPass(appUrl);

                        AppBean appBean = new AppBean();
                        appBean.name = appName;
                        appBean.serverUrl = appUrl;
                        appBean.serverPort = appPort;
                        appBeans.add(appBean);
                    }

                }
            }

        } catch (IOException e) {
            bResult = false
            result += "Impossible de parser le fichier !<br/>"
            log.error("Failed to parse file : " + e.printStackTrace())
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    bResult = false
                    result += "Impossible de parser le fichier !<br/>"
                    log.error("Failed to parse file : " + e.printStackTrace())
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    bResult = false
                    result += "Impossible de parser le fichier !<br/>"
                    log.error("Failed to parse file : " + e.getMessage())
                    e.printStackTrace();
                }
            }
        }
        log.info("ServerBean info:" + serverBean.toString());

        Server server;
        if ( (serverBean.name == null)) {
            log.warn("No existing server name found.Create Default server APACHE with name :" + machine.name)
            server = new Server(name:machine.name, portNumber: 80, serverType: Server.TYPE.APACHE )
        } else {
            server = Server.saveServer(serverBean)
        }

        if (server == null) {
            result = "Import du fichier impossible: Pas de serveur web Apache associé au fichier."
            return false;
        }

        // SAVE
        // Create Machine
        // Create Server
        if (!machine.getServers()?.contains(server)) {
            machine.addServer(server);
            machine.save();
            log.info("Save OK server " + serverName + " in machine " + machine.name);
        }

        log.info("Number of application found in this file :" + appBeans.size())

        for (AppBean appBean : appBeans) {
            App app = null;


            App lineApp = App.findByName{name==appBean.name}
            if ( lineApp == null ) {
                if (appBean.description == null) {
                    log.info("Initialize description to EMPY")
                    appBean.description = "EMPTY";
                }
                app = new App(name: appBean.name, description: appBean.description, url:appBean.serverUrl )
                app.save();
                log.info("Save App " + appBean.name + " OK")
                result = result + "Ajout de l'application :" + appBean.name + "\n"
            } else {
                log.warn("App " + appBean.name + "still exists in database.")
            }


            if (app != null) {
                server.addToLinkApps(appBean.name);
                server.save();

                machine.addApplication(app)
                machine.addServer(server)
                if (!machine.save()) {
                    log.error("Can't Save machine " + appServer)
                } else {
                    log.info("Save machine OK:" + appServer)
                }
            }


        }
        if (bResult) {
            result += " Import SUCCESS"
        } else {
            result += " Import FAILED"
        }

    }



    /**
     * Parse file line by line
     * @param inputStream
     * @return true if no error occurs during parsing.
     */
  /*  def parse() {
        boolean bResult = false;

        def serverName = EMPTY
        def appName = EMPTY
        def appUrl = EMPTY
        def appServer = EMPTY
        def appPort = EMPTY

        log.info("Start parsing file ...")
        if ((server != null) && (inputStream != null)) {
            try {
                br = new BufferedReader(new InputStreamReader(inputStream))
                String strLine
                log.info("Ici")

                while ((strLine = br.readLine()) != null) {
                    log.info("Line : " + strLine)

                    //If ProxyPass               /appli http://webX.fr:PORT/APPLI
                    if (strLine.startsWith(PROXY_PASS + SPACE)) {
                        def params = strLine.tokenize()
                        final int NB_LINE_ELEMENT = 3; // Number element in ProxyPass line.
                        if (params.size() == NB_LINE_ELEMENT ) {
                            def tmpApp = params.get(1);
                            appName = extractAppNameInProxyPass(tmpApp);
                            appUrl = params.get(2);

                            // extract server and port from http://webX.fr:PORT/APPLI or https://webX.fr:PORT/APPLI
                            appServer = extractServerFromHttpProxyPass(appUrl);
                            appPort = extractPortFromHttpProxyPass(appUrl);



                            App lineApp = App.findByName(appName)
                            if ( lineApp == null ) {
                                //TODO : description
                                App app = new App(name: appName, description: "test", url: appUrl )
                                app.addServer(server)
                                if (!app.save()) {
                                    log.info("Can't save application app :" + app);
                                } else {
                                    log.info("Save application app OK :" + app)
                                    server.addToLinkApps(appName)
                                    server.save()
                                    log.info("Save in list apps of server OK :" + appName)
                                    Machine machine = Machine.findByName(appServer)
                                    if (machine == null) {
                                        machine = new Machine(name: appServer, ipAddress: "127.0.0.1")
                                    }
                                    machine.addApplication(app)
                                    machine.addServer(server)
                                    if (!machine.save()) {
                                        log.error("Can't Save machine " + appServer)
                                    } else {
                                        log.info("Save machine OK:" + appServer)
                                    }

                                }
                            } else {
                                log.debug("Application App " + appName + " still exists in database.")
                            }
                        }
                    }

                }
                bResult = true;
            } catch (IOException e) {
                bResult = false;
                log.error("Failed to parse file : " + e.printStackTrace())
            }

        } else {
            log.error("Can't parse a null file.")
        }
        log.info("End of parsing file ")
        return bResult
    }*/

    def close() {
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
     *
     * @param str e.g:LoadModule access_module modules/mod_access.so
     */
    def getApacheModules(String strLine) {
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
     * Extract xml part of http.conf and create a new xml with it.
     * @param inputStream
     * @return xml string (e.g:<xml></xml>)
     */
    def parseLocationFromApacheConf(InputStream inputStream) {
        def xml = "" //TODO : unused

        BufferedReader br;
        if (inputStream != null) {
            try {

                br = new BufferedReader(new InputStreamReader(inputStream))
                String strLine

                boolean bXml = false

                String name
                List<String> weblos = new ArrayList<>() //TODO put a list
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
                            def params = strLine.tokenize(",")
                            log.info("params wbelo:" + params.toString())
                            for(String weblo :params) {
                                log.info("weblo:" + weblo)
                                if (!weblo.contains("WebLogicCluster")) {
                                    weblos.add(weblo)
                                }
                            }
                        }
                    }

                    if (strLine.startsWith("</Location>")) {
                        xml += strLine

                        log.debug("name:" + name)
                        log.debug("weblo:" + weblos.toString())
                        log.debug("CT:" + bCT)

                        App app = App.findByName(name)
                        if ( app == null ) {
                            app = new App(name: name, description: "TODO_NOTNULL", url: "TODO_NOTNULL" )

                            if (app.save()) {
                                log.info("Save OK : App save:" + app.name)
                            } else {
                                log.error("error can't save Machine:" + name)
                            }
                        } else {
                            log.info("Application still exist")
                        }


                        for(String str : weblos) {
                            def params = str.tokenize(":")
                            log.info(params.toString())
                            if (params.size() == 2) {
                                String machinName = params.get(0)
                                String port = params.get(1)


                                Server server = Server.findByName(machinName)
                                if (server == null) {
                                    server = new Server(name: machinName, portNumber: port, serverType: Server.TYPE.WEBLOGIC)
                                }
                                server.addToLinkApps(machinName)
                                boolean bSave = server.save()
                                if (bSave) {
                                    log.info("Save OK : Server save:" + machinName)
                                } else {
                                    log.error("error can't save Server:" + machinName)
                                }


                                log.info("Searching machine name :" + machinName)
                                Machine machine = Machine.findByName(machinName);
                                if ((machine == null) && (machinName != null) ) {
                                    machine = new Machine(name:machinName, ipAddress: "127.0.0.1");
                                }
                                machine.addApplication(app)
                                machine.addServer(server)
                                bSave = machine.save()
                                if (bSave) {
                                    log.info("Save OK : Machine save:" + machinName)
                                } else {
                                    log.error("error can't save Machine:" + machinName)
                                }
                                app.addServer(server)

                            }

                            }
                            bXml = false
                        }
                        //TODO : Add app server

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
