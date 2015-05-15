package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.AppBean
import org.apache.commons.logging.LogFactory
import toolprod.App
import toolprod.Server
import toolprod.TreeNode
import fr.edu.toolprod.node.Node

/**
 * Persist Node data.
 */
abstract class TreeNodeData extends Node {

    /**
     * Logger.
     */
    private static final log = LogFactory.getLog(this)

    /**
     * Constant.
     */
    private static final String EMPTY = ""

    /**
     * Suffix for a node name.
     * It solves problem with 2 parents with same childs (eg: wappsco1:10206 et wappsco2:10206 )
     */
    protected String suffixNodeName = EMPTY


    /**
     * Constructor.
     * @param suffix
     */
    TreeNodeData(String suffix) {
        log.info("TreeNodeData() constructor with suffix:" + suffix)
        if ( suffix != null ) {
            if (!suffix.isEmpty()) {
                suffixNodeName = suffix
            }
        }
    }

    /**
     * Save a child with server
     * @param node
     * @param server
     * @return
     */
    def saveServerChild(TreeNode parent, Server serverChild) {
        log.info("saveServerChild(parent, serverChild) save under node:" + parent?.nodeData?.name + " Server child : " + serverChild?.toString())
        String nodeName = buildNodeName(suffixNodeName, parent?.nodeData?.name, serverChild.name)
        TreeNode resultNode = saveNode(parent, serverChild, nodeName, serverChild.portNumber.toString())
        return resultNode
    }

    public abstract TreeNode saveTreeLeaf(TreeNode treeNodeParent, App app, AppBean appBean, Server serv) ;


    public abstract TreeNode saveServerChild(TreeNode treeNodeParent, AppBean appBean) ;


    public abstract void saveTree(App myApp, AppBean appBean, Server server) ;


}




