package fr.edu.toolprod.bean

import grails.validation.Validateable
import org.apache.commons.logging.LogFactory

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
     * Logger.
     */
    private static final log = LogFactory.getLog(this)


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
       if (moduleName != null) {
            modules.add(moduleName)
       } else {
           log.warn("addToModules() Apache module name is null.Cannot add to the Apache module list (modules).")
       }
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
