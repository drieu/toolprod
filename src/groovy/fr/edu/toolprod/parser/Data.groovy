package fr.edu.toolprod.parser

//import com.tree.TreeNode
//import com.tree.TreeNodeImpl
import fr.edu.toolprod.bean.AppBean
import fr.edu.toolprod.bean.ServerBean
import org.apache.commons.logging.LogFactory
import toolprod.App
import toolprod.Status
import toolprod.Machine
import toolprod.Portal
import toolprod.Server

/**
 * Data is used to save App parsed in httpd conf and the result of check method in database.
 */
class Data {

    private static final log = LogFactory.getLog(this)

    /**
     *  Machine which contains httpd conf file.
     */
    private Machine machine

    /**
     * Server for this httpd.conf.
     */
    private Server server;

    /**
     * Suffix for a node name.
     * It solves problem with 2 parents with same childs (eg: wappsco1:10206 et wappsco2:10206 )
     */
    private String suffixNodeName = ""

    /**
     * Result string to show on html page when it finished.
     */
    def result = ''

    Data(Machine machine) {
        this.machine = machine
    }

    /**
     * Main method for saving data.
     * @param serverBean
     * @param appBeans
     * @return false if an error occurs.
     */
    def save(ServerBean serverBean, List<AppBean> appBeans) {
        boolean bResult = saveServer(serverBean)
        if (bResult) {
            for (AppBean appBean : appBeans) {
                if (appBean.name != null) {
                    suffixNodeName = appBean.name
                    if ((appBean.weblos != null) && (appBean.weblos.size()>0)) {
                        saveWebloApp(appBean)
                    } else {
                        saveApacheApp(appBean)
                    }
                    addAppToServer(appBean)
                }
            }
        }
        return bResult;
    }

    /**
     * Add application into server ( local or referenced )
     * @param appBean AppBean
     * @return
     */
    def addAppToServer(AppBean appBean) {
        if (!server.linkToApps.contains(appBean.name)) {
            log.debug("addAppToServer() : Save application:" + appBean.name + " in the app list of web server:" + server.name )
            server.addToLinkApps(appBean.name);
            server.save(failOnError: true,flush:true);
        } else {
            log.debug("addAppToServer() : Nothing to save application:" + appBean.name + " still exist in the app list of web server:" + server.name )
        }

        // Add to the machine app list only if it's a local application ( same name of machine in URL )
        if (appBean.serverUrl.contains(machine.name)) {
            machine.addAppBean(appBean)
        }
        machine.addServer(server)
        if (!machine.save(failOnError: true,flush:true)) {
            log.error("addAppToServer() : Can't Save machine " + machine)
        } else {
            log.info("addAppToServer() : Save machine OK:" + machine)
        }
    }


    /**
     * Save server Apache on Machine
     * @param serverBean
     * @return
     */
    def saveServer(ServerBean serverBean) {
        boolean bResult = true
        Integer port = 80
        log.info("ServerBean info:" + serverBean.toString());

        if ( (serverBean.name == null)) {
            log.warn("No existing server name found.Create Default server APACHE with name :" + machine.name)
            Server searchServer = Server.findByNameAndPortNumber(machine.name, port)
            if (searchServer == null) {
                server = new Server(name:machine.name, machineHostName: machine.name, portNumber: port, serverType: Server.TYPE.APACHE )
                server.save(failOnError: true,flush:true)
            } else {
                server = searchServer
            }
        } else {
            server = Server.saveServer(serverBean)
        }

        if (server == null) {
            result += "Import du fichier impossible: Pas de serveur web Apache associé au fichier."
            bResult = false;
        } else {
            if (!machine.getServers()?.contains(server)) {
                machine.addServer(server);
                machine.save(failOnError: true, flush: true);
                log.info("Save OK server " + server.name + " in machine " + machine.name);
            }
        }
        return bResult
    }

    /**
     * Save apache application.
     * @param appBean
     * @return
     */
    def saveApacheApp(AppBean appBean) {
        log.debug("saveApacheApp() appBean:" + appBean)
        App myApp = App.findOrCreateByNameAndDescription(appBean.name, appBean.description)
        if (!myApp.urls.contains(appBean.serverUrl)) {
            myApp.urls.add(appBean.serverUrl)
        }
        log.debug("saveApacheApp() save server:" + server.name)
        if (!myApp.servers.contains(server)) {
            myApp.addServer(server)
        }

        log.debug("saveApacheApp() add portals ")
        for (String portalName: appBean.portals) {
            Portal portal = Portal.findByName(portalName)
            if (portal != null && !myApp.portals.contains(portal)) {
                myApp.portals.add(portal)
            }
        }

        log.info("saveApacheApp() save tree")
        TreeNodeData treeNodeData = new ApacheTreeNodeData(suffixNodeName)
        treeNodeData.saveTree(myApp, appBean, server)
        result = result + appBean.name + " "

    }


/**
     * Save weblogic server and weblogic application in database.
     * @param appBean AppBean
     */
    def saveWebloApp(AppBean appBean) {
        log.info("saveWebloApp() weblos:" + appBean.weblos.toString() + " appBean:" + appBean.toString())

        App app = App.findOrCreateByNameAndDescription(appBean.name, appBean.description);
        if (!app.urls.contains(appBean.serverUrl)) {
            app.urls.add(appBean.serverUrl)
            app.save(failOnError: true,flush:true)
        }
        //Save portals in application
        for (String str: appBean.portals) {
            if (!app.portals.contains(str)) {
                Portal portal = Portal.findByName(str)
                if (portal != null && !app.portals.contains(portal)) {
                    app.portals.add(portal)
                }
            }
        }

        app.save(failOnError: true,flush:true)

        log.info("saveWebloApp() App find or create:" + app)
        List<Server> webloServers = new ArrayList<>()
        for(String str : appBean.weblos) {

            def params = str.tokenize(":")
            log.info(params.toString())
            if (params.size() == 2) {
                String machinName = params.get(0)
                String portTest = params.get(1)

                Server webloServer = Server.findOrCreateByNameAndPortNumberAndServerTypeAndMachineHostName(machinName, portTest.toInteger(),Server.TYPE.WEBLOGIC, machinName)
                if (!webloServer.linkToApps.contains(appBean.name)) {
                    webloServer.addToLinkApps(appBean.name)
                }
                webloServer.save(failOnError: true,flush:true)
                log.info("saveWebloApp() Server " + webloServer)


                Machine machine = Machine.findOrCreateByName(machinName)
                machine.addApplication(app)
                machine.addServer(webloServer)
                machine.save(failOnError: true,flush:true)
                log.info("saveWebloApp() Machine find or create:" + machine)

                // Why equals method of webloServer cannot be call ???
                boolean bFind = false
                for (Server serv : app.servers) {
                    if (serv.name.equals(webloServer.name) && serv.portNumber.equals(webloServer.portNumber)) {
                        bFind = true
                        break
                    }
                }
                if (!bFind) {
                    log.info("saveWebloApp() add webloServer to the App list.")
                    app.addServer(webloServer)
                }

                app.save(failOnError: true,flush:true)
                webloServers.add(webloServer)
            }
        }

        log.info("saveWebloApp() save tree")
        TreeNodeData treeNodeData = new WeblogicTreeNodeData(suffixNodeName, webloServers)
        treeNodeData.saveTree(app, appBean, server)
        result = result + appBean.name + " "
    }

    def static saveCheck(String machineName, String fileName, String confServerName) {
        if (machineName != null && fileName != null && confServerName!= null) {
            Status check = Status.findOrCreateByMachineNameAndFileNameAndName(machineName, fileName, confServerName)
            check.save(failOnError: true,flush:true)
        }
    }

}
