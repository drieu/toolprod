package toolprod

/**
 * Servers.
 */
class Server {

    /**
     * Server name.
     */
    String name

    /**
     * IP adress
     */
    String ipAddress

    static constraints = {
        name()
        ipAddress()
    }
}
