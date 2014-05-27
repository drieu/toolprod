package toolprod

class WebServerController {

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

    def weblogic() {
        log.info("weblogic of WebController")
        def servers = Server.findAll("from Server as s where s.serverType=:type",[type:Server.TYPE.WEBLOGIC])
        if (servers.isEmpty()) {
            log.info("No result return by query !")
        }
        def type = Server.TYPE.WEBLOGIC.toString()
        return [servers: servers, type: type]
    }

    /**
     * Get the server name passed in parameter and redirect to apache page.
     * @return server name.
     */
    def getWebServer() {
        def name = params.get("name")
        redirect(action:"apache", params: [name : name])
    }
}
