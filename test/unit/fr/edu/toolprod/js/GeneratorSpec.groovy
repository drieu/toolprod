package fr.edu.toolprod.js

import grails.test.GrailsUnitTestCase

/**
 * Test Generator oj js element for tree structure.
 */
class GeneratorSpec extends GrailsUnitTestCase   {

    void testGenerateStrDataNominalCase() {
        Generator generator = new Generator()
        String gen = generator.generateStrData("myNodeName", "myParentName", "APP1", "title1 of APP1")
        assertNotNull(gen)
        assertFalse(gen.isEmpty())
        assertTrue("the name of the node must be present", gen.contains("myNodeName"))
        assertTrue("the parent name must be present", gen.contains("myParentName"))
        assertTrue("the name \"APP1\" must be present", gen.contains("myParentName"))
        assertTrue("the name \"title1\" must be present", gen.contains("title1"))

    }


    void testGenerateStrDataWithNullValue() {
        Generator generator = new Generator()
        String gen = generator.generateStrData(null, "myParentName", "APP1", "title1 of APP1")
        assertNotNull(gen)

        generator = new Generator()
        gen = generator.generateStrData("myNodeName", null, "APP1", "title1 of APP1")
        assertNotNull(gen, " a null parentName shoudn't cause an error")

        generator = new Generator()
        gen = generator.generateStrData("myNodeName", "myParentName", null, "title1 of APP1")
        assertNotNull(gen, " a null name shoudn't cause an error")

        generator = new Generator()
        gen = generator.generateStrData("myNodeName", "myParentName", "APP1", null)
        assertNotNull(gen, " a null title shoudn't cause an error")
    }

}
