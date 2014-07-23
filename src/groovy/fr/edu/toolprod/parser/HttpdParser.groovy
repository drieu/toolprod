package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.AppBean
import fr.edu.toolprod.bean.ServerBean
import fr.edu.toolprod.parser.XmlParser
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

    /**
     * Define Machine before parsing file (use in constructor).
     * @param machineName String.
     */
    private def defineMachine(String machineName) {
        if (machineName == null || machineName.isEmpty()) {
            throw new IllegalArgumentException("MachineName must not be null !")
        }
        machine = Machine.findOrCreateByName(machineName)
        machine.save(failOnError: true)
    }

    /**
     * Main method for parse a httpd file.
     * @return bResult true if OK and false if error. ( error message is stored in result private attribute.
     */
    def parse() {
        boolean bResult = true;
        result = "";

        String strLine
        ServerBean serverBean = new ServerBean();
        List<AppBean> appBeans = new ArrayList<>();

        try {

            boolean bLocationTag = false; // identify begin and end of xml tag Location

            String name
            List<String> weblos = new ArrayList<>() //TODO put a list

            br = new BufferedReader(new InputStreamReader(inputStream))

            while ((strLine = br.readLine()) != null) {

                log.debug("strLine:" + strLine)
                // If ServerName
                if (strLine.startsWith(SERVER_NAME)) {
                    serverBean.name = XmlParser.parseServerName(strLine)
                }

                // If port
                if (strLine.startsWith(SERVER_PORT)) {
                    serverBean.portNumber = XmlParser.parseListen(strLine)
                }

                // If LoadModule
                if (strLine.startsWith(SERVER_MODULE + SPACE)) {
                    def tmp = XmlParser.parseLoadModule(strLine);
                    if (tmp != null && !tmp.isEmpty()) {
                        serverBean.addToModules(tmp);
                    }
                }

                // If ProxyPass
                if (strLine.startsWith(PROXY_PASS + SPACE)) {
                    AppBean appBean = XmlParser.parseProxyPass(strLine)
                    if (appBean != null) {
                        appBeans.add(appBean);
                    }
                }


                if ( (strLine.startsWith("<Location" + SPACE))) {
                    def params = strLine.tokenize()
                    String xmlStart = params.get(0)

                    //extract name for Location
                    name = XmlParser.parseLocationName(strLine)
                    if (xmlStart != null) {
                        String str = xmlStart.substring(1) //e.g:<Location
                        bLocationTag = true
                    }
                }

                if (bLocationTag) {
                    // If WebLogicCluster
                    List<String> lst = XmlParser.parseWebLogicCluster(strLine)
                    for(String str : lst) {
                        weblos.add(str);
                    }
                    log.info(weblos.toString())
                }

                if (strLine.startsWith("</Location>")) {
                    log.debug("name:" + name + " weblo:" + weblos.toString())
                    AppBean appBean = new AppBean(name:name, serverUrl:"http://test.com", serverPort:80);
                    appBeans.add(appBean);
                    log.info("weblos :" + weblos.toString())
                    saveWeblo(weblos, appBean)

                    weblos = new ArrayList<>()
                    bLocationTag = false
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
        if (!saveParsingData(serverBean, appBeans)) {
            bResult = false;
        }


        if (bResult) {
            result += " Import SUCCESS"
        } else {
            result += " Import FAILED"
        }

    }

    /**
     * Save weblogic server and weblogic application in database.
     * @param weblos List<String>
     * @param appBean AppBean
     */
    def saveWeblo(List<String> weblos, AppBean appBean) {
        if ((weblos == null) || (appBean == null)) {
            throw new IllegalArgumentException("Bad parameter fot saveWeblo() method !")
        }
        log.info("saveWeblo() weblos:" + weblos.toString() + " appBean:" + appBean.toString())

        App app = App.findOrCreateByNameAndDescriptionAndUrl(appBean.name,"TODO","http://test.com");
        app.save(failOnError: true)

        log.info("saveWeblo() App find or create:" + app)
        for(String str : weblos) {

            def params = str.tokenize(":")
            log.info(params.toString())
            if (params.size() == 2) {
                String machinName = params.get(0)
                String portTest = params.get(1)

                Server server = Server.findOrCreateByNameAndPortNumberAndServerType(machinName,portTest,Server.TYPE.WEBLOGIC)
                server.addToLinkApps(appBean.name)
                server.save(failOnError: true)
                log.info("saveWeblo() Server find or create:" + server)


                Machine machine = Machine.findOrCreateByName(machinName)
                machine.addApplication(app)
                machine.addServer(server)
                machine.save(failOnError: true)
                log.info("saveWeblo() Machine find or create:" + machine)

                app.addServer(server)
                app.save(failOnError: true)
            }
        }
    }

    /**
     * Save parsing data
     * @param serverBean
     * @param appBeans
     * @return
     */
    def saveParsingData(ServerBean serverBean, List<AppBean> appBeans) {
        log.info("ServerBean info:" + serverBean.toString());
        String port = 80
        boolean bResult = true

        // Create Server
        Server server;
        if ( (serverBean.name == null)) {
            log.warn("No existing server name found.Create Default server APACHE with name :" + machine.name)
            server = new Server(name:machine.name, portNumber: port, serverType: Server.TYPE.APACHE )
            // TODO ?
        } else {
            server = Server.saveServer(serverBean)
        }

        if (server == null) {
            result += "Import du fichier impossible: Pas de serveur web Apache associé au fichier."
            bResult = false;
        } else {
            if (!machine.getServers()?.contains(server)) {
                machine.addServer(server);
                machine.save();
                log.info("Save OK server " + server.name + " in machine " + machine.name);
            }

            log.info("Number of application found in this file :" + appBeans.size())

            saveAppBean(appBeans, server)
        }
        return bResult;
    }

    /**
     * Save a List of AppBean found in parse file.
     * @param appBeans List<AppBean>
     * @param server Server
     */
    def saveAppBean(List<AppBean> appBeans, Server server) {

        if (appBeans == null) {
            throw new IllegalArgumentException("saveAppBean() appBeans must not be null ! ")
        }

        if (server == null) {
            throw new IllegalArgumentException("saveAppBean() server must not be null ! ")
        }


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
