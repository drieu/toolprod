package toolprod

class IndexController {

    def index () {
        println ("MainController : index()")
        def m = Machine.findAll()
        def s = Server.findAll()
        def server = Server.findByName("app1")
        return [ machines: m, servers:s, server:server  ]

    }

    def getServer() {
        [ server : Server.findByName("web1") ]
    }
}
