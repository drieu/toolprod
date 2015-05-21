package fr.edu.toolprod.bean

import grails.validation.Validateable

/**
 * Bean to store temporaly state of a Server.
 */
@Validateable
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

    /**
     * Constructor.
     */
    ServerBean() {
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
    public String toString() {
        return "ServerBean{" +
                "name='" + name + '\'' +
                ", portNumber='" + portNumber + '\'' +
                ", machineHostName='" + machineHostName + '\'' +
                ", modules=" + modules +
                '}';
    }
}
