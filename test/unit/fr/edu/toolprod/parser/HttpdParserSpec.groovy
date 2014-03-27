package fr.edu.toolprod.parser

import grails.test.GrailsUnitTestCase
/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
class HttpdParserSpec extends GrailsUnitTestCase {

    void testExtractServerFromHttpProxyPassUsual() {
        HttpdParser parser = new HttpdParser()
        assertEquals("web.fr", parser.extractServerFromHttpProxyPass("http://web.fr:82/myapp"))
        assertEquals("test.org",parser.extractServerFromHttpProxyPass("http://test.org:82/myapp"))
    }

    void testExtractServerFromHttpProxyPassWithBadValues() {
        HttpdParser parser = new HttpdParser()
        assertEquals("When value is null, test should return an empty value.", "", parser.extractServerFromHttpProxyPass(null));
        assertEquals("When value is empty, test should return an empty value.", "", parser.extractServerFromHttpProxyPass(""));
        assertEquals("When value is ???, test should return an empty value.", "", parser.extractServerFromHttpProxyPass("???"));
        assertEquals("When value is ???, test should return an empty value.", "", parser.extractServerFromHttpProxyPass("XX:XX"));
    }

    void testExtractPortFromHttpProxyPassWithBadValues() {
        HttpdParser parser = new HttpdParser()
        assertEquals("When value is null, test should return an empty value.", "", parser.extractPortFromHttpProxyPass(null));
        assertEquals("When value is empty, test should return an empty value.", "", parser.extractPortFromHttpProxyPass(""));
        assertEquals("When value is ???, test should return an empty value.", "", parser.extractPortFromHttpProxyPass("???"));
        assertEquals("When value is ???, test should return an empty value.", "", parser.extractPortFromHttpProxyPass("XX:XX"));
    }

    void testExtractPortFromHttpProxyPassUsual() {
        HttpdParser parser = new HttpdParser()
        assertEquals("81", parser.extractPortFromHttpProxyPass("http://web.fr:81/myapp"))
        assertEquals("82",parser.extractPortFromHttpProxyPass("http://web.fr:82/myapp"))
    }
}
