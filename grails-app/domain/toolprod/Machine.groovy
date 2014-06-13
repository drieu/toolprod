package toolprod

/**
 * List all application list in apache configuration
 */
class Machine {

    /**
     * Name of the web server.
     */
    String name

    /**
     * IP address.
     */
    String ipAddress

    Set<App> apps = []
    Set<Server> servers = []
    static hasMany = [apps : App, servers : Server]



    static constraints = {
        name()
        ipAddress()
    }

    def addApplication(App app) {
        if (app == null) {
            throw new IllegalArgumentException("Can't add a null server to Machine apps list !")
        }
        apps.add(app)
        this.save()
    }

    def addServer(Server server) {
        if (server == null) {
            throw new IllegalArgumentException("Can't add a null server to Machine servers list !")
        }
        if(name != null) {
            servers.add(server)
        }

    }


    @Override
    public java.lang.String toString() {
        return "Machine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", apps=" + apps +
                ", servers=" + servers +
                ", version=" + version +
                '}';
    }
}
