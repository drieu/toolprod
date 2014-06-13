package toolprod

import fr.edu.toolprod.bean.AppBean

/**
 * Application class.
 * TODO : CT
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

        if (!servers.contains(server)) {
            log.debug("Add server to the server list.")
            servers.add(server)
        } else {
            log.debug("Server still in server list.")
        }

    }

    public static saveApp(AppBean appBean) {
        App lineApp = App.findByName{name==appBean.name}
        if ( lineApp == null ) {
            //TODO : description
            App app = new App(name: appBean.name, description: appBean.description )
            app.save();
        }
        return lineApp;
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
