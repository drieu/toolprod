package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.AppBean
import toolprod.App
import toolprod.Server
import toolprod.TreeNode

/**
 * Created with IntelliJ IDEA.
 * User: drieu
 * Date: 06/02/15
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
class WeblogicTreeNodeData extends TreeNodeData {

    List<Server> webloServers = new ArrayList<>()

    private static final int DEFAULT_PORT = 80


    WeblogicTreeNodeData(String suffix) {
        super(suffix)
    }

    WeblogicTreeNodeData(String suffix, List<Server> webloServers) {
        super(suffix)
        this.webloServers = webloServers
    }

    @Override
    TreeNode saveServerChild(TreeNode treeNodeParent, AppBean appBean) {
        log.info("saveApacheServerChild(treeNodeParent, appBean) AppBean to save() :" + appBean.toString())
        Server serverChild = new Server(appBean.appServer, appBean.appPort, appBean.appServer)
        serverChild.serverType=Server.TYPE.WEBLOGIC
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
        log.info("saveTreeLeaf() ( WEBLOGIC ) Child does not exists for treeNodeParent with name:" + treeNodeParent?.nodeData?.name + " and  port:" + treeNodeParent?.nodeData?.portNumber)
        TreeNode parentNode = null
        TreeNode webloNode = null
        if (treeNodeParent == null) {
            log.warn("saveApacheTreeLeaf() TreeNode parameter is null.Cannot save.")
            return webloNode
        }
        if (app == null) {
            log.warn("saveApacheTreeLeaf() App parameter is null.Cannot save.")
            return webloNode
        }
        if (serv == null) {
            log.warn("saveApacheTreeLeaf() Server parameter is null.Cannot save.")
            return webloNode
        }

        if (appBean.appServer != null) {//If appBean.appServer != null, it is a link to an other apache
            parentNode = this.saveWeblogicServerChild(treeNodeParent, appBean)
        } else {
            parentNode = treeNodeParent
        }
        webloNode = this.saveServerChild(parentNode, serv)
        app.node.save(failOnError: true, flush:true)
        app.save(failOnError: true, flush:true)

        return webloNode
    }


    /**
     * Save server child for weblogic.
     * @param webloNode TreeNode
     * @param webloServers List<Server>
     */
    private void saveWeblogicServerChild(TreeNode webloNode, List<Server> webloServers) {
        for (Server webloServer : webloServers) {
            this.saveServerChild(webloNode, webloServer)
        }
    }

    /**
     * Save AppBean in a tree of node for weblogic.
     * @param myApp
     * @param appBean
     * @param server
     * @return
     */
    public void saveTree(App myApp, AppBean appBean, Server server) {
        log.info("saveTree() for weblogic")
        TreeNode treeNodeParent = this.createParentNodeForApp(myApp, appBean.name)
        if (treeNodeParent.isLeaf()) {
            TreeNode webloNode = saveTreeLeaf(treeNodeParent, myApp, appBean, server)
            saveWeblogicServerChild(webloNode, webloServers)

        } else {
            log.info("saveWebloTree() Childs exist.Need to search where to add the node")
            TreeNode node
            if (appBean.appServer == null ) {
                TreeNode webloNode = this.saveServerChild(treeNodeParent, server)    // we are on the local server ( e.g = appliloc )
                saveWeblogicServerChild(webloNode, webloServers)

            } else {
                if (appBean.getAppPort() == null) {
                    appBean.setAppPort(DEFAULT_PORT)
                }
                int portDefault = server.getPortNumber()

                Server searchServer = Server.findByNameAndPortNumber(server.name, portDefault)
                if ( searchServer != null ) {
                    TreeNode sNode = this.searchNode(searchServer)
                    if ( sNode != null ) {
                        node = saveChildNodeForServer(treeNodeParent, server, appBean.appServer, appBean.appPort)
                        saveWeblogicServerChild(node, webloServers)
                        sNode.parent = node
                        sNode.save(failOnError: true, flush:true)
                    } else {
                        log.warn("saveWebloTree() unknown case cannot find a server and not node !")
                    }

                } else {
                    node = saveChildNodeForAppBean(treeNodeParent, appBean)
                    saveWeblogicServerChild(node, webloServers)
                    this.saveServerChild(node, server)
                }
            }
            //this.showNode(myApp)
        }
    }

    /**
     *
     * @param treeNodeParent
     * @param appBean
     * @return
     */
    public TreeNode saveChildNodeForAppBean(TreeNode treeNodeParent, AppBean appBean) {
        TreeNode node = null
        if (appBean == null) {
            log.warn("saveChildNodeForAppBean() appBean is null.")
            return node
        }

        String appServer = appBean.appServer
        String appPort = appBean.appPort

        log.info("saveChildNodeForAppBean() Search searchServer in nodes with appServer:" + appServer + " and appBean.appPort:" + appPort)
        Server searchServer = Server.findByNameAndPortNumber(appServer, appPort.toInteger())
        if (searchServer == null) {
            log.info("saveChildNodeForAppBean() no searchServer found.Save it under treeNodeParent")
            node = this.saveServerChild(treeNodeParent, appBean)
        } else {
            log.info("saveChildNodeForAppBean() searchServer still exists")
            node = this.searchNode(searchServer)
        }
        return node
    }

    /**
     *
     * @param treeNodeParent
     * @param serv
     * @param appServer
     * @param appPort
     * @return
     */
    public TreeNode saveChildNodeForServer(TreeNode treeNodeParent, Server serv, String appServer, String appPort) {
        TreeNode node = null
        Server searchServer = Server.findByNameAndPortNumber(appServer, appPort.toInteger())
        if (searchServer == null) {
            log.debug("saveWebloTree() no searchServer found with name=" + appServer + " port:" +  appPort + ".Save it under treeNodeParent")
            node = this.saveServerChild(treeNodeParent, serv)
        } else {
            log.debug("saveWebloTree() searchServer still exists")
            node = this.searchNode(searchServer)
        }
        return node
    }
}
