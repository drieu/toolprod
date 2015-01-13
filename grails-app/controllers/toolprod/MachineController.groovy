package toolprod

class MachineController {

    public static final String MACHINE_PARAM = "machine";

    public static final String QUERY_PARAM = "query";

    def index() {}

    /**
     * Search action.
     * Retun result in search.gsp for a given param
     * @return
     */
    def search() {
        def results
        def param = params.get(QUERY_PARAM)
        log.info("IndexController:search() query:" + param)

        if (param != null) {
            log.info("group() search with criteria name and description in App")
            results = new ArrayList<>();
            results = App.withCriteria {
                or {
                    ilike('name', "%" + param + "%")
                    ilike('description', "%" + param + "%")
                }
                order("name", "asc")
            }
            if (results.isEmpty()) {
                results = null
            }
            log.info("group() search result : " + results)
        }
        return [ searchResults:results ]
    }

    def group() {

        def machines = Machine.findAll("from Machine as m order by m.name");
        def machineGroups = MachineGroup.findAll()
        def apps = new HashSet()
        def localApps = new HashSet()
        def machine
        def machineServers
        List<String> refs = new ArrayList<>()
        List<App> results = null

        log.debug("IndexController:index() Parameter machine passed in request:" + params.get("machine"))
        String param = params.get(MACHINE_PARAM)
        if (param != null) {
            machine = Machine.findByName(param);
            if ( machine != null) {
                apps = machine.apps
                for(App app : apps) {
                    if( (app?.servers?.size() == 1) && (app?.urls?.contains(machine?.name))) {
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

        def selectedMachineGroup = params.get("group")
        if (selectedMachineGroup == null) {
            selectedMachineGroup = ""
        }

        return [ apps: localApps, machines: machines, machine:machine, machineServers:machineServers, refs: refs, machineGroups:machineGroups, selectedMachineGroup:selectedMachineGroup]

    }

    /**
     * Get machine name and redirect to index page.
     **/
    def getMachineApps() {
        def selectMachine = params.get(MACHINE_PARAM)
        def selectedGroup = params.get('group')
        redirect(action:"group", params: [machine : selectMachine, group : selectedGroup])
    }

    def list() {
        List<Machine> machines = Machine.findAll()
        return [machines:machines]
    }
}
