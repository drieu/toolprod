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
        println(param)
        if (param != null) {
            machine = Machine.findByName(param);
            apps = machine.apps
            machineServers = machine.servers
        }

        return [ apps: apps, machines: machines, machine:machine, machineServers:machineServers ]

    }

    def getMachineApps() {
        def selectMachine = params.get("machine")
        redirect(action:"index", params: [machine : selectMachine])
    }



// TODO : by server type
//    def getServersByType() {
//        def type = params.get("type")
//        List<Server> servers = Server.findByServerType(type)
//    }
}
