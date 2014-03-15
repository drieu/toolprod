package toolprod

class IndexController {

    def index () {
        println ("MainController : index()")
        [lst : Server.findAll()]
    }
}
