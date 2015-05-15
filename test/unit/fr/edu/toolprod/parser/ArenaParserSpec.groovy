package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.ArenaBean
import grails.test.GrailsUnitTestCase

/**
 * Test ARENA parsing method.
 */
class ArenaParserSpec extends GrailsUnitTestCase  {

    def is
    def isErr

    @Override
    void setUp() {
        is = new FileInputStream("test/integration/arena_test.xml")
        isErr = new FileInputStream("test/integration/arena_test_with_error.xml")
    }

    @Override
    void tearDown() {
        is.close()
    }

    void testParseNominalCase() {
        Parser parser = new ArenaParser(is)
        List<ArenaBean> arenaBeans = parser.parse()
        assertNotNull(arenaBeans)

        assertEquals(2,arenaBeans.size())
        ArenaBean bean = arenaBeans.get(0)
        assertEquals("trombi_test",bean.appName)
        bean = arenaBeans.get(1)
        assertEquals("moodle",bean.appName)
    }

    void testParseWithError() {
        Parser parser = new ArenaParser(isErr)
        List<ArenaBean> arenaBeans = parser.parse()
        assertNotNull(arenaBeans)
    }

    void testParseWithNull() {
        Parser parser = new ArenaParser(null)
        List<ArenaBean> arenaBeans = parser.parse()
        assertEquals(0, arenaBeans.size())
    }


}
