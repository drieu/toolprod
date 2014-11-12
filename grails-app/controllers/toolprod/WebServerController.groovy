package toolprod

/**
 * Controller for Apache and Weblogic.
 */
class WebServerController {

    /**
     * Parameter name in request.
     */
    public static final String NAME_PARAM = "name";

    /**
     * Parameter port in request.
     */
    public static final String PORT_PARAM = "port";

    /**
     * Parameter type in request.
     */
    public static final String TYPE_PARAM = "type";

    /**
     * Use to set empty parameter.
     */
    public static final String EMPTY_PARAM = "";

    /**
     * Show apache web page.
     * @return
     */
    def apache() {
        log.debug("WebServerController:apache()")
        def selectServer = null
        def servers = Server.findAll("from Server as s where s.serverType=:type",[type:Server.TYPE.APACHE])
        if (servers.isEmpty()) {
            log.info("WebServerController:apache() No result return by query !")
        }
        def type = Server.TYPE.APACHE.toString()

        String param = params.get(NAME_PARAM)
        String port = params.get(PORT_PARAM)
        if (param != null) {
            selectServer = Server.findByNameAndPortNumber(param, port.toInteger());
            log.info("WebServerController:apache() linkapps :" + selectServer.linkToApps)

        }

        Map<String, List<String>> map = new HashMap<>()
        for(Server s : servers) {
            List<String> lst = map.get(s.name)
            if (lst == null ) {
                lst = new ArrayList<>()
            }
            lst.add(s.portNumber)
            map.put(s.name, lst)
        }

        return [servers: servers, type: type, selectServer: selectServer, map:map]
    }

    /**
     * Show weblogic page.
     * @return
     */
    def weblogic() {
        log.debug("WebServerController:weblogic()")
        def selectServer = null;
        def servers = Server.findAll("from Server as s where s.serverType=:type",[type:Server.TYPE.WEBLOGIC])
        if (servers.isEmpty()) {
            log.info("WebServerController:weblogic() No result return by query !")
        }
        Map<String, List<String>> map = new HashMap<>()
        for(Server s : servers) {
            List<String> lst = map.get(s.name)
            if (lst == null ) {
                lst = new ArrayList<>()
            }
            lst.add(s.portNumber)
            map.put(s.name, lst)
        }


        def type = Server.TYPE.WEBLOGIC.toString()

        String param = params.get(NAME_PARAM)
        String port = params.get(PORT_PARAM)

        if (param != null) {
            selectServer = Server.findByNameAndPortNumber(param, port);
            log.debug("WebServerController:weblogic() linkapps :" + selectServer.linkToApps)

        }
        return [servers: servers, type: type, selectServer: selectServer, map:map]
    }

    /**
     * Get the selected weblogic name.
     */
    def getWeblogicServer() {
        def name = params.get(NAME_PARAM)
        redirect(action:"weblogic", params: [name : name])
    }

    /**
     * Get the Apache server name passed in parameter and redirect to apache page.
     * @return server name.
     */
    def getWebServer() {
        def name = params.get(NAME_PARAM)
        String type = params.get(TYPE_PARAM)
        String port = params.get(PORT_PARAM)
        if (type == null) {
            type = EMPTY_PARAM;
        }
        type = type.toLowerCase()
        redirect(action:type, params: [name : name, port : port])
    }
}
