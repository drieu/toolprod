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
        def type = Server.TYPE.APACHE.toString()

        String param = params.get(NAME_PARAM)
        String port = params.get(PORT_PARAM)
        if (port == null || port.isEmpty()) {
            port = Server.getDEFAULT_PORT()
        }

        if (param != null) {
            selectServer = Server.findByNameAndPortNumber(param, port.toInteger());
            log.info("WebServerController:apache() linkapps :" + selectServer?.linkToApps)

        } else { //use by index.gsp because we search the server by machine name
            param = params.get("machineName")
            if (param != null) {
                selectServer = Server.findByMachineHostNameAndPortNumber(param, port.toInteger());
                log.info("WebServerController:apache() linkapps (machineName case ):" + selectServer?.linkToApps)
            }
        }

        Map<String, List<String>> map = getServers(Server.TYPE.APACHE)

        return [type: type, selectServer: selectServer, map:map]
    }

    /**
     * Get a map with server and list port
     * @param type APACHE/WEBLO
     * @return
     */
    private Map<String, List<String>> getServers(Server.TYPE type) {
        Map<String, List<String>> map = new TreeMap<>()
        String port
        def servers = Server.findAll("from Server as s where s.serverType=:type",[type:type])
        if (servers.isEmpty()) {
            log.info("WebServerController:weblogic() No result return by query !")
        }

        if (type == Server.TYPE.WEBLOGIC) {
            port = Server.DEFAULT_WEBLOGIC_PORT
        } else {
            port =  Server.DEFAULT_PORT
        }


        for(Server s : servers) {
            List<String> lst = map.get(s.name)
            if (lst == null ) {
                lst = new ArrayList<>()
            }

            String portToAdd
            Integer tmpPort = s.portNumber
            if (tmpPort == null) {
                portToAdd = port
            } else {
                portToAdd = s.portNumber.toString()
            }


            if (!lst.contains(portToAdd)) {
                lst.add(portToAdd)
                lst.sort()
                if ((s.name != null) && (!s.name.isEmpty())) {
                    map.put(s.name, lst)
                }
            }
        }
        return map
    }

    /**
     * Show weblogic page.
     * @return
     */
    def weblogic() {
        log.debug("WebServerController:weblogic()")
        def selectServer = null;

        Map<String, List<String>> map = getServers(Server.TYPE.WEBLOGIC)

        def type = Server.TYPE.WEBLOGIC.toString()

        String param = params.get(NAME_PARAM)
        String port = params.get(PORT_PARAM)
        if (port == null || port.isEmpty()) {
            port = Server.getDEFAULT_WEBLOGIC_PORT()
        }
        if (param != null) {
            final Integer p = port.toInteger()
            selectServer = Server.findByNameAndPortNumber(param, p);
            log.debug("WebServerController:weblogic() linkapps :" + selectServer?.linkToApps)

        }
        return [type: type, selectServer: selectServer, map:map]
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

    def getWebServerByMachineName() {
        def name = params.get(NAME_PARAM)
        String type = params.get(TYPE_PARAM)
        String port = params.get(PORT_PARAM)
        if (type == null) {
            type = EMPTY_PARAM;
        }
        type = type.toLowerCase()
        redirect(action:type, params: [machineName : name, port : port])
    }
}
