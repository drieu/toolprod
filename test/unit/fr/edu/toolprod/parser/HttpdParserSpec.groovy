package fr.edu.toolprod.parser

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class HttpdParserSpec extends Specification {


    def setup() {
    }

    def cleanup() {
    }

    void testExtractServerFromHttpProxyPass() {

        HttpdParser parser = new HttpdParser()
        assertEquals("", parser.extractPortFromHttpProxyPass(null));


    }
}
