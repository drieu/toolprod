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

    static hasMany = [apps:App]

    static constraints = {
        name()
        ipAddress()
    }

    String toString() {
        return "${name}"
    }
}
