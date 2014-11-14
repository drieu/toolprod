package toolprod

import fr.edu.toolprod.bean.ServerBean
import org.apache.commons.logging.LogFactory

/**
 * Servers like Apache, Weblogic
 */
class Server {

    /**
     * ServerName in apache conf.
     */
    String name

    /**
     * Listen in apache conf.
     */
    Integer portNumber

    /**
     * Hostname.
     */
    String machineHostName


    Server parent

    Server child

    /**
     * List references application.
     */
    List<String> linkToApps = []

    /**
     * List Apache modules.
     */
    List<String> modules = []
    static hasMany = [ linkToApps: String, modules: String]

    TYPE serverType

    enum TYPE {
        APACHE, WEBLOGIC
    }

    private static final log = LogFactory.getLog(this)

    static constraints = {
        name()
        portNumber(defaultValue:80)
        machineHostName(defaultValue:"127.0.0.1")
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", portNumber=" + portNumber +
                ", parent=" + parent +
                ", child=" + child +
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
        log.info("Save serverBean:" + serverBean)
        Server server = Server.find{name==serverBean.name; portNumber==serverBean.portNumber}
        if (server == null) {
            server = new Server(name: serverBean.name,machineHostName: serverBean.machineHostName, portNumber: serverBean.portNumber.toInteger(), serverType: Server.TYPE.APACHE)
            server.modules = serverBean.modules;
            server.save();

        } else {
            log.info("Server still " + serverBean.name + " exist in database");
        }
        return server
    }
}
