package toolprod

class IndexController {

    def index () {
        println ("MainController : index()")

        def machines = Machine.findAll();
        def apps
        def machine

        println("machine:" + params.get("machine"))
        String param = params.get("machine")
        println(param)
        if (param != null) {
            machine = Machine.findByName(param);
            apps = machine.apps
        }

        return [ apps: apps, machines: machines, machine:machine ]


    }

    def getMachineApps() {
        def selectMachine = params.get("machine")
        redirect(action:"index", params: [machine : selectMachine])
    }
}
