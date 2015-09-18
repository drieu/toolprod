package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.AppBean
import fr.edu.toolprod.bean.MultipartFileBean
import fr.edu.toolprod.bean.ServerBean
import grails.test.GrailsUnitTestCase
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartFile

class HttpdParserSpec extends GrailsUnitTestCase {

    MultipartFileBean file
    InputStream is

    @Override
    void setUp() {
        InputStream is = new FileInputStream("test/integration/httpd.conf.app")

        file = new MultipartFileBean()
        file.inputStream = is
        file.originalFilename = "httpd.conf.app"

    }

    @Override
    void tearDown() {
        if (is != null) {
            is.close()
        }
    }

    void testParse() {
        HttpdParser parser = new HttpdParser(file, "mymachine")
        parser.parse()
        assertNotNull(parser.appBeans)
        assertEquals(1,parser.appBeans.size())

        ServerBean serverBean = parser.serverBean
        assertEquals("titiWeb.ac-limoges.fr" ,serverBean.name)
        assertEquals("8032" ,serverBean.portNumber)


        AppBean appBean = parser.appBeans.get(0)
        assertEquals("titi" ,appBean.name)
        assertEquals("http://titi.ac-limoges.fr/titi/", appBean.serverUrl)
        assertEquals("serverPort should be 80", "80", appBean.serverPort)


        assertEquals("appPort should be 80", "80", appBean.appPort)
        assertEquals("titi.ac-limoges.fr", appBean.appServer)
    }


    void testParseWithNull() {
        try {
            HttpdParser parser = new HttpdParser(null, null)
            parser.parse()
            assertFalse("An exception should be thrown", false)
        } catch(IllegalArgumentException e) {
            assertTrue("An IllegalArgumentException exception should be thrown :" + e.message,true)
        }
    }

    void testCheckWithNull() {
        HttpdParser parser = new HttpdParser(file, "fakeMachine")
        assertFalse(parser.check(null, null))
    }

}
