package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.AppBean
import grails.test.GrailsUnitTestCase
/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */

class XmlParserSpec extends GrailsUnitTestCase {

    /**
     * Test parse of ProxyPass line.
     */
    void testParseProxyPass() {
        assertNull(XmlParser.parseProxyPass(null))
        assertNull(XmlParser.parseProxyPass(""))

        AppBean appBean

        appBean = XmlParser.parseProxyPass("ProxyPass  /test http://web3.ac-limoges.fr:8123/test")
        assertNotNull(appBean)
        assertEquals("test",appBean.name)
        assertEquals("http://web3.ac-limoges.fr:8123/test",appBean.serverUrl)
        assertEquals("8123",appBean.serverPort)

    }

    /**
     * Test parse of LoadModule line.
     */
    void testParseLoadModule() {
        assertNotNull(XmlParser.parseLoadModule(null))
        assertEquals("", XmlParser.parseLoadModule(null))
        assertEquals("", XmlParser.parseLoadModule(""))
        assertEquals("mem_cache_module", XmlParser.parseLoadModule("LoadModule mem_cache_module modules/mod_mem_cache.so"))
        assertEquals("", XmlParser.parseLoadModule("LoadModule "))
    }

    /**
     * Test extract server name in ServerName line.
     */
    void testParseServerName() {
        assertNotNull(XmlParser.parseServerName(null))
        assertEquals("", XmlParser.parseServerName(null))
        assertEquals("", XmlParser.parseServerName(""))
        assertEquals("test.ac-limoges.fr", XmlParser.parseServerName("ServerName test.ac-limoges.fr"))
        assertEquals("", XmlParser.parseServerName("ServerName "))
    }

    /**
     * Test extract server port in Listen line.
     */
    void testParseListen() {
        assertNotNull(XmlParser.parseListen(null))
        assertEquals("", XmlParser.parseListen(null))
        assertEquals("", XmlParser.parseListen(""))
        assertEquals("8089", XmlParser.parseListen("Listen 8089"))
        assertEquals("80", XmlParser.parseListen("Listen "))
    }

    /**
     * Test extract weblogic port from WebLogicPort line.
     */
    void testParseWebLogicPort() {
        def weblogicPort

        weblogicPort = XmlParser.parseWebLogicPort(null)
        assertEquals(weblogicPort, "")

        weblogicPort = XmlParser.parseWebLogicPort("")
        assertEquals(weblogicPort, "")

        weblogicPort = XmlParser.parseWebLogicPort("WebLogicPort 10507")
        assertEquals(weblogicPort, "10507")

        weblogicPort = XmlParser.parseWebLogicPort("#WebLogicPort 10507")
        assertEquals(weblogicPort, "")

        weblogicPort = XmlParser.parseWebLogicPort("#   WebLogicPort 10507")
        assertEquals(weblogicPort, "")

    }

    /**
     * Test extract weblogic port from WebLogicHost line
     */
    void testParseWebLogicHost() {
        def weblogicHost

        weblogicHost = XmlParser.parseWebLogicHost(null)
        assertEquals(weblogicHost, "")

        weblogicHost = XmlParser.parseWebLogicHost("")
        assertEquals(weblogicHost, "")

        weblogicHost = XmlParser.parseWebLogicHost("WebLogicHost webapp5.ac-limoges.fr")
        assertEquals(weblogicHost, "webapp5.ac-limoges.fr")

        weblogicHost = XmlParser.parseWebLogicHost("#  WebLogicHost webapp5.ac-limoges.fr")
        assertEquals(weblogicHost, "")

        weblogicHost = XmlParser.parseWebLogicHost("#WebLogicHost webapp5.ac-limoges.fr")
        assertEquals(weblogicHost, "")
    }

        /**
     * Test Extract server:port from WebLogicCluster line
     * e.g : WebLogicCluster web1.ac-limoges.fr:77777, web2.ac-limoges.fr:77347
     */
    void testParseWebLogicCluster() {
        List<String> weblos = XmlParser.parseWebLogicCluster(null)
        assertNotNull(weblos)
        assertEquals(0, weblos.size())

        weblos = XmlParser.parseWebLogicCluster("WebLogicCluster web1.ac-limoges.fr:77777, web2.ac-limoges.fr:77347")
        assertTrue(weblos.contains("web1.ac-limoges.fr:77777"))
        assertTrue(weblos.contains("web2.ac-limoges.fr:77347"))

        weblos = XmlParser.parseWebLogicCluster("WebLogicCluster web1.ac-limoges.fr:77777")
        assertTrue(weblos.contains("web1.ac-limoges.fr:77777"))
        assertEquals(1, weblos.size())

        weblos = XmlParser.parseWebLogicCluster("web1.ac-limoges.fr:77777, web2.ac-limoges.fr:77347")
        assertEquals(0, weblos.size())

        weblos = XmlParser.parseWebLogicCluster("   WebLogicCluster web1.ac-limoges.fr:77777, web2.ac-limoges.fr:77347")
        assertTrue(!weblos.contains("WebLogicCluster web1.ac-limoges.fr:77777"))
        assertTrue(!weblos.contains("WebLogicCluster web2.ac-limoges.fr:77347"))

        weblos = XmlParser.parseWebLogicCluster("#   WebLogicCluster web1.ac-limoges.fr:77777, web2.ac-limoges.fr:77347")
        assertEquals(0, weblos.size())

    }


    /**
     * Extract app name in ProxyPass without '/'
     * e.g : We extract the / of /securite in ProxyPass /securite http://... )
     */
    void testParseAppNameInProxyPass() {
        assertEquals("myapp", XmlParser.parseAppNameInProxyPass("/myapp/"))
        assertEquals("myapp", XmlParser.parseAppNameInProxyPass("/myapp"))
        assertEquals("myapp", XmlParser.parseAppNameInProxyPass("myapp/"))
        assertEquals("", XmlParser.parseAppNameInProxyPass(""))
        assertEquals("", XmlParser.parseAppNameInProxyPass(null))
    }

    /**
    *  Extract server from an url (e.g:http://web.fr:82/myapp)
    */
    void testParseServerFromHttpProxyPassUsual() {
        assertEquals("web.fr", XmlParser.parseServerFromHttpProxyPass("http://web.fr:82/myapp"))
        assertEquals("test.org",XmlParser.parseServerFromHttpProxyPass("http://test.org:82/myapp"))
    }

    /**
     * Extract server from a bad url
     */
    void testParseServerFromHttpProxyPassWithBadValues() {
        assertEquals("When value is null, test should return an empty value.", "", XmlParser.parseServerFromHttpProxyPass(null));
        assertEquals("When value is empty, test should return an empty value.", "", XmlParser.parseServerFromHttpProxyPass(""));
        assertEquals("When value is ???, test should return an empty value.", "", XmlParser.parseServerFromHttpProxyPass("???"));
        assertEquals("When value is ???, test should return an empty value.", "", XmlParser.parseServerFromHttpProxyPass("XX:XX"));
    }


    /**
     * Extract port from an url (e.g:http://web.fr:81/myapp)
     */
    void testParsePortFromHttpProxyPassUsual() {
        assertEquals("81", XmlParser.parsePortFromHttpProxyPass("http://web.fr:81/myapp"))
        assertEquals("82",XmlParser.parsePortFromHttpProxyPass("http://web.fr:82/myapp"))
    }

    /**
     * Extract port from a bad url
     */
    void testParserPortFromHttpProxyPassWithBadValues() {
        assertEquals("When value is null, test should return an empty value.", "", XmlParser.parsePortFromHttpProxyPass(null));
        assertEquals("When value is empty, test should return an empty value.", "", XmlParser.parsePortFromHttpProxyPass(""));
        assertEquals("When value is ???, test should return an empty value.", "", XmlParser.parsePortFromHttpProxyPass("???"));
        assertEquals("When value is ???, test should return an empty value.", "", XmlParser.parsePortFromHttpProxyPass("XX:XX"));
    }

    /**
     * Extract protocol in url (e.g:http://webX.fr:PORT/APPLI)
     */
    void testParseProtocol() {
        assertEquals("Empty value should return an EMPTY value", "", XmlParser.parseProtocol(""))
        assertEquals("Empty value should return an EMPTY value", "http://", XmlParser.parseProtocol("http://www.test.com"))
        assertEquals("Empty value should return an EMPTY value", "http://", XmlParser.parseProtocol("http://127.0.0.1:8081/appli"))
        assertEquals("Empty value should return an EMPTY value", "https://", XmlParser.parseProtocol("https://www.test.com"))
        assertEquals("Empty value should return an EMPTY value", "https://", XmlParser.parseProtocol("https://127.0.0.1:8081/appli"))
    }

    /**
     * Test method to extract name in <Location NAME>
     */
    void testParseLocationName() {
        assertEquals("Empty value should return an EMPTY value", "", XmlParser.parseLocationName(""))
        assertEquals("Parse Bad tag for Location should return an EMPTY value", "", XmlParser.parseLocationName("<loca"))
        assertEquals("Parse Bad tag for Location (not finished with >) should return an EMPTY value", "test", XmlParser.parseLocationName("<Location test"))
        assertEquals("nominal case should work", "test", XmlParser.parseLocationName("<Location test>"))
        assertEquals("nominal case should work", "test", XmlParser.parseLocationName("<Location test >"))
    }


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
}

