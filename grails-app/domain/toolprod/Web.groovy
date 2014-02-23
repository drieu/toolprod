package toolprod

/**
 * List all application list in apache configuration
 */
class Web {

    /**
     * Name of the web server.
     */
    String name

    /**
     * IP address.
     */
    String ipAddress

    /**
     * Port Number.
     */
    Integer portNumber


    static constraints = {
        name()
        ipAddress()
        portNumber()
    }
}
