import grails.util.Environment
import org.codehaus.groovy.grails.commons.GrailsApplication

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
