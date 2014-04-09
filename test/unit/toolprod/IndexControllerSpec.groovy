package toolprod

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(IndexController)
class IndexControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }


    void "test empty getMachineApps"() {
        when:
        controller.getMachineApps()

        then:
        response.redirectedUrl == '/?machine='
    }

    void "test empty search"() {
        when:
        controller.search()

        then:
        response.redirectedUrl == '/?query='
    }
}
