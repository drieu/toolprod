import toolprod.App
import toolprod.Machine
import toolprod.Server

class BootStrap {

    def init = { servletContext ->

        log.info("Nothing to load at startup ...")
    }

    def destroy = {
    }
}
