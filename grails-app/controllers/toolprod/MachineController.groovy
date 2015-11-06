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

    /**
     * Show machine list for a group with apps, web servers ...
     * @return
     */
    def group() {
        log.info("groupe()")
        def apps = new HashSet()
        def localApps = new HashSet()
        def machine
        def machineServers
        List<String> refs = new ArrayList<>()
        List<App> results = null

        String param = params.get(MACHINE_PARAM)

        if (param != null) {
            machine = Machine.findByName(param);
            if ( machine != null) {
                apps = machine.apps
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
        List<Machine> machines = new ArrayList<>()

        def selectedMachineGroup = params.get("group")
        if (selectedMachineGroup == null ) {
            selectedMachineGroup = "ALL"
        }
        if (selectedMachineGroup == "" ) {
            selectedMachineGroup = "ALL"
        }

        if (selectedMachineGroup == "ALL" ) {
            machines = Machine.findAll()
        } else {
            def machineGroup = MachineGroup.findByGroupName(selectedMachineGroup)
            if (machineGroup != null) {
                for (Machine m : Machine.findAll()){
                    String mName = m.name
                    for(String str : machineGroup.regex) {
                        if (mName.startsWith(str)) {
                            machines.add(m)
                        }
                    }
                }
            } else {
                log.warn("group() unknow machine group name")
            }
        }
        return [ machines: machines, machine:machine, machineServers:machineServers, refs: refs, selectedMachineGroup:selectedMachineGroup]
    }


    /**
     * Get machine name and redirect to index page.
     **/
    def getMachineApps() {
        def selectMachine = params.get(MACHINE_PARAM)
        def selectedGroup = params.get('group')
        redirect(action:"group", params: [machine : selectMachine, group : selectedGroup])
    }

}
