package fr.edu.toolprod.bean


import grails.test.GrailsUnitTestCase

/**
 * Test AppBean.
 */
class AppBeanSpec extends GrailsUnitTestCase   {

    void testSetUrlTestWithNominal() {
        AppBean appBean = new AppBean()
        appBean.setUrl("test.com", "8384", "myapp")
        assertEquals("http://test.com:8384/myapp", appBean.serverUrl)
    }

    void testSetUrlTestWithNull() {
        AppBean appBean = new AppBean()
        appBean.setUrl(null, null, null)
        assertEquals("http://notdefined.com", appBean.serverUrl)
    }

    void testSetUrlWithShortNominal() {
        AppBean appBean = new AppBean()
        appBean.setUrl("test.com", "8384", "myapp","racvision")
        assertEquals("http://test.com:8384/myapp/racvision", appBean.serverUrl)
    }

    void testSetUrlWithShortNull() {
        AppBean appBean = new AppBean()
        appBean.setUrl(null, null, null, null)
        assertEquals("http://notdefined.com", appBean.serverUrl)
    }

    void testAppPortWithNull() {
        AppBean appBean = new AppBean()
        appBean.setAppPort(null)
        assertEquals("80", appBean.appPort)
    }

    void testAppPortWithNominal() {
        AppBean appBean = new AppBean()
        appBean.setAppPort("62")
        assertEquals("62", appBean.appPort)
    }
}
