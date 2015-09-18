package fr.edu.toolprod.parser

import grails.test.GrailsUnitTestCase

/**
 * Unit test for BigIp.conf parser methods.
 */
class BigIpParserSpec extends GrailsUnitTestCase {

    void testExtractServerPortWithEmptyTestCase(){
        BigIpParser parser = new BigIpParser((InputStream)null)
        assertEquals("Should return empty !","" , parser.extractServerPort(null))
        assertEquals("Should return empty !","" , parser.extractServerPort(""))
        assertEquals("Should return empty !","" , parser.extractServerPort(":"))
    }

    void testExtractServerPortWithBadValue(){
        BigIpParser parser = new BigIpParser((InputStream)null)
        assertEquals("Should return empty !","" , parser.extractServerPort("/ABD/BAD"))
        assertEquals("Should return empty because there is no { at the end.","" , parser.extractServerPort("/LB-PUBLIC/webX:8025"))
    }

    void testExtractServerPortWithNominalCase(){
        BigIpParser parser = new BigIpParser((InputStream)null)
        assertEquals("Should return server port.","8025" , parser.extractServerPort("/LB-PUBLIC/webX:8025  {"))
    }

    void testExtractServerNameWithEmptyTestCase(){
        BigIpParser parser = new BigIpParser((InputStream)null)
        assertEquals("Should return empty !","" , parser.extractServerName(null))
        assertEquals("Should return empty !","" , parser.extractServerName(""))
        assertEquals("Should return empty !","" , parser.extractServerName(":"))
        assertEquals("Should return empty !","" , parser.extractServerName("/"))
        assertEquals("Should return empty !","" , parser.extractServerName("//"))
    }

    void testExtractServerNameWithBadValue() {
        BigIpParser parser = new BigIpParser((InputStream)null)
        assertEquals("Should return empty !","" , parser.extractServerName("/LB-PUBLIC/:8028"))
        assertEquals("Should return empty !","" , parser.extractServerName("/LB-PUBLIC/BAD"))
    }

    void testExtractServerNameWithNominalCase(){
        BigIpParser parser = new BigIpParser((InputStream)null)
        assertEquals("Should return webX from /LB-PUBLIC/webX:8028","webX.ac-limoges.fr" , parser.extractServerName("/LB-PUBLIC/webX:8028"))
    }


    void testExtractShortVipName() {
        BigIpParser parser = new BigIpParser((InputStream)null)
        assertEquals("Should return empty !","" , parser.extractShortVipName(null))
        assertEquals("Should return empty !","" , parser.extractShortVipName("-"))
        assertEquals("Should return empty !","" , parser.extractShortVipName("."))
    }

    void testExtractShortVipNameWithBadValue() {
        BigIpParser parser = new BigIpParser((InputStream)null)
        assertEquals("Should return empty !","" , parser.extractShortVipName("ltm pool /LB-PUBLIC/pool.ac-limoges"))
    }

    void testExtractShortVipNameWithNominalCase() {
        BigIpParser parser = new BigIpParser((InputStream)null)
        assertEquals("Should return appli !","appli" , parser.extractShortVipName("ltm pool /LB-PUBLIC/pool-appli.ac-limoges.... {"))
    }


    void testExtractFullVipName() {
        BigIpParser parser = new BigIpParser((InputStream)null)
        assertEquals("Should return empty !","" , parser.extractFullVipName(null))
        assertEquals("Should return empty !","" , parser.extractFullVipName("-"))
        assertEquals("Should return empty !","" , parser.extractFullVipName("."))
    }

    void testExtractFullVipNameWithBadValue() {
        BigIpParser parser = new BigIpParser((InputStream)null)
        assertEquals("Should return empty !","" , parser.extractFullVipName("ltm pool /LB-PUBLIC/pool.ac-limoges"))
    }

    void testExtractFullVipNameWithNominalCase() {
        BigIpParser parser = new BigIpParser((InputStream)null)
        assertEquals("Should return appli !","pool-appli.ac-limoges.fr_ssl" , parser.extractFullVipName("ltm pool /LB-PUBLIC/pool-appli.ac-limoges.fr_ssl_grpid_6 {"))
    }

}
