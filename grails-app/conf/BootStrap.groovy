import grails.util.GrailsUtil
import toolprod.App
import toolprod.Machine
import toolprod.Server
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->
        if (Environment.current == Environment.DEVELOPMENT) {
            log.info("ENVIRONNEMENT : " + Environment.DEVELOPMENT)
        } else if (Environment.current == Environment.TEST){
            log.info("ENVIRONNEMENT : " +  Environment.TEST)
        } else if (Environment.current == Environment.PRODUCTION){
            log.info("ENVIRONNEMENT : " +  Environment.PRODUCTION)
        } else {
            log.error("ENVIRONNEMENT : " +  Environment.PRODUCTION)
            return
        }
        log.info("Nothing to load at startup ...")
    }

    def destroy = {
    }
}
