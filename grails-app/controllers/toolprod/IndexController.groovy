package toolprod

class IndexController {

    def index () {
        println ("MainController : index()")

        def machines = Machine.findAll();
        def apps
        def machine
        def machineServers

        println("machine:" + params.get("machine"))
        String param = params.get("machine")
        if (param != null) {
            machine = Machine.findByName(param);
            apps = machine.apps
            machineServers = machine.servers
        }

        List<App> results = new ArrayList<>()
        param = params.get("query")
        if (param != null) {
            results = App.withCriteria {
                or {
                    ilike('name', "%" + param + "%")
                    ilike('description', "%" + param + "%")
                }
            }
        }

        return [ apps: apps, machines: machines, machine:machine, machineServers:machineServers, searchResults: results]

    }

    def getMachineApps() {
        def selectMachine = params.get("machine")
        redirect(action:"index", params: [machine : selectMachine])
    }

    def search() {
        def query = params.get("query")
        println("search query :" + query)

        redirect(action:"index", params: [query : query])
    }

// TODO : by server type
//    def getServersByType() {
//        def type = params.get("type")
//        List<Server> servers = Server.findByServerType(type)
//    }
}
