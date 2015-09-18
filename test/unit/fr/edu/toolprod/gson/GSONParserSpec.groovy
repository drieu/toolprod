package fr.edu.toolprod.gson

import fr.edu.toolprod.bean.ServerBean
import grails.test.GrailsUnitTestCase



class GSONParserSpec extends GrailsUnitTestCase {

    ServerBean serverBean

    ServerBean child

    ServerBean serverBeanChild

    @Override
    void setUp() {
        serverBean = new ServerBean()
        serverBean.name = "source_aeronautique"
        serverBean.portNumber = "80"

        child = new ServerBean()
        child.name = "appliloc6.ac-limoges.fr"
        child.portNumber = "8015"

        serverBeanChild = new ServerBean()
        serverBeanChild.name = "web1.ac-limoges.fr"
        serverBeanChild.portNumber = "8052"
    }

    void testCreateTreeWithOneParent() {
        GSONBean gsonBean = new GSONBean(serverBean)
        gsonBean.name = "source_aeronautique"

        gsonBean.nodeData = serverBean
//        GSONBean nChild = gsonBean.addChild(child)
//        nChild.addChild(serverBeanChild)

        GSONParser parser = new GSONParser()
        String str = parser.createTreeWithoutPersist(gsonBean)
        log.info(str)
        assertEquals("{name:'source_aeronautique_80',open:true}", str)

    }
}
