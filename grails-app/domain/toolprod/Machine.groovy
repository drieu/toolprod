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


    static hasMany = [servers : Server]


    static constraints = {
        name()
        ipAddress()
    }
}
