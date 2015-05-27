package fr.edu.toolprod.bean

import grails.validation.Validateable

/**
 * AppBean class.
 */
@Validateable
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
     * url of web Server which launch the application.
     */
    String serverUrl = ""


    /**
     * port of web Server which launch the application.
     */
    String serverPort = ""

    /**
     * Weblogic list.
     */
    List<String> weblos = new ArrayList<>()

    /**
     * Server location for this application.
     * In case of proxypass we know the value of server.
     */
    String appServer

    /**
     * Server location for this application.
     * In case of proxypass we know the value of port.
     */
    String appPort


    private static final String DEFAUL_APPNAME = "UNEUSED_APP"

    private static final String DEFAUL_DESCRIPTION = "EMPTY"

    public static final String DEFAUL_SERVER_URL = "http://notdefined.com"

    private static final String DEFAUL_SERVER_PORT = "http://notdefined.com"

    private static final String DEFAULT_PORT = "80"

    private static final String DEFAULT_PROTOCOL = "http://"

    private static final String COLON = ":"

    private static final String SLASH = "/"

    /**
     * Constructor.
     */
    AppBean() {
        this.name = DEFAUL_APPNAME
        this.description = DEFAUL_DESCRIPTION
        this.serverUrl = DEFAUL_SERVER_URL
        this.serverPort = DEFAUL_SERVER_PORT
    }

    /**
     * Set an url for this application.
     * This url is built automaticaly with given parameters or set with DEFAUL_SERVER_URL.
     * @param hostname
     * @param port
     * @param app
     */
    public def setUrl(String hostname, String port, String app) {
        String url = DEFAUL_SERVER_URL
        if ((hostname != null) && (!hostname.isEmpty()) && (app != null) && (!app.isEmpty())) {
            if ((port != null)  && (!port.isEmpty())) {
                url = DEFAULT_PROTOCOL + hostname + COLON + port + SLASH + app
            } else {
                url = DEFAULT_PROTOCOL + hostname + SLASH + app
            }
        }
        this.serverUrl = url
    }

    /**
     * Set an url for this application.
     * Add shortUrl to this url ( exemple http://..../racvision )
     * This url is built automaticaly with given parameters or set with DEFAUL_SERVER_URL.
     * @param hostname
     * @param port
     * @param app
     * @param shortUrl ( e.g : racvision )
     */
    public def setUrl(String hostname, String port, String app, String shortUrl) {
        String url = DEFAUL_SERVER_URL
        if ((hostname != null) && (!hostname.isEmpty()) && (app != null) && (!app.isEmpty())) {
            if ((port != null)  && (!port.isEmpty())) {
                url = DEFAULT_PROTOCOL + hostname + COLON + port + SLASH + app
            } else {
                url = DEFAULT_PROTOCOL + hostname + SLASH + app
            }
            if ((shortUrl != null) && (!shortUrl.isEmpty())) {
                url = url + SLASH + shortUrl
            }
        }
        this.serverUrl = url
    }

    /**
     * Set an url for this application.
     * Add shortUrl from the list to this url ( exemple http://..../racvision ).
     * This url is built automaticaly with given parameters or set with DEFAUL_SERVER_URL.
     * @param hostname
     * @param port
     * @param app
     * @param shortUrl ( e.g : racvision )
     */
    public def setUrls(String hostname, String port, String app,List<String> shortUrls) {
        if(shortUrls != null) {
            for(String shortUrl : shortUrls) {
                setUrl(hostname, port, app, shortUrl)
            }
        }
    }

    /**
     * Getter.
     * @return
     */
    String getAppPort() {
        return appPort
    }

    /**
     * Set app port and if not set a DEFAULT_APACHE_PORT
     * @param appPort
     */
    void setAppPort(String port) {
        this.appPort = port
        if (port == null) {
            appPort = DEFAULT_PORT
        } else if (appPort.isEmpty()) {
            appPort = DEFAULT_PORT
        }
    }


    @Override
    public String toString() {
        return "AppBean{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", serverPort='" + serverPort + '\'' +
                ", weblos=" + weblos +
                ", appServer='" + appServer + '\'' +
                ", appPort='" + appPort + '\'' +
                '}';
    }
}
