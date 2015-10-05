package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.AppBean
import fr.edu.toolprod.bean.MultipartFileBean
import fr.edu.toolprod.bean.ServerBean
import org.apache.commons.logging.LogFactory
import toolprod.Machine
import toolprod.MachineGroup

/**
 * Parse httpd.conf file.
 */
class HttpdParser {

/**
 * Constants
 */

    private static final String EMPTY = ""

    private static final String COLON = ":"

    private static final String PERIOD = "."

    private static final String HASH = "#"

    private static final String SPACE = ' '

    private static final String DEFAULT_PORT = "80"

    private static final String PROXY_PASS = "ProxyPass"

    private static final String SERVER_MODULE = "LoadModule"

    private static final String SERVER_NAME = "ServerName"

    private static final String SERVER_PORT = "Listen"

    private static final String WEBLOGIC_HOST = "WebLogicHost"

    private static final String WEBLOGIC_PORT = "WebLogicPort"

    private static final String LOCATION_START = "<Location"

    private static final String LOCATION_END = "</Location>"

    private static final String LOCATIONMATCH_START = "<LocationMatch"

    private static final String LOCATIONMATCH_END = "</LocationMatch>"

    private static final String VIRTUALHOST_START = "<VirtualHost"

    private static final String HTTPD_FILENAME_START = "httpd.conf."

    private static final log = LogFactory.getLog(this)

/**
 * Variables
 */

    /**
     * Machine for the httpd.conf parsed.
     */
    private String machineName

    ServerBean getServerBean() {
        return serverBean
    }

    private ServerBean serverBean

    /**
     * Getter use for unit test.
     * @return
     */
    List<AppBean> getAppBeans() {
        return appBeans
    }

    private List<AppBean> appBeans

    private MultipartFileBean file

    /**
     * Use to read file.
     */
    private BufferedReader br;

    /**
     * Result of parsing.
     */
    private String result = EMPTY

    /**
     * Result of parsing when closing file.
     */
    private String closeResult = EMPTY

    /**
     * Getter.
     * @return
     */
    String getResult() {
        return result
    }

    /**
     * Constructor.
     * @param f File
     * @param machineName : Machine name
     * @param portalsChoice : List of portail where application will be shown
     */
    HttpdParser(MultipartFileBean f, final String name) {
        if (f == null) {
            throw new IllegalArgumentException("MultipartFileBean must not be null !")
        }
        file = f;
        machineName = name
    }

    HttpdParser(final String name) {
        machineName = name
    }



    def parse(File f) {
        boolean bResult = true;

        String strLine
        serverBean = new ServerBean()
        appBeans = new ArrayList<>()
        serverBean.machineHostName = machineName
        try {

            boolean bLocationTag = false; // identify begin and end of xml tag Location

            String name = EMPTY
            String WebLogicHost = EMPTY
            String WebLogicPort = EMPTY
            List<String> weblos = new ArrayList<>()
            List<String> shortUrls = new ArrayList<>()

            log.info("Parse httpd.conf file :" + f.name)
            InputStream inputStream = new FileInputStream(f)
            br = new BufferedReader(new InputStreamReader(inputStream))
            while ((strLine = br.readLine()) != null) {
                strLine = strLine.trim()
                if (strLine.startsWith(SERVER_NAME)) { // If ServerName
                    serverBean.name = XmlParser.parseServerName(strLine)

                } else if (strLine.startsWith(SERVER_PORT)) {// If port
                    serverBean.portNumber = XmlParser.parseListen(strLine)

                } else if (strLine.startsWith(SERVER_MODULE + SPACE)) { // If LoadModule
                    def tmp = XmlParser.parseLoadModule(strLine);
                    if (tmp != null && !tmp.isEmpty()) {
                        serverBean.addToModules(tmp);
                    }

                } else if (strLine.startsWith(PROXY_PASS + SPACE)) { // If ProxyPass
                    AppBean appBean = XmlParser.parseProxyPass(strLine, f.name)
                    if ( (appBean != null)) {
                        appBean.setUrls(serverBean.name, serverBean.portNumber, appBean.name, shortUrls)
                        appBeans.add(appBean);
                    }
                } else if (( !strLine.startsWith(HASH) && strLine.contains(WEBLOGIC_HOST))) { // If WebLogicHost
                    WebLogicHost = XmlParser.parseWebLogicHost(strLine)

                } else if (( !strLine.startsWith(HASH) && strLine.contains(WEBLOGIC_PORT))) { // If WebLogicPort
                    WebLogicPort = XmlParser.parseWebLogicPort(strLine)

                } else if ( (strLine.startsWith(LOCATIONMATCH_START + SPACE))) {// If LocationMatch
                    def params = strLine.tokenize()
                    String xmlStart = params.get(0)

                    name = XmlParser.parseLocationName(strLine, f.name) //extract name for Location
                    shortUrls = XmlParser.parseLocationPathAfterName(strLine)
                    if (xmlStart != null) {
                        bLocationTag = true
                    }

                } else if (strLine.startsWith(LOCATIONMATCH_END)) {  // LocationMatch is parsed only for WebLogicCluster
                    if (!weblos.isEmpty()) {
                        AppBean appBean = getAppBean(name, serverBean)
                        appBean.setUrls(serverBean.name, serverBean.portNumber, appBean.name, shortUrls)
                        appBean.weblos = weblos
                        appBeans.add(appBean)
                    }
                    weblos = new ArrayList<>()
                    bLocationTag = false

                } else if ( (strLine.startsWith(LOCATION_START + SPACE))) { // If Location
                    def params = strLine.tokenize()
                    String xmlStart = params.get(0)

                    name = XmlParser.parseLocationName(strLine, f.name) //extract name for Location
                    shortUrls = XmlParser.parseLocationPathAfterName(strLine)
                    if (xmlStart != null) {
                        bLocationTag = true
                    }

                } else if (strLine.startsWith(LOCATION_END)) { // Location is parsed only for WebLogicCluster
                    if (!weblos.isEmpty()) {
                        AppBean appBean = getAppBean(name, serverBean)
                        appBean.setUrls(serverBean.name, serverBean.portNumber, appBean.name, shortUrls)
                        appBean.weblos = weblos
                        appBeans.add(appBean);
                    }

                    weblos = new ArrayList<>()
                    bLocationTag = false

                } else if (strLine.startsWith(VIRTUALHOST_START)) {
                    AppBean appBean = new AppBean();
                    String strName = XmlParser.getNameFromFileName(f.name);
                    int pos = strName.indexOf(PERIOD)
                    if (pos > 0) {
                        strName = strName.substring(pos + 1, strName.length())
                    }
                    appBean.name = strName
                    appBean.setUrls(serverBean.name, serverBean.portNumber, appBean.name, shortUrls)
                    appBeans.add(appBean)

                }

                if (bLocationTag) {
                    log.info("Location Tag:" + WebLogicHost + COLON + WebLogicPort)
                    for(String str : XmlParser.parseWebLogicCluster(strLine)) { // If WebLogicCluster
                        weblos.add(str);
                    }

                    if ((WebLogicHost != null) && !WebLogicHost.isEmpty() && (WebLogicPort != null) && !WebLogicPort.isEmpty()) {
                        String str
                        if ((WebLogicPort != null) && !WebLogicPort.isEmpty()) {
                            str = WebLogicHost + COLON + WebLogicPort
                        } else {
                            str = WebLogicHost + COLON + DEFAULT_PORT
                        }
                        log.info("Add into weblos array : 'WebLogicHost:WebLogicPort' str=" + str)
                        weblos.add(str)
                        WebLogicHost = EMPTY
                        WebLogicPort = EMPTY
                    }

                    log.debug("parse() weblos:" + weblos?.toString())
                }
            }

        } catch (IOException e) {
            bResult = false
            result += "Impossible de parser le fichier !<br/>"
            log.error("Failed to parse file : " + e.printStackTrace())
        } finally {
            closeResult = close()
            if (!closeResult.isEmpty()) {
                result += closeResult
                bResult = false
            }
        }

        // If EMPTY httpd.conf create application with name of http.conf.name
        if(appBeans.size() == 0) {
            log.info("parse() : appBeans.size() == 0")
            AppBean appBean = new AppBean();
            appBean.name = XmlParser.getNameFromFileName(f.name);
                appBeans.add(appBean)
            appBean = null
        }

        return bResult
    }
    /**
     * Main method for parse a httpd file.
     * @return bResult true if OK and false if error. ( error message is stored in result private attribute ).
     */
    def parse() {
        boolean bResult = true;

        String strLine
        serverBean = new ServerBean()
        appBeans = new ArrayList<>()
        serverBean.machineHostName = machineName
        try {

            boolean bLocationTag = false; // identify begin and end of xml tag Location

            String name = EMPTY
            String WebLogicHost = EMPTY
            String WebLogicPort = EMPTY
            List<String> weblos = new ArrayList<>()
            List<String> shortUrls = new ArrayList<>()

            br = new BufferedReader(new InputStreamReader(file.inputStream))
            while ((strLine = br.readLine()) != null) {
                strLine = strLine.trim()
                if (strLine.startsWith(SERVER_NAME)) { // If ServerName
                    serverBean.name = XmlParser.parseServerName(strLine)

                } else if (strLine.startsWith(SERVER_PORT)) {// If port
                    serverBean.portNumber = XmlParser.parseListen(strLine)

                } else if (strLine.startsWith(SERVER_MODULE + SPACE)) { // If LoadModule
                    def tmp = XmlParser.parseLoadModule(strLine);
                    if (tmp != null && !tmp.isEmpty()) {
                        serverBean.addToModules(tmp);
                    }

                } else if (strLine.startsWith(PROXY_PASS + SPACE)) { // If ProxyPass
                    AppBean appBean = XmlParser.parseProxyPass(strLine, file.originalFilename)
                    if ( (appBean != null)) {
                        appBean.setUrls(serverBean.name, serverBean.portNumber, appBean.name, shortUrls)
                        appBeans.add(appBean);
                    }
                } else if (( !strLine.startsWith(HASH) && strLine.contains(WEBLOGIC_HOST))) { // If WebLogicHost
                    WebLogicHost = XmlParser.parseWebLogicHost(strLine)

                } else if (( !strLine.startsWith(HASH) && strLine.contains(WEBLOGIC_PORT))) { // If WebLogicPort
                    WebLogicPort = XmlParser.parseWebLogicPort(strLine)

                } else if ( (strLine.startsWith(LOCATIONMATCH_START + SPACE))) {// If LocationMatch
                    def params = strLine.tokenize()
                    String xmlStart = params.get(0)

                    name = XmlParser.parseLocationName(strLine, file.originalFilename) //extract name for Location
                    shortUrls = XmlParser.parseLocationPathAfterName(strLine)
                    if (xmlStart != null) {
                        bLocationTag = true
                    }

                } else if (strLine.startsWith(LOCATIONMATCH_END)) {  // LocationMatch is parsed only for WebLogicCluster
                    if (!weblos.isEmpty()) {
                        AppBean appBean = getAppBean(name, serverBean)
                        appBean.setUrls(serverBean.name, serverBean.portNumber, appBean.name, shortUrls)
                        appBean.weblos = weblos
                        appBeans.add(appBean)
                    }
                    weblos = new ArrayList<>()
                    bLocationTag = false

                } else if ( (strLine.startsWith(LOCATION_START + SPACE))) { // If Location
                    def params = strLine.tokenize()
                    String xmlStart = params.get(0)

                    name = XmlParser.parseLocationName(strLine, file.originalFilename) //extract name for Location
                    shortUrls = XmlParser.parseLocationPathAfterName(strLine)
                    if (xmlStart != null) {
                        bLocationTag = true
                    }

                } else if (strLine.startsWith(LOCATION_END)) { // Location is parsed only for WebLogicCluster
                    if (!weblos.isEmpty()) {
                        AppBean appBean = getAppBean(name, serverBean)
                        appBean.setUrls(serverBean.name, serverBean.portNumber, appBean.name, shortUrls)
                        appBean.weblos = weblos
                        appBeans.add(appBean);
                    }

                    weblos = new ArrayList<>()
                    bLocationTag = false

                } else if (strLine.startsWith(VIRTUALHOST_START)) {
                    AppBean appBean = new AppBean();
                    String strName = XmlParser.getNameFromFileName(file.originalFilename);
                    int pos = strName.indexOf(PERIOD)
                    if (pos > 0) {
                        strName = strName.substring(pos + 1, strName.length())
                    }
                    appBean.name = strName
                    appBean.setUrls(serverBean.name, serverBean.portNumber, appBean.name, shortUrls)
                    appBeans.add(appBean)

                }

                if (bLocationTag) {
                    log.debug("Location Tag:" + WebLogicHost + COLON + WebLogicPort)
                    for(String str : XmlParser.parseWebLogicCluster(strLine)) { // If WebLogicCluster
                        weblos.add(str);
                    }

                    if ((WebLogicHost != null) && !WebLogicHost.isEmpty() && (WebLogicPort != null) && !WebLogicPort.isEmpty()) {
                        String str
                        if ((WebLogicPort != null) && !WebLogicPort.isEmpty()) {
                            str = WebLogicHost + COLON + WebLogicPort
                        } else {
                            str = WebLogicHost + COLON + DEFAULT_PORT
                        }
                        log.info("Add into weblos array : 'WebLogicHost:WebLogicPort' str=" + str)
                        weblos.add(str)
                        WebLogicHost = EMPTY
                        WebLogicPort = EMPTY
                    }

                    log.debug("parse() weblos:" + weblos?.toString())
                }
            }

        } catch (IOException e) {
            bResult = false
            result += "Impossible de parser le fichier !<br/>"
            log.error("Failed to parse file : " + e.printStackTrace())
        } finally {
            closeResult = close()
            if (!closeResult.isEmpty()) {
                result += closeResult
                bResult = false
            }
        }

        // If EMPTY httpd.conf create application with name of http.conf.name
        if(appBeans.size() == 0) {

            log.info("parse() : appBeans.size() == 0")
            AppBean appBean = new AppBean();
            appBean.name = XmlParser.getNameFromFileName(file.originalFilename);
            println(appBean.name)
            appBeans.add(appBean)

        }

        return bResult
    }

    /**
     * Save data.
     * @return
     */
    public boolean save() {
        boolean bResult
        if (machineName == null || machineName.isEmpty()) {
            throw new IllegalArgumentException("MachineName must not be null !")
        }
        Machine machine = Machine.findOrCreateByName(machineName)
        machine.save(failOnError: true, flush:true)
        Data data = new Data(machine)
        bResult = data.save(serverBean, appBeans)
        result += data.result
        return bResult
    }



    /**
     * delete old data and replace.
     * @return
     */
    public boolean overwrite() {
        boolean bResult
        if (machineName == null || machineName.isEmpty()) {
            throw new IllegalArgumentException("MachineName must not be null !")
        }
        Machine machine = Machine.findOrCreateByName(machineName)
        machine.save(failOnError: true, flush:true)
        Data data = new Data(machine)
        bResult = data.save(serverBean, appBeans)
        result += data.result
        return bResult

    }

    /**
     * Initialise an appBean with appName and serverBean
     * @param appName application name.
     * @param serverBean
     * @return
     */
    def static getAppBean(String appName, ServerBean serverBean) {

        AppBean appBean = new AppBean(name:appName);
        appBean.setUrl(serverBean.machineHostName, serverBean.portNumber, appName);
        return appBean
    }


    /**
     * Status in apache configuration if ServerName is equals to machineName
     * @param machineName
     * @param fileName
     * @return  true if no error during parsing
     */
    def check(String machineName, String fileName) {
        boolean bResult = true;
        result = EMPTY;

        String strLine
        try {
            br = new BufferedReader(new InputStreamReader(file.inputStream))
            while ((strLine = br.readLine()) != null) {
                strLine = strLine.trim()
                if (strLine.startsWith(SERVER_NAME)) { // If ServerName

                    String confServerName = XmlParser.parseServerName(strLine)
                    log.info("====>confServerName:" + confServerName)
                    log.info("====>machineName:" + machineName)
                    Data data = new Data(null);
                    if (!machineName.equals(confServerName)) {
                        data.saveCheck(machineName, fileName, confServerName)
                        bResult = false
                    }
                }
            }
            result += EMPTY

        } catch (IOException e) {

            bResult = false
            result += "Impossible de parser le fichier !<br/>"
            log.error("Failed to parse file : " + e.printStackTrace())
        } finally {
            closeResult = close()
            if (!closeResult.isEmpty()) {
                result = closeResult
                bResult = false
            }
        }
        result += result
        return bResult
    }


    /**
     * Close inpustream fo file to parse.
     * @return String result EMPTY if there is no error or a message.
     */
    def close() {
        String result = EMPTY
        if (file != null) {
            try {
                file.inputStream.close();
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
