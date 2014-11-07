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
     * Application list.
     */
    Set<App> apps = []

    /**
     * Servers list.
     */
    Set<Server> servers = []


    static hasMany = [apps : App, servers : Server]



    static constraints = {
        name()
        ipAddress(nullable: true)
    }

    def addAppBean(AppBean appBean) {
        App app = App.findOrCreateByNameAndDescription(appBean.name, appBean.description)
        apps.add(app)
        this.save()
    }


    def addApplication(App app) {
        if (app == null) {
            throw new IllegalArgumentException("Can't add a null application to Machine apps list !")
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
