package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.AppBean
import org.apache.commons.logging.LogFactory
import toolprod.App
import toolprod.Server
import toolprod.TreeNode

/**
 * Persist Node data
 */
class TreeNodeData {

    private static final log = LogFactory.getLog(this)

    /**
     * Create a parent node for an App.(e.g : source_[application name].
     *
     * @param myApp
     * @param nodeParentName
     */
    def createParentNodeForApp(App myApp, String nodeParentName) {
        log.info("createParentNodeForApp()")
        TreeNode treeNodeParent = null
        if ( (nodeParentName != null) && (nodeParentName.isEmpty() == false)) {
            log.debug("createParentNodeForApp() search if parent with name: " + nodeParentName + "exists")
            treeNodeParent = TreeNode.findByName("source_" + nodeParentName)
            if (treeNodeParent == null) {
                treeNodeParent = saveSourceNode(nodeParentName)
                myApp.node = treeNodeParent
                myApp.save(failOnError: true)
                log.debug("createParentNodeForApp() create parent: " + nodeParentName)
            }
        } else {
            log.warn("createParentNodeForApp() Bad node parent name ( null or empty) ")
        }
        log.debug("createParentNodeForApp() MyApp with parent node :" + myApp.toString())
        return treeNodeParent
    }

    /**
     * Save a fake server app which will have server parents found in httpd.conf.
     */
    def saveSourceNode(String sourceName) {
        log.info("saveSourceNode() Save parent with name : |source_" + sourceName + "|")
        String virtualName = "source_" + sourceName
        Server serverSource = new Server(virtualName, "80", virtualName)
        serverSource.serverType=Server.TYPE.APACHE
        serverSource.save(failOnError: true)

        TreeNode node = new TreeNode(serverSource)
        node.nodeData = serverSource
        node.name = virtualName
        node.save(failOnError: true)
        log.info("saveSourceNode() ==> SOURCE node:" + node.toString())
        return node
    }

    /**
     * Search node for a server.
     * @param searchServer
     * @return
     */
    public searchNode(Server searchServer) {
        log.info("Search node for  :" + searchServer.toString())
        return TreeNode.findByNodeData(searchServer)
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
    }

    /**
     * Save a child with server
     * @param node
     * @param server
     * @return
     */
    def saveChild(TreeNode childNode, Server server) {
        log.info("saveChild(childNode, server) ===> Save Server child :" + server?.toString() + " under node:" + childNode?.nodeData?.name)
        TreeNode node = childNode.addChild(server)
        node.name = "childNodeBis"
        node.save(failOnError: true)
        return node
    }

    /**
     * Save a child with appBean.
     */
    def saveChild(TreeNode treeNodeParent, AppBean appBean) {
        log.info("saveChild(treeNodeParent, appBean) AppBean to save() :" + appBean.toString())
        Server serverChild = new Server(appBean.appServer, appBean.appPort, appBean.appServer)
        serverChild.serverType=Server.TYPE.APACHE
        serverChild.save(failOnError: true)
        log.info("saveChild() Save Server child :" + serverChild.toString() + " under treeNodeParent node:" + treeNodeParent?.nodeData?.name )

        TreeNode childNode = treeNodeParent.addChild(serverChild)
        childNode.parent = treeNodeParent
        childNode.name = "child"
        childNode.save(failOnError: true)
        return childNode
    }

    def saveApacheTree(App myApp, AppBean appBean, Server server) {
        TreeNodeData treeNodeData = new TreeNodeData()
        TreeNode treeNodeParent = treeNodeData.createParentNodeForApp(myApp, appBean.name)

        if (treeNodeParent.isLeaf()) {
            log.info("saveApacheTree() Child does not exists for treeNodeParent with name:" + treeNodeParent?.nodeData?.name + " and  port:" + treeNodeParent?.nodeData?.name)
            TreeNode childNode
            if (appBean.appServer != null) {//If appBean.appServer != null, it is a link to an other apache
                childNode = treeNodeData.saveChild(treeNodeParent, appBean)
            } else {
                childNode = treeNodeParent
            }
            treeNodeData.saveChild(childNode, server)
            myApp.node.save(failOnError: true)
            myApp.save(failOnError: true)

        } else {
            log.info("saveApacheTree() Childs exist.Need to search where to add the node ...")
            TreeNode node
            if (appBean.appServer == null ) {
                treeNodeData.saveChild(treeNodeParent, server)    // we are on the local server
            } else {
                if (appBean.appPort == null) {
                    appBean.appPort = 80
                } else if (appBean.appPort.isEmpty()) {
                    appBean.appPort = 80
                }
                int portDefault = server.portNumber
                if (server.portNumber == null) {
                    portDefault = 80
                } else if (appBean.appPort.isEmpty()) {
                    portDefault = 80
                }
                Server searchServer = Server.findByNameAndPortNumber(server.name, portDefault)
                if ( searchServer != null ) {
                    TreeNode sNode = treeNodeData.searchNode(searchServer)
                    if ( sNode != null ) {

                        searchServer = Server.findByNameAndPortNumber(appBean.appServer, appBean.appPort.toInteger())
                        if (searchServer == null) {
                            log.debug("saveApacheTree() no searchServer found with name=" + appBean.appServer + " port:" +  appBean.appPort + ".Save it under treeNodeParent")
                            node = treeNodeData.saveChild(treeNodeParent, appBean)

                        } else {
                            log.debug("saveApacheTree() searchServer still exists")
                            node = treeNodeData.searchNode(searchServer)
                        }
                        sNode.parent = node
                        sNode.save()
                    } else {
                        log.warn("saveApacheTree() unknown case cannot find a server and not node !")
                    }
                } else {

                    log.debug("saveApacheTree() Search searchServer in nodes with appServer:" + appBean.appServer + " and appBean.appPort:" + appBean.appPort)
                    searchServer = Server.findByNameAndPortNumber(appBean.appServer, appBean.appPort.toInteger())
                    if (searchServer == null) {
                        log.debug("saveApacheTree() no searchServer found.Save it under treeNodeParent")
                        node = treeNodeData.saveChild(treeNodeParent, appBean)

                    } else {
                        log.debug("saveApacheTree() searchServer still exists")
                        node = treeNodeData.searchNode(searchServer)
                    }
                    treeNodeData.saveChild(node, server)
                }
            }
            //treeNodeData.showNode(myApp)
        }
    }
}
