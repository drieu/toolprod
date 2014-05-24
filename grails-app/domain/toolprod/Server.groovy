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


    Server parent

    Server child

    List<String> linkToApps = []

    TYPE serverType

    enum TYPE {
        APACHE, WEBLOGIC
    }


    static constraints = {
        name()
        portNumber(defaultValue:80)
    }

    @Override
    public java.lang.String toString() {
        return "Server{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", portNumber=" + portNumber +
                ", parent=" + parent +
                ", child=" + child +
                ", linkToApps=" + linkToApps +
                ", serverType=" + serverType +
                ", version=" + version +
                '}';
    }

    def addToLinkApps(String appName) {
        if (linkToApps == null) {
            linkToApps = new ArrayList<String>();
        }
        linkToApps.add(appName)
    }


}
