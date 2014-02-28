import toolprod.App

class BootStrap {

    def init = { servletContext ->
        App tsApp = new App(name: "ts", description: "Teleservices", url: "http://teleservices.ac-limoges.fr/ts" )
        if (!tsApp.save()) {
            log.error("Bootstrap : Can't save ts App")
        } else {
            log.info("Bootstrap : Save ts App OK.")
        }

    }
    def destroy = {
    }
}
