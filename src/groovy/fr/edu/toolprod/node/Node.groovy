package fr.edu.toolprod.node

import org.apache.commons.logging.LogFactory
import toolprod.App
import toolprod.Server
import toolprod.TreeNode

/**
 * Class to Manage Node.
 */
class Node {

    private static final log = LogFactory.getLog(this)

    private static final String PREFIX_PARENT_NODE_NAME = "source_"

    private static final String DEFAULT_APACHE_PORT = "80"

    private static final String DEFAULT_NODE_PORT = "80"


    /**
     * Create a parent node for an App.(e.g : source_[application name] ).
     *
     * @param myApp
     * @param applicationName application name.
     */
    def createParentNodeForApp(App myApp, String applicationName) {

        String  nodeParentName = PREFIX_PARENT_NODE_NAME + applicationName
        log.info("createParentNodeForApp() nodeParentName:" + nodeParentName + " myApp:" + myApp.toString() )
        TreeNode treeNodeParent = null
        if ( (nodeParentName != null) && (nodeParentName.isEmpty() == false)) {

            log.debug("createParentNodeForApp() search if parent with name: " + nodeParentName + "exists")
            treeNodeParent = TreeNode.findByName(nodeParentName)
            if (treeNodeParent == null) {
                treeNodeParent = saveSourceNode(nodeParentName)
                myApp.node = treeNodeParent
                myApp.save(failOnError: true, flush:true)
                log.info("createParentNodeForApp() create parent: " + nodeParentName)
            } else {
                log.info("createParentNodeForApp() parent node " + nodeParentName + "still exists")
            }
        } else {
            log.warn("createParentNodeForApp() Bad node parent name ( null or empty) ")
        }
        log.info("createParentNodeForApp() MyApp with parent node :" + treeNodeParent.toString())
        return treeNodeParent
    }

    /**
     * Save a fake server app which will be parents of all application found in htttp.conf.
     * Name of source node : source_[application name]
     * @param application name
     */
    private TreeNode saveSourceNode(String sourceName) {
        log.info("saveSourceNode() Save parent with name :" + sourceName)
        Server serverSource = Server.findByNameAndPortNumber(sourceName, DEFAULT_APACHE_PORT)
        if ( serverSource == null) {
            serverSource = new Server(sourceName, DEFAULT_APACHE_PORT, sourceName)
            serverSource.serverType=Server.TYPE.APACHE
            serverSource.save(failOnError: true, flush:true)
        }

        TreeNode node = TreeNode.findByNodeData(serverSource)
        if (node == null) {
            node = new TreeNode(serverSource)
            node.nodeData = serverSource
            node.name = sourceName
            node.port = DEFAULT_NODE_PORT
            node.save(failOnError: true, flush:true)
            log.info("saveSourceNode() new source node create with sourcename:" + serverSource)
        } else {
            log.info("saveSourceNode() nothing to do source node still exists sourceName:" + sourceName)
        }
        return node
    }

    /**
     * Save a node
     * @param parent Parent of this node
     * @param serverChild Server cild.
     * @param nodeName
     * @param serverChildPortNumber
     * @return
     */
    def saveNode (TreeNode parent, Server serverChild, String nodeName, String serverChildPortNumber) {
        TreeNode resultNode = null
        if ( parent == null) {
            log.warn("saveNode () : Parent is null for nodeName:" + nodeName + " and serverChildPortNumber:" + serverChildPortNumber)
            return resultNode
        }
        if ( serverChild == null) {
            log.warn("saveNode () : Server is null for nodeName:" + nodeName + " and serverChildPortNumber:" + serverChildPortNumber)
            return resultNode
        }
        if (nodeName == null) {
            log.warn("saveNode () : nodeName is null !")
            return resultNode
        }
        if (serverChildPortNumber == null) {
            log.warn("saveNode () : serverChildPortNumber is null for nodeName:" + nodeName)
            return resultNode
        }

        log.info("saveNode()  nodeName:" + nodeName + " port:" + serverChildPortNumber)
        resultNode = TreeNode.findByNameAndPort(nodeName, serverChildPortNumber)
        if (resultNode == null) {
            resultNode = parent.addChild(serverChild)
            resultNode.parent = parent
            resultNode.name = nodeName
            resultNode.port = serverChildPortNumber
            resultNode.save(failOnError: true, flush:true)
        } else {
            log.info("saveNode() node still exists :" + nodeName + " with port:" + serverChildPortNumber)
        }
        return resultNode
    }


    /**
     * Search node for a Server.
     * @param searchServer
     * @return null if not found.
     */
    public TreeNode searchNode(Server searchServer) {
        TreeNode resultNode = null
        if (searchServer != null) {
            log.info("searchNode() Search node for  :" + searchServer.toString())
            resultNode = TreeNode.findByNodeData(searchServer)
        } else {
            log.warn("searchNode() : searchServer is null !")
        }
        return resultNode
    }

    /**
     * Print treeNode on console.
     * @param myApp
     */
    def showNode(App myApp) {
        TreeNode node = myApp.node
        log.info("showNode() ====>SHOW NODE<====")
        log.info("showNode() ====>Source :" + node.toString())
        for (TreeNode c : node.getChildren()) {
            if (c != null && c.nodeData != null) {
                log.info("showNode() Child 1:" + c.toString())
                for (TreeNode cbis : c.getChildren()) {
                    if (c != null && c.nodeData != null) {
                        log.info("showNode() Child 2:" + cbis.toString())
                    }
                }
            }
        }
        log.info("showNode() ====><====")
    }

    /**
     * Create Unique Node name.
     * if web1 -> serv1 for MYAPP => name will be MYAPP_WEB1_SERV1
     * @param suffixNodeName e.g :application Name ( e.g : MYAPP )
     * @param parentName ( e.g : WEB1 )
     * @param childName  ( e.g : SERV1 )
     * @return
     */
    String buildNodeName(String suffixNodeName, String parentName, String childName)   {
        String name =  childName
        if ((suffixNodeName != null) && (parentName != null) && (childName!=null)) {
            name = suffixNodeName + "_" + parentName + "_" + childName
        } else {
            log.warn("buildNodeName() : a paramaeter is null suffixNodeName:" + suffixNodeName)
            log.warn("buildNodeName() : a paramaeter is null parentName:" + parentName)
            log.warn("buildNodeName() : a paramaeter is null childName:" + childName)
        }
        return name
    }





}
