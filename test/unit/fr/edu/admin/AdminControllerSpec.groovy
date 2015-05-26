package fr.edu.admin

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AdminController)
class AdminControllerSpec extends Specification {

    void "test index redirect"() {
        when:
        controller.index()

        then:
        response.redirectedUrl == '/admin/init'
    }

    void "test show initData"() {
        when:
        controller.initData()

        then:
        view == '/admin/initData'
    }

    void "test show initDataBis"() {
        when:
        controller.initData()

        then:
        view == '/admin/initData'
    }



}
