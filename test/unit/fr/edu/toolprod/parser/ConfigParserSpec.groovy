package fr.edu.toolprod.parser

import grails.test.GrailsUnitTestCase

/**
 * Created with IntelliJ IDEA.
 * User: drieu
 * Date: 17/10/14
 * Time: 11:54
 * To change this template use File | Settings | File Templates.
 */
class ConfigParserSpec extends GrailsUnitTestCase {

    void testParseGroup() {
        ConfigParser configParser = new ConfigParser(null)
        configParser.parseGroup("group_name=machine1,machine2")
        Map<String, List<String>> map = configParser.machineByGroup
        println(map.toString())
        assertNotNull(map)
        assertNotSame(0, map.size())
        assertTrue(map.containsKey('name'));
        List<String> lst = map.get('name')
        assertTrue(lst.contains('machine1'))
        assertTrue(lst.contains('machine2'))
    }
}
