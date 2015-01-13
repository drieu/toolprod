package fr.edu.toolprod.gson

import fr.edu.toolprod.gson.GSONBean
import org.apache.commons.logging.LogFactory


/**
 * Created with IntelliJ IDEA.
 * User: drieu
 * Date: 12/12/14
 * Time: 14:09
 * To change this template use File | Settings | File Templates.
 */
class GSONParser {

    private static final log = LogFactory.getLog(this)


    public String createTree(GSONBean node) {
        String data = ""
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
     * Produce String in JSON format
     * e.g : var zNodes={ ... }
     * @param node
     * @return String
     */
    public String createDataFromNode(GSONBean node) {
        String data = "var zNodes = [\n"
        for (GSONBean c : node.getChildren()) {
            data += "{\n"
            data +=  "name:'" + node.name + "',open:true,\n"
            data += createDataChildFromNode(c)
            data += "},\n"
        }
        data += "];"
        return data
    }

    private String createDataChildFromNode(GSONBean cbis) {
        String result = ""
        result += "\t\tchildren: [\n"
        log.info("NAME:" + cbis?.nodeData?.name)
        result += "                    { name: '" + cbis?.nodeData?.name + "',open:true,\n"
        log.info("SIZE:" + cbis.getChildren().size())
        if (cbis.getChildren().size() != 0) {
            result += "\t\tchildren: [\n"
            int count = cbis.getChildren().size()
            int cpt = 0
            for (GSONBean node : cbis.getChildren())  {
                result +=  "{name:'" + node?.nodeData?.name + "'}\n"
                if (cpt != count - 1) {
                    result += ","
                }
                cpt = cpt + 1
//                    result += createDataChildFromNode(node)
            }
            result += "\t\t]\n"
        }
        result += "}\n"
        result += "\t\t]\n"
        return result

    }
}
