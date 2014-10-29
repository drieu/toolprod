package fr.edu.toolprod.bean

/**
 * AppBean class.
 */
class AppBean {

    /**
     * Application name.
     */
    String name = ""

    /**
     * Application description.
     */
    String description = ""

    /**
     * url of Server which launch the application.
     */
    String serverUrl = ""

    /**
     * port of Server which launch the application.
     */
    String serverPort = ""

    /**
     * Portal list.
     */
    List<String> portals = new ArrayList<>()

    /**
     * Weblogic list.
     */
    List<String> weblos = new ArrayList<>()


    private static final String DEFAUL_APPNAME = "UNEUSED_APP"

    private static final String DEFAUL_DESCRIPTION = "EMPTY"

    private static final String DEFAUL_SERVER_URL = "http://notdefined.com"

    private static final String DEFAUL_SERVER_PORT = "http://notdefined.com"


    AppBean() {
        this.name = DEFAUL_APPNAME
        this.description = DEFAUL_DESCRIPTION
        this.serverUrl = DEFAUL_SERVER_URL
        this.serverPort = DEFAUL_SERVER_PORT
    }

    public boolean isDefaulApp() {
        if (description.equals(DEFAUL_DESCRIPTION) && serverUrl.equals(DEFAUL_SERVER_URL) &&  description.equals(DEFAUL_DESCRIPTION)) {
            return true;
        }
        return false;
    }

    public def setUrl(String hostname, String port, String app) {
        String url = DEFAUL_SERVER_URL
        if ((hostname != null) && (!hostname.isEmpty()) && (app != null) && (!app.isEmpty())) {
            if ((port != null)  && (!port.isEmpty())) {
                url = "http://" + hostname + ":" + port + "/" + app
            } else {
                url = "http://" + hostname + "/" + app
            }
        }
        this.serverUrl = url
    }


    @Override
    public String toString() {
        return "AppBean{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", serverPort='" + serverPort + '\'' +
                ", portals=" + portals +
                '}';
    }
}
