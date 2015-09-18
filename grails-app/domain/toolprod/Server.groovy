package toolprod

import fr.edu.toolprod.bean.ServerBean
import org.apache.commons.logging.LogFactory

/**
 * Servers like Apache, Weblogic
 */
class Server implements Comparable{

    /**
     * ServerName in apache conf.
     */
    String name

    /**
     * Server port number.
     */
    Integer portNumber

    /**
     * Hostname.
     */
    String machineHostName


    /**
     * List references application.
     */
    List<String> linkToApps = []

    /**
     * List Apache modules.
     */
    List<String> modules = []
    static hasMany = [ linkToApps: String, modules: String]

    /**
     * TYPE of server ( apache, weblogic )
     */
    TYPE serverType

    enum TYPE {
        APACHE, WEBLOGIC
    }

    /**
     * Logger.
     */
    private static final log = LogFactory.getLog(this)


    private static final int DEFAULT_APACHE_PORT = 80

    /**
     *  DEFAULT_APACHE_PORT.
     */
    static int getDEFAULT_PORT() {
        return DEFAULT_APACHE_PORT
    }


    /**
     *  DEFAULT_WEBLOGIC_PORT.
     */
    private static final int DEFAULT_WEBLOGIC_PORT = 8080


    static int getDEFAULT_WEBLOGIC_PORT() {
        return DEFAULT_WEBLOGIC_PORT
    }

    /**
     *  DEFAULT_IP.
     */
    private static final String DEFAULT_IP = "127.0.0.1"


    static constraints = {
        name()
        portNumber(defaultValue:DEFAULT_APACHE_PORT)
        machineHostName(defaultValue:DEFAULT_IP)
    }

    /**
     * Constructor.
     * @param name
     * @param portNumber
     * @param machineHostName
     */
    Server(String name, String portNumber, String machineHostName) {
        this.name = name
        if (portNumber == null) {
            portNumber = Integer.toString(DEFAULT_APACHE_PORT)
        } else if ( portNumber.isEmpty()) {
            portNumber = Integer.toString(DEFAULT_APACHE_PORT)
        }
        this.portNumber = portNumber.toInteger()
        this.machineHostName = machineHostName
    }

    /**
     * Constructor.
     * @param name
     * @param machineHostName
     * @param portNumber
     * @param serverType
     */
    Server(String name, String machineHostName, Integer portNumber, Server.TYPE serverType ){
        this.name = name
        if ( (portNumber == null) || (portNumber == 0)) {
            portNumber = DEFAULT_APACHE_PORT
        }
        this.portNumber = portNumber
        this.serverType = serverType
        this.machineHostName = machineHostName
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", portNumber=" + portNumber +
                ", linkToApps=" + linkToApps +
                ", serverType=" + serverType +
                ", version=" + version +
                '}';
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Server server = (Server) o

        if (name != server.name) return false
        if (portNumber != server.portNumber) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (portNumber != null ? portNumber.hashCode() : 0)
        return result
    }

    @Override
    int compareTo(Object o) {
        if (o instanceof Server) {
            if ( (this.name.equals(o.name)) && (this.portNumber.equals(o.portNumber)) ) {
                return 0
            }
        }
        return 1
    }

    /**
     * Get port number ( Listen line in apache conf ).
     * return DEFAULT_APACHE_PORT=80 if no values set.
     */
    Integer getPortNumber() {

        int portDefault
        if ( (portNumber == null) || (portNumber == 0)) {
            portDefault = DEFAULT_APACHE_PORT
        } else {
            portDefault = this.portNumber
        }
        return portDefault
    }

    /**
     * Add an application to linkapps
     * @param appName application name.
     */
    def addToLinkApps(String appName) {
        if (linkToApps == null) {
            linkToApps = new ArrayList<String>();
        }
        if (!linkToApps.contains(appName)) {
            linkToApps.add(appName)
        }
        linkToApps.sort()
    }

    /**
     * Save a Server in database by using data in serverBean
     * @param serverBean
     */
    public static saveServer(ServerBean serverBean) {
        if (serverBean == null) {
            throw new IllegalArgumentException("Can't save a Server with a null serverBean !")
        }
        log.info("saveServer() Save serverBean:" + serverBean)
        Integer port = 0
        if (serverBean.portNumber != null) {
           port = serverBean.portNumber.toInteger()
        }
        Server server = findByMachineHostNameAndPortNumber(serverBean.machineHostName, port)
        if (server == null) {
            server = new Server(serverBean.name, serverBean.machineHostName, port, Server.TYPE.APACHE)
            server.modules = serverBean.modules;
            server.save(failOnError: true, flush:true)

        } else {
            log.info("Server still " + serverBean.name + " exist in database");
        }
        return server
    }

}
