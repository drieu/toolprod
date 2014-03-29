package toolprod

class IndexController {

    def index () {
        println ("MainController : index()")

        Server.TYPE type = Server.TYPE.WEBLOGIC
        def s = Server.findAllByServerType(type)

        println("app:" + params.get("app"))
        def server = Server.findByName(params.get("app"))
        return [ myType:type, servers:s, server:server  ]

    }

    def getServer() {
        //TODO retreive param app
        redirect(action:"index", params: [app : "app1"])
    }
}
