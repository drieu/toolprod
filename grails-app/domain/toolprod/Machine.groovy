package toolprod

import fr.edu.toolprod.bean.AppBean

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

    /**
     * Application list in this machine.
     */
    Set<App> apps = []

    /**
     * Servers list.
     */
    Set<Server> servers = []


    static hasMany = [apps : App, servers : Server]

    static mapping = {
        sort "name"
    }

    static constraints = {
        name()
        ipAddress(nullable: true)
    }

    /**
     * Search if an AppBean exists in database and add it to the list of application.
     * If it does not exist we creae it with appBean.name and appBean.description.
     * @param appBean
     */
    def addAppBean(AppBean appBean) {
        App app = App.findOrCreateByNameAndDescription(appBean.name, appBean.description)
        apps.add(app)
        this.save(failOnError: true, flush:true)
    }

    /**
     * Add an existing application to list of  machine app 's list.
     * @param app
     */
    def addApplication(App app) {
        if (app == null) {
            throw new IllegalArgumentException("Can't add a null application to Machine apps list !")
        }
        apps.add(app)
        this.save(failOnError: true, flush:true)
    }

    /**
     * Add a server to the list of servers for this machine.
     * @param server
     * @return
     */
    def addServer(Server server) {
        if (server == null) {
            throw new IllegalArgumentException("Can't add a null server to Machine servers list !")
        }
        if( name != null ) {
            servers.add(server)
        }

    }


    @Override
    public String toString() {
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
