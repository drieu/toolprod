package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.AppBean
import org.apache.commons.logging.LogFactory
import toolprod.App
import toolprod.Server
import toolprod.TreeNode

/**
 * Class used to save apache Node in tree.
 */
class ApacheTreeNodeData extends TreeNodeData {


    private static final log = LogFactory.getLog(this)

    private static final int DEFAULT_PORT = 80

    ApacheTreeNodeData(String suffix) {
        super(suffix)
    }

    @Override
    TreeNode saveServerChild(TreeNode treeNodeParent, AppBean appBean) {
        log.info("saveApacheServerChild(treeNodeParent, appBean) AppBean to save() :" + appBean.toString())
        Server serverChild = new Server(appBean.appServer, appBean.appPort, appBean.appServer)
        serverChild.serverType=Server.TYPE.APACHE
        serverChild.save(failOnError: true, flush:true)
        log.info("saveApacheServerChild() Save Server child :" + serverChild.toString() + " under treeNodeParent node:" + treeNodeParent?.nodeData?.name )

        String nodeName = buildNodeName(suffixNodeName, treeNodeParent?.nodeData?.name, serverChild.name)
        TreeNode resultNode = saveNode(treeNodeParent, serverChild, nodeName, serverChild.portNumber.toString())
        return resultNode
    }
/**
     * Save Tree in case of a leaf.
     * @param treeNodeParent
     * @param app
     * @param appBean
     * @param serv
     * @return
     */
    public TreeNode saveTreeLeaf(TreeNode treeNodeParent, App app, AppBean appBean, Server serv) {
        TreeNode childNode = null
        log.info("saveApacheTreeLeaf() ( APACHE ) Case of a leaf : Child does not exists for treeNodeParent")
        if (treeNodeParent == null) {
            log.warn("saveApacheTreeLeaf() TreeNode parameter is null.Cannot save.")
            return childNode
        }
        if (app == null) {
            log.warn("saveApacheTreeLeaf() App parameter is null.Cannot save.")
            return childNode
        }
        if (serv == null) {
            log.warn("saveApacheTreeLeaf() Server parameter is null.Cannot save.")
            return childNode
        }

        log.info("saveApacheTreeLeaf() Child does not exists for treeNodeParent with name:" + treeNodeParent?.nodeData?.name + " and  port:" + treeNodeParent?.nodeData?.name)

        if (appBean.appServer != null) {//If appBean.appServer != null, it is a link to an other apache
            childNode = this.saveServerChild(treeNodeParent, appBean)
        } else {
            log.info("saveApacheTreeLeaf() Init childNode with treeNodeParent:" + treeNodeParent)
            childNode = treeNodeParent
        }
        this.saveServerChild(childNode, serv)
        app.node.save(failOnError: true, flush:true)
        app.save(failOnError: true, flush:true)

        return childNode
    }

    /**
     * Save Tree for apache application.
     * @param myApp
     * @param appBean
     * @param server
     */
    public void saveTree(App myApp, AppBean appBean, Server server) {
        log.info("saveApacheTree()")
        TreeNode treeNodeParent = this.createParentNodeForApp(myApp, appBean.name)

        if (treeNodeParent.isLeaf()) {
            saveTreeLeaf(treeNodeParent, myApp, appBean, server)

        } else {
            log.info("saveApacheTree() Childs exist.Need to search where to add the node ...")
            TreeNode node
            if (appBean.appServer == null ) {
                log.debug("saveApacheTree() appBean.appServer == null => we are in local server")
                this.saveServerChild(treeNodeParent, server)    // we are on the local server
            } else {
                if (appBean.getAppPort() == null) {
                    appBean.setAppPort(DEFAULT_PORT)
                }
                int portDefault = server.getPortNumber()
                Server searchServer = Server.findByNameAndPortNumber(server.name, portDefault)
                if ( searchServer != null ) {
                    TreeNode sNode = this.searchNode(server, searchServer, appBean.name)
                    if ( sNode != null ) {
                        log.info("Search if server still exist name:" + appBean?.appServer + " port:" + appBean?.appPort)
                        Server searchAppBeanServer = Server.findByNameAndPortNumber(appBean.appServer, appBean.appPort.toInteger())
                        if (searchAppBeanServer == null) {
                            log.debug("saveApacheTree() no searchServer found with name=" + appBean.appServer + " port:" +  appBean.appPort + ".Save it under treeNodeParent")
                            node = this.saveServerChild(treeNodeParent, appBean)

                        } else {
                            log.debug("saveApacheTree() searchServer still exists")
                            // TODO create server with appBean.appServer and appBean.appPort.toInteger()
                            node = this.searchNode(server, searchAppBeanServer, appBean.name)
                        }
                        sNode.parent = node
                        sNode.save(failOnError: true, flush:true)
                    } else {
                        TreeNode childNode
                        if (appBean.appServer != null) {//If appBean.appServer != null, it is a link to an other apache
                            childNode = this.saveServerChild(treeNodeParent, appBean)
                        } else {
                            childNode = treeNodeParent
                        }
                        this.saveServerChild(childNode, server)
                        myApp.node.save(failOnError: true, flush:true)
                        myApp.save(failOnError: true, flush:true)
                    }
                } else {

                    log.debug("saveApacheTree() Search searchServer in nodes with appServer:" + appBean.appServer + " and appBean.appPort:" + appBean.appPort)
                    searchServer = Server.findByNameAndPortNumber(appBean.appServer, appBean.appPort.toInteger())
                    if (searchServer == null) {
                        log.debug("saveApacheTree() no searchServer found.Save it under treeNodeParent")
                        node = this.saveServerChild(treeNodeParent, appBean)

                    } else {
                        log.debug("saveApacheTree() searchServer still exists")
                        node = this.searchNode(server, searchServer, appBean.name)
                    }
                    this.saveServerChild(node, server)
                }
            }
            //showNode(myApp)
        }
    }
}
