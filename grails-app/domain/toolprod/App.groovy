package toolprod

import fr.edu.toolprod.bean.AppBean
import org.apache.commons.logging.LogFactory

/**
 * Application class.
 */
class App {

    static searchable = true

    /**
     * Application name.
     */
    String name

    /**
     * Application description.
     */
    String description

    /**
     * url
     * This parameter is construct ( see AppBean class for detail ).
     */
    List<String> urls = []

    /**
     * Portail in which app will be shown.
     */
    List<Portal> portals = []


    /**
     * Server list.
     */
    List<Server> servers = []
    static hasMany = [servers : Server, portals : Portal, urls : String]

    private static final log = LogFactory.getLog(this)

    private static final String  EMPTY = "";

    static mapping = {
        sort "name":"asc";
    }

    static constraints = {
        name(blank:false)
        description(size:0..400)
    }

    def addServer(Server server) {

        if (server == null) {
            throw new IllegalArgumentException("Can't add server to servers list of App because server is null")
        }
        if (servers == null) {
            servers = new ArrayList<Server>()
        }

        if (!servers.contains(server)) {
            log.debug("Add server to the server list.")
            servers.add(server)
        } else {
            log.debug("Server still in server list.")
        }

    }

    @Override
    public String toString() {
        return "App{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", urls='" + urls.toString() + '\'' +
                ", portals=" + portals +
                ", servers=" + servers +
                ", version=" + version +
                '}';
    }
}
