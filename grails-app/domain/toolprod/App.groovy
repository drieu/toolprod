package toolprod

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
     * url.
     * TODO : list of urls ?
     */
    String url

    List<Server> servers = []

    static hasMany = [servers : Server]


    static constraints = {
        name(blank:false)
        description(size:0..400)
        url(size:0..350, url:true)
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

        log.info("Add server to servers list.")
        servers.add(server)

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
