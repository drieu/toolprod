package toolprod

/**
 * Main controller.
 */
class IndexController {

    /**
     * Parameter machine in request.
     */
    public static final String MACHINE_PARAM = "machine";

    /**
     * Parameter query in request.
     */
    public static final String QUERY_PARAM = "query";

    /**
     * Index for the main page.
     */
    def index () {
        log.info ("IndexController:index()")

        def machines = Machine.findAll("from Machine as m order by m.name");
        def apps = new HashSet()
        def machine
        def machineServers

        log.debug("IndexController:index() Parameter machine passed in request:" + params.get("machine"))
        String param = params.get(MACHINE_PARAM)
        if (param != null) {
            machine = Machine.findByName(param);
            if ( machine != null) {
                apps = machine.apps
                apps = apps.sort{it.name}
                machineServers = machine.servers
                log.info("IndexController:index() Machine:" + machine.toString())
            }
        }

        List<App> results = null
        param = params.get(QUERY_PARAM)
        if (param != null) {
            results = new ArrayList<>();
            results = App.withCriteria {
                or {
                    ilike('name', "%" + param + "%")
                    ilike('description', "%" + param + "%")
                }
                order("name", "asc")
            }
        }

        return [ apps: apps, machines: machines, machine:machine, machineServers:machineServers, searchResults: results]

    }

    /**
     * Get machine name and redirect to index page.
     **/
    def getMachineApps() {
        def selectMachine = params.get(MACHINE_PARAM)
        redirect(action:"index", params: [machine : selectMachine])
    }

    /**
     * Get search request parameter and redirect to the index controller.
     */
    def search() {
        def query = params.get(QUERY_PARAM)
        log.debug("IndexController:search() query:" + query)
        redirect(action:"index", params: [query : query])
    }

}
