package toolprod

class WebServerController {

    /**
     * Show apache web page.
     * @return
     */
    def apache() {
        log.info("apache of WebController")
        def selectServer = null
        def servers = Server.findAll("from Server as s where s.serverType=:type",[type:Server.TYPE.APACHE])
        if (servers.isEmpty()) {
            log.info("No result return by query !")
        }
        def type = Server.TYPE.APACHE.toString()

        String param = params.get("name")
        if (param != null) {
            selectServer = Server.findByName(param);
            log.info("linkapps :" + selectServer.linkToApps)

        }
        return [servers: servers, type: type, selectServer: selectServer]
    }

    /**
     * Show weblogic page.
     * @return
     */
    def weblogic() {
        log.info("weblogic of WebController")
        def selectServer = null
        def servers = Server.findAll("from Server as s where s.serverType=:type",[type:Server.TYPE.WEBLOGIC])
        if (servers.isEmpty()) {
            log.info("No result return by query !")
        }
        def type = Server.TYPE.WEBLOGIC.toString()

        String param = params.get("name")
        if (param != null) {
            selectServer = Server.findByName(param);
            log.info("linkapps :" + selectServer.linkToApps)

        }
        return [servers: servers, type: type, selectServer:selectServer]
    }

    /**
     * Get the selected weblogic name.
     * @return
     */
    def getWeblogicServer() {
        def name = params.get("name")
        redirect(action:"weblogic", params: [name : name])
    }

    /**
     * Get the Apache server name passed in parameter and redirect to apache page.
     * @return server name.
     */
    def getWebServer() {
        def name = params.get("name")
        redirect(action:"apache", params: [name : name])
    }
}
