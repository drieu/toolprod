package toolprod

class IndexController {

    def index () {
        log.info ("MainController : index()")

        def machines = Machine.findAll("from Machine as m order by m.name");
        def apps = new HashSet()
        def machine
        def machineServers

        log.debug("Parameter machine passed in request:" + params.get("machine"))
        String param = params.get("machine")
        if (param != null) {

            machine = Machine.findByName(param);
            if ( machine != null) {
                apps = machine.apps
                apps = apps.sort{it.name}
                machineServers = machine.servers
                log.info("Machine:" + machine.toString())
            }
        }

        List<App> results = null
        param = params.get("query")
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
     *
     **/
    def getMachineApps() {
        def selectMachine = params.get("machine")
        redirect(action:"index", params: [machine : selectMachine])
    }

    def search() {
        def query = params.get("query")
        log.debug("search query :" + query)
        redirect(action:"index", params: [query : query])
    }

}
