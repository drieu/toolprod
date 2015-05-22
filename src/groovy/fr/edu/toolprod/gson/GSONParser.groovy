package fr.edu.toolprod.gson

import fr.edu.toolprod.bean.ServerBean
import org.apache.commons.logging.LogFactory
import toolprod.TreeNode


/**
 * Generate GSON.
 */
class GSONParser {


    private static final String PARAM_NAME = "name"

    /**
     * EMPTY.
     */
    private static final String EMPTY = ""

    /**
     * Logger.
     */
    private static final log = LogFactory.getLog(this)

    /**
     * Generate GSON String from a TreeNode
     * @param node
     * @return GSON String e.g:{name:...
     */
    public String createTree(TreeNode node) {
        String data = EMPTY
        log.info("createTree()")
        if (node != null) {
            data += "{name:'" + getRealParentName(node?.nodeData?.name, node?.nodeData?.portNumber) + "',open:true"

            if (node.getChildren().size() != 0) {
                data += ", children:["

                int nbChild =  node.getChildren().size()
                int cpt = 0
                for (TreeNode child : node.getChildren()) {

                    if (child.getChildren().size() == 0) {
                        if (cpt == ( nbChild -1 )) { // if last
                            data += "{name:'" + child?.nodeData?.name + "_" + child?.nodeData?.portNumber  + "',open:true}"
                        } else {
                            data += "{name:'" + child?.nodeData?.name + "_" + child?.nodeData?.portNumber  + "',open:true},"
                        }
                    } else {
                        data += "\n"
                        data += createTree(child)
                        if (cpt != ( nbChild -1 )) {
                            data += ","
                        }
                        data += "\n"
                    }
                    cpt = cpt + 1
                }
                data += "]}"
            } else {
                data += "}"
            }
        }

        return data
    }

    /**
     * Get the real parent name
     * @param name
     * @param portNumber
     * @return
     */
    private static String getRealParentName(String name, Integer portNumber) {
        String result = "?"
        if (name != null) {
            if (name.contains(PARAM_NAME)) {
                result = name.substring(PARAM_NAME.size(), name.length())
            } else {
                result = name + "_" + portNumber.toString()
            }
        }
        return result
    }

    /**
     * Create a GSON tree without using TreeNode which is persist.
     * Use to test createTree method.
     * @param node GSONBean
     * @return
     */
    public String createTreeWithoutPersist(GSONBean node) {
        String data = EMPTY
        if (node != null) {
            data += "{name:'" + node?.nodeData?.name + "_" + node?.nodeData?.portNumber + "',open:true"

            if (node.getChildren().size() != 0) {
                data += ", children:["

                int nbChild =  node.getChildren().size()
                int cpt = 0
                for (GSONBean child : node.getChildren()) {

                    if (child.getChildren().size() == 0) {
                        if (cpt == ( nbChild -1 )) { // if last
                            data += "{name:'" + child?.nodeData?.name + "_" + node?.nodeData?.portNumber  + "',open:true}"
                        } else {
                            data += "{name:'" + child?.nodeData?.name + "_" + node?.nodeData?.portNumber  + "',open:true},"
                        }
                    } else {
                        data += "\n"
                        data += createTreeWithoutPersist(child)
                        if (cpt != ( nbChild -1 )) {
                            data += ","
                        }
                        data += "\n"
                    }
                    cpt = cpt + 1
                }
                data += "]}"

            } else {
                data += "}"
            }
        }

        return data
    }

    /**
     * Method to test produce GSON.
     * @return
     */
     public static String createDataTest() {
         ServerBean serverBean

         ServerBean child
         ServerBean child2
         ServerBean child3

         ServerBean serverBeanChild
         ServerBean serverBeanChild2

         serverBean = new ServerBean()
         serverBean.name = "source_aeron"
         serverBean.portNumber = "80"

         child = new ServerBean()
         child.name = "apptest6.ac-limoges.fr"
         child.portNumber = "8888"

         child2 = new ServerBean()
         child2.name = "apptest7.ac-limoges.fr"
         child2.portNumber = "8909"

         child3 = new ServerBean()
         child3.name = "apptest8.ac-limoges.fr"
         child3.portNumber = "8181"

         serverBeanChild = new ServerBean()
         serverBeanChild.name = "webtest1.ac-limoges.fr"
         serverBeanChild.portNumber = "8282"

         serverBeanChild2 = new ServerBean()
         serverBeanChild2.name = "webtest2.ac-limoges.fr"
         serverBeanChild2.portNumber = "8383"

         GSONBean gsonBean = new GSONBean(serverBeanChild)
         gsonBean.name = "source_aero"

         gsonBean.nodeData = serverBean
         GSONBean nChild = gsonBean.addChild(child)
         nChild.addChild(child2)
         gsonBean.addChild(serverBeanChild)


         GSONParser parser = new GSONParser()
         return parser.createTreeWithoutPersist(gsonBean)
     }



}
