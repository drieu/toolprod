package toolprod

import org.apache.commons.logging.LogFactory

/**
 * Application class.
 */
class App {

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
     * Save ARENA path
     */
    String arenaPath

    /**
     * TreeNode to store servers tree.
     */
    TreeNode node

    /**
     * Server list.
     */
    List<Server> servers = []
    static hasMany = [servers : Server, portals : Portal, urls : String]

    /**
     * Logger.
     */
    private static final log = LogFactory.getLog(this)


    static mapping = {
        sort "name":"asc";
    }

    static constraints = {
        name(blank:false)
        description(size:0..400)
        node(nullable: true)
        arenaPath(nullable: true, size:0..200)
    }

    /**
     * Add a server to List<Server>.
     * @throws IllegalArgumentException
     * @param server
     */
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
    public java.lang.String toString() {
        return "App{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", urls=" + urls +
                ", portals=" + portals +
                ", node=" + node +
                ", servers=" + servers +
                ", version=" + version +
                '}';
    }
}
