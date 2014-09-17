package fr.edu.toolprod.parser


import grails.test.GrailsUnitTestCase
/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */

class HttpdParserSpec extends GrailsUnitTestCase {

    void testParseLocationHostAndPort() {
        List<String> results = HttpdParser.parseLocationHostAndPort(null)
        assertNotNull(results)
        assertEquals(0, results.size())
    }

}
