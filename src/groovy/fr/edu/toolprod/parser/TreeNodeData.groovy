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
        node.port = "80"
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
        log.info("searchNode() Search node for  :" + searchServer.toString())
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
    def saveServerChild(TreeNode childNode, Server server) {
        log.info("saveApacheServerChild(childNode, server) ===> Save Server child :" + server?.toString() + " under node:" + childNode?.nodeData?.name)
        TreeNode resultNode = TreeNode.findByNameAndPort(server.name, server.portNumber.toString())
        if (resultNode == null) {
            resultNode = childNode.addChild(server)
            resultNode.name = server.name
            resultNode.port = server.portNumber
            resultNode.save(failOnError: true)
        }
        return resultNode
    }

    /**
     * Save a child by creating a server with appBean parameter.
     */
    def saveApacheServerChild(TreeNode treeNodeParent, AppBean appBean) {
        log.info("saveApacheServerChild(treeNodeParent, appBean) AppBean to save() :" + appBean.toString())
        Server serverChild = new Server(appBean.appServer, appBean.appPort, appBean.appServer)
        serverChild.serverType=Server.TYPE.APACHE
        serverChild.save(failOnError: true)
        log.info("saveApacheServerChild() Save Server child :" + serverChild.toString() + " under treeNodeParent node:" + treeNodeParent?.nodeData?.name )

        TreeNode resultNode = TreeNode.findByNameAndPort(serverChild.name, serverChild.portNumber.toString())
        if (resultNode == null) {
            resultNode = treeNodeParent.addChild(serverChild)
            resultNode.parent = treeNodeParent
            resultNode.name = serverChild.name
            resultNode.port = serverChild.portNumber
            resultNode.save(failOnError: true)
        }
        return resultNode
    }

    /**
     * Save a child by creating a server with appBean parameter.
     */
    def saveWeblogicServerChild(TreeNode treeNodeParent, AppBean appBean) {
        log.info("saveApacheServerChild(treeNodeParent, appBean) AppBean to save() :" + appBean.toString())
        Server serverChild = new Server(appBean.appServer, appBean.appPort, appBean.appServer)
        serverChild.serverType=Server.TYPE.WEBLOGIC
        serverChild.save(failOnError: true)
        log.info("saveApacheServerChild() Save Server child :" + serverChild.toString() + " under treeNodeParent node:" + treeNodeParent?.nodeData?.name )

        TreeNode resultNode = TreeNode.findByNameAndPort(serverChild.name, serverChild.portNumber.toString())
        if (resultNode == null) {
            resultNode = treeNodeParent.addChild(serverChild)
            resultNode.parent = treeNodeParent
            resultNode.name = serverChild.name
            resultNode.port = serverChild.portNumber
            resultNode.save(failOnError: true)
        }
        return resultNode
    }

    def saveApacheTree(App myApp, AppBean appBean, Server server) {
        TreeNodeData treeNodeData = new TreeNodeData()
        TreeNode treeNodeParent = treeNodeData.createParentNodeForApp(myApp, appBean.name)

        if (treeNodeParent.isLeaf()) {
            log.info("saveApacheTree() Child does not exists for treeNodeParent with name:" + treeNodeParent?.nodeData?.name + " and  port:" + treeNodeParent?.nodeData?.name)
            TreeNode childNode
            if (appBean.appServer != null) {//If appBean.appServer != null, it is a link to an other apache
                childNode = treeNodeData.saveApacheServerChild(treeNodeParent, appBean)
            } else {
                childNode = treeNodeParent
            }
            treeNodeData.saveServerChild(childNode, server)
            myApp.node.save(failOnError: true)
            myApp.save(failOnError: true)

        } else {
            log.info("saveApacheTree() Childs exist.Need to search where to add the node ...")
            TreeNode node
            if (appBean.appServer == null ) {
                log.debug("saveApacheTree() appBean.appServer == null => we are in local server")
                treeNodeData.saveServerChild(treeNodeParent, server)    // we are on the local server
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
                        log.info("Search if server still exist name:" + appBean?.appServer + " port:" + appBean?.appPort)
                        searchServer = Server.findByNameAndPortNumber(appBean.appServer, appBean.appPort.toInteger())
                        if (searchServer == null) {
                            log.debug("saveApacheTree() no searchServer found with name=" + appBean.appServer + " port:" +  appBean.appPort + ".Save it under treeNodeParent")
                            node = treeNodeData.saveApacheServerChild(treeNodeParent, appBean)

                        } else {
                            log.debug("saveApacheTree() searchServer still exists")
                            node = treeNodeData.searchNode(searchServer)
                        }
                        sNode.parent = node
                        sNode.save()
                    } else {
                        TreeNode childNode
                        if (appBean.appServer != null) {//If appBean.appServer != null, it is a link to an other apache
                            childNode = treeNodeData.saveApacheServerChild(treeNodeParent, appBean)
                        } else {
                            childNode = treeNodeParent
                        }
                        treeNodeData.saveServerChild(childNode, server)
                        myApp.node.save(failOnError: true)
                        myApp.save(failOnError: true)
                    }
                } else {

                    log.debug("saveApacheTree() Search searchServer in nodes with appServer:" + appBean.appServer + " and appBean.appPort:" + appBean.appPort)
                    searchServer = Server.findByNameAndPortNumber(appBean.appServer, appBean.appPort.toInteger())
                    if (searchServer == null) {
                        log.debug("saveApacheTree() no searchServer found.Save it under treeNodeParent")
                        node = treeNodeData.saveApacheServerChild(treeNodeParent, appBean)

                    } else {
                        log.debug("saveApacheTree() searchServer still exists")
                        node = treeNodeData.searchNode(searchServer)
                    }
                    treeNodeData.saveServerChild(node, server)
                }
            }
            //treeNodeData.showNode(myApp)
        }
    }

    def saveWebloTree(App myApp, AppBean appBean, Server server, List<Server> webloServers) {
        TreeNodeData treeNodeData = new TreeNodeData()
        TreeNode treeNodeParent = treeNodeData.createParentNodeForApp(myApp, appBean.name)


        if (treeNodeParent.isLeaf()) {
            log.info("saveWebloTree() Child does not exists for treeNodeParent with name:" + treeNodeParent?.nodeData?.name + " and  port:" + treeNodeParent?.nodeData?.name)
            TreeNode childNode
            if (appBean.appServer != null) {//If appBean.appServer != null, it is a link to an other apache
                childNode = treeNodeData.saveWeblogicServerChild(treeNodeParent, appBean)
            } else {
                childNode = treeNodeParent
            }
            TreeNode webloNode = treeNodeData.saveServerChild(childNode, server)
            for (Server webloServer : webloServers) {
                treeNodeData.saveServerChild(webloNode, webloServer)
            }
            myApp.node.save(failOnError: true)
            myApp.save(failOnError: true)

        } else {
            log.info("saveWebloTree() Childs exist.Need to search where to add the node ... TODO")
            TreeNode node
            if (appBean.appServer == null ) {
                TreeNode webloNode = treeNodeData.saveServerChild(treeNodeParent, server)    // we are on the local server ( e.g = appliloc )
                for (Server webloServer : webloServers) {
                    treeNodeData.saveServerChild(webloNode, webloServer)
                }
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
                            log.debug("saveWebloTree() no searchServer found with name=" + appBean.appServer + " port:" +  appBean.appPort + ".Save it under treeNodeParent")
                            node = treeNodeData.saveServerChild(treeNodeParent, server)
                            for (Server webloServer : webloServers) {
                                treeNodeData.saveServerChild(node, webloServer)
                            }

                        } else {
                            log.debug("saveWebloTree() searchServer still exists")
                            node = treeNodeData.searchNode(searchServer)
                            for (Server webloServer : webloServers) {
                                treeNodeData.saveServerChild(node, webloServer)
                            }
                        }
                        sNode.parent = node
                        sNode.save()
                    } else {
                        log.warn("saveWebloTree() unknown case cannot find a server and not node !")
                    }
                } else {

                    log.debug("saveWebloTree() Search searchServer in nodes with appServer:" + appBean.appServer + " and appBean.appPort:" + appBean.appPort)
                    searchServer = Server.findByNameAndPortNumber(appBean.appServer, appBean.appPort.toInteger())
                    if (searchServer == null) {
                        log.debug("saveWebloTree() no searchServer found.Save it under treeNodeParent")
                        node = treeNodeData.saveApacheServerChild(treeNodeParent, appBean)
                        for (Server webloServer : webloServers) {
                            treeNodeData.saveServerChild(node, webloServer)
                        }
                    } else {
                        log.debug("saveWebloTree() searchServer still exists")
                        node = treeNodeData.searchNode(searchServer)
                        for (Server webloServer : webloServers) {
                            treeNodeData.saveServerChild(node, webloServer)
                        }
                    }
                    treeNodeData.saveServerChild(node, server)
                }
            }
            treeNodeData.showNode(myApp)
        }
    }
}
