package fr.edu.toolprod.bean

/**
 * Bean to store temporaly state of a Server.
 * User: drieu
 * Date: 13/06/14
 * Time: 16:43
 */
class ServerBean {

    /**
     * Server name.
     */
    String name

    /**
     * Port Number.
     */
    String portNumber

    /**
     * Hostname.
     */
    String machineHostName

    /**
     * Modules Apache list.
     */
    List<String> modules = []

    ServerBean() {
    }

    ServerBean(String name, String portNumber, String machineHostName) {
        this.name = name
        this.portNumber = portNumber
        this.machineHostName = machineHostName
    }
/**
     * Add a module to the modules list.
     * @param moduleName  Name of the module.
     * @return
     */
    def addToModules(String moduleName) {
       modules.add(moduleName)
    }


    @Override
    public java.lang.String toString() {
        return "ServerBean{" +
                "name='" + name + '\'' +
                ", portNumber=" + portNumber +
                ", modules=" + modules +
                '}';
    }
}
