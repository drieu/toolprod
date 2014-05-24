package toolprod

class WebServerController {

    def apache() {
        log.info("apache of WebController")
        def servers = Server.findAll("from Server as s where s.serverType=:type",[type:Server.TYPE.APACHE])
        if (servers.isEmpty()) {
            log.info("No result return by query !")
        }
        def type = Server.TYPE.APACHE.toString()
        return [servers: servers, type: type]
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
}
