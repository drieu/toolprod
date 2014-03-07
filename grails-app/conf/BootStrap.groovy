import toolprod.App
import toolprod.Server

class BootStrap {

    def init = { servletContext ->
        App tsApp = new App(name: "ts", description: "Teleservices", url: "http://teleservices.ac-limoges.fr/ts" )
        if (!tsApp.save()) {
            log.error("Bootstrap : Can't save ts App")
        } else {
            log.info("Bootstrap : Save ts App OK.")
        }
        Server serv = new Server(name: "appliloc5.ac-limoges.fr", ipAddress: "127.0.0.1")
        serv.addToApps(tsApp)
        if (!serv.save()) {
            log.error("Bootstrap : Can't save tsApp server")
        } else {
            log.info("Bootstrap : Save server OK.")
        }
        Server serv1 = new Server(name: "appliloc6.ac-limoges.fr", ipAddress: "127.0.0.1")
        if (!serv1.save()) {
            log.error("Bootstrap : Can't save server1")
        } else {
            log.info("Bootstrap : Save server OK.")
        }



    }
    def destroy = {
    }
}
