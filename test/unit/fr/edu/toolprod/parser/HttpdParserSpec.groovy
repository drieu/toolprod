package fr.edu.toolprod.parser


import grails.test.GrailsUnitTestCase
/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */

class HttpdParserSpec extends GrailsUnitTestCase {

//    void testExtractAppNameInProxyPass() {
//        HttpdParser parser = new HttpdParser()
//        assertEquals("myapp", parser.extractAppNameInProxyPass("/myapp/"))
//        assertEquals("myapp", parser.extractAppNameInProxyPass("/myapp"))
//        assertEquals("myapp", parser.extractAppNameInProxyPass("myapp/"))
//        assertEquals("", parser.extractAppNameInProxyPass(""))
//        assertEquals("", parser.extractAppNameInProxyPass(null))
//
//    }
//
//    void testExtractServerFromHttpProxyPassUsual() {
//        HttpdParser parser = new HttpdParser()
//        assertEquals("web.fr", parser.extractServerFromHttpProxyPass("http://web.fr:82/myapp"))
//        assertEquals("test.org",parser.extractServerFromHttpProxyPass("http://test.org:82/myapp"))
//    }
//
//    void testExtractServerFromHttpProxyPassWithBadValues() {
//        HttpdParser parser = new HttpdParser()
//        assertEquals("When value is null, test should return an empty value.", "", parser.extractServerFromHttpProxyPass(null));
//        assertEquals("When value is empty, test should return an empty value.", "", parser.extractServerFromHttpProxyPass(""));
//        assertEquals("When value is ???, test should return an empty value.", "", parser.extractServerFromHttpProxyPass("???"));
//        assertEquals("When value is ???, test should return an empty value.", "", parser.extractServerFromHttpProxyPass("XX:XX"));
//    }
//
//    void testExtractPortFromHttpProxyPassWithBadValues() {
//        HttpdParser parser = new HttpdParser()
//        assertEquals("When value is null, test should return an empty value.", "", parser.extractPortFromHttpProxyPass(null));
//        assertEquals("When value is empty, test should return an empty value.", "", parser.extractPortFromHttpProxyPass(""));
//        assertEquals("When value is ???, test should return an empty value.", "", parser.extractPortFromHttpProxyPass("???"));
//        assertEquals("When value is ???, test should return an empty value.", "", parser.extractPortFromHttpProxyPass("XX:XX"));
//    }
//
//    void testExtractPortFromHttpProxyPassUsual() {
//        HttpdParser parser = new HttpdParser()
//        assertEquals("81", parser.extractPortFromHttpProxyPass("http://web.fr:81/myapp"))
//        assertEquals("82",parser.extractPortFromHttpProxyPass("http://web.fr:82/myapp"))
//    }
//
//    void testParseWithNull() {
//        HttpdParser parser = new HttpdParser()
//        assertFalse(parser.parse(null, null))
//    }
//
//    void testGetApacheModules() {
//        HttpdParser parser = new HttpdParser()
//
//        // Null case.
//        def modules = parser.extractLoadModule(null)
//        assertNotNull("Module list should be empty and not null", modules)
//        assertEquals(0, modules.size())
//
//        //Usual case.
//        modules = parser.extractLoadModule("LoadModule access_module modules/mod_access.so")
//        assertNotNull("Module list should be empty and not null", modules)
//        assertEquals(1, modules.size())
//        assertTrue(modules.contains("access_module"))
//    }


    void testParseLocationName() {
        HttpdParser parser = new HttpdParser()
        assertEquals("Empty value should return an EMPTY value", "", parser.parseLocationName(""))
        assertEquals("Parse Bad tag for Location should return an EMPTY value", "", parser.parseLocationName("<loca"))
        assertEquals("Parse Bad tag for Location (not finished with >) should return an EMPTY value", "test", parser.parseLocationName("<Location test"))
        assertEquals("nominal case should work", "test", parser.parseLocationName("<Location test>"))
        assertEquals("nominal case should work", "test", parser.parseLocationName("<Location test >"))
    }
}
