package toolprod

/**
 * Main controller.
 */
class IndexController {

    /**
     * Parameter query in request.
     */
    public static final String QUERY_PARAM = "query";

    /**
     * Constant.
     */
    private static final String EMPTY = ""

    /**
     * Index for the main page.
     */
    def index () {
        log.info ("IndexController:index()")

        def data = "\nvar dataSet = [\n"
        for(App p : App.findAll()) {
            String link = "<a href=/toolprod/appRetail/app?name=" + p.name + ">" + p.name + "</a>"

            String vips = ""
            for( String portal : p.vips) {
                Vip vip = Vip.findByTechnicalName(portal)
                if (vip != null) {
                    String vipname = vip?.name + "_" + vip?.type
                    if (vipname == null) {
                        vipname = EMPTY
                    }
                    vips += vipname
                    vips += " "
                }
            }
            for(Server serv:p.servers) {
                if ( serv != null) {
                    data += "['" + link + "','" + vips + "','" + serv?.machineHostName + "','" + serv?.portNumber + "'],"
                } else {
                    data += "['" + link + "','" + vips + "','',''],"

                }
            }
        }
        data += "\n];"
        log.debug(data)
        def count = App.findAll().size()
        return [count:count, data:data]
    }

}
