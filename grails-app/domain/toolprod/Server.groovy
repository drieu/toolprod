package toolprod

/**
 * Servers like Apache, Weblogic
 */
class Server {

    /**
     * Server name.
     */
    String name

    /**
     * Port Number.
     */
    Integer portNumber

    TYPE serverType

    enum TYPE {
        APACHE, WEBLOGIC
    }

    static hasMany = [apps:App]

    static constraints = {
        name()
        portNumber()
    }

    String toString() {
        return "${name}"
    }
}
