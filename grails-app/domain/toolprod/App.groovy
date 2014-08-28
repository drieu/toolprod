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
     * url.
     * TODO : list of urls ?
     */
    String url

    List<Server> servers = []

    static hasMany = [servers : Server]

    private static final log = LogFactory.getLog(this)

    private static final String  EMPTY = "";

    static mapping = {
        sort "name":"asc";
    }

    static constraints = {
        name(blank:false)
        description(size:0..400)
        // URL set to false because of special URL in Apache configuration e.g: http://web3.ac-limoges.fr/inscrinetcl/([Ee]tab|[Ii]nscription
        url(size:0..350, url:false)
    }


    def addServer(String serverName) {
        Server server = Server.findByName(serverName)
        if (server == null) {
            server = new Server(serverName)
        }
        server.save()
        if (servers == null) {
            servers = new HashSet<>(Server)
        }
        servers.add(server)
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
    public java.lang.String toString() {
        return "App{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", servers=" + servers +
                ", version=" + version +
                '}';
    }



}
