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
     * Type de machine
     */
    public static final String MACHINE_TYPE_PARAM = "type_machine";


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
        def localApps = new HashSet()
        def machine
        def machineServers
        List<String> refs = new ArrayList<>()

        log.debug("IndexController:index() Parameter machine passed in request:" + params.get("machine"))
        String param = params.get(MACHINE_PARAM)
        if (param != null) {
            machine = Machine.findByName(param);
            if ( machine != null) {
                apps = machine.apps
                for(App app : apps) {
                    if( (app?.servers?.size() == 1) && (app?.url?.contains(machine?.name))) {
                        localApps.add(app)
                    }
                }
                apps = apps.sort{it.name}
                machineServers = machine?.servers?.sort {it.portNumber }
                log.info("IndexController:index() Machine:" + machine.toString())
                for(Server server: machineServers) {
                    for(String appRef:server?.linkToApps) {
                        if (!refs.contains(appRef)) {
                            refs.add(appRef)
                        }
                    }

                }
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

        def machineGroups = MachineGroup.findAll()
        def selectedMachineGroup = params.get("group")
        if (selectedMachineGroup == null) {
            selectedMachineGroup = ""
        }

        return [ apps: localApps, machines: machines, machine:machine, machineServers:machineServers, searchResults: results, refs: refs, machineGroups:machineGroups, selectedMachineGroup:selectedMachineGroup]

    }

    /**
     * Get machine name and redirect to index page.
     **/
    def getMachineApps() {
        def selectMachine = params.get(MACHINE_PARAM)
        def selectedGroup = params.get('group')
        redirect(action:"index", params: [machine : selectMachine, group : selectedGroup])
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
