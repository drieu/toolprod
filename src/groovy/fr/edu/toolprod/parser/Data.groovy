package fr.edu.toolprod.parser

import fr.edu.toolprod.bean.AppBean
import fr.edu.toolprod.bean.ServerBean
import org.apache.commons.logging.LogFactory
import toolprod.App
import toolprod.Check
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
                if ((appBean.weblos != null) && (appBean.weblos.size()>0)) {
                    saveWebloApp(appBean)
                } else {
                    saveApacheApp(appBean)
                }
                addAppToServer(appBean)
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
            server.save(failOnError: true);
        } else {
            log.debug("addAppToServer() : Nothing to save application:" + appBean.name + " still exist in the app list of web server:" + server.name )
        }

        // Add to the machine app list only if it's a local application ( same name of machine in URL )
        if (appBean.serverUrl.contains(machine.name)) {
            machine.addAppBean(appBean)
        }
        machine.addServer(server)
        if (!machine.save(failOnError: true)) {
            log.error("addAppToServer() : Can't Save machine " + machine)
        } else {
            log.info("addAppToServer() : Save machine OK:" + machine)
        }
    }


    /**
     * Save server.
     * @param serverBean
     * @return
     */
    def saveServer(ServerBean serverBean) {
        boolean bResult = true
        Integer port = 80
        log.info("ServerBean info:" + serverBean.toString());

        if ( (serverBean.name == null)) {
            log.warn("No existing server name found.Create Default server APACHE with name :" + machine.name)
            server = new Server(name:machine.name, machineHostName: machine.name, portNumber: port, serverType: Server.TYPE.APACHE )
            server.save(failOnError: true)
        } else {
            server = Server.saveServer(serverBean)
        }

        if (server == null) {
            result += "Import du fichier impossible: Pas de serveur web Apache associé au fichier."
            bResult = false;
        } else {
            if (!machine.getServers()?.contains(server)) {
                machine.addServer(server);
                machine.save();
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
        App myApp = App.findOrCreateByNameAndDescriptionAndUrl(appBean.name, appBean.description, appBean.serverUrl)
        log.info("saveApacheApp() ==> save server:" + server.name)
        myApp.addServer(server)
        for (String portalName: appBean.portals) {
            Portal portal = Portal.findByName(portalName)
            if (!myApp.portals.contains(portal)) {
                myApp.portals.add(portal)
            }
        }
        log.debug("saveApacheApp() app:" + appBean)
        myApp.save(failOnError: true)
        result = result + appBean.name + " "
    }

/**
     * Save weblogic server and weblogic application in database.
     * @param appBean AppBean
     */
    def saveWebloApp(AppBean appBean) {
        log.info("saveWebloApp() weblos:" + appBean.weblos.toString() + " appBean:" + appBean.toString())

        App app = App.findOrCreateByNameAndDescriptionAndUrl(appBean.name, appBean.description, appBean.serverUrl);
        //Save portals in application
        for (String str: appBean.portals) {
            if (!app.portals.contains(str)) {
                Portal portal = Portal.findByName(str)
                if (portal != null) {
                    app.portals.add(portal)
                }
            }
        }
        app.save(failOnError: true)

        log.info("saveWebloApp() App find or create:" + app)
        for(String str : appBean.weblos) {

            def params = str.tokenize(":")
            log.info(params.toString())
            if (params.size() == 2) {
                String machinName = params.get(0)
                String portTest = params.get(1)

                Server server = Server.findOrCreateByNameAndPortNumberAndServerTypeAndMachineHostName(machinName,portTest,Server.TYPE.WEBLOGIC, machinName)
                if (!server.linkToApps.contains(appBean.name)) {
                    server.addToLinkApps(appBean.name)
                }
                server.save(failOnError: true)
                log.info("saveWebloApp() Server find or create:" + server)


                Machine machine = Machine.findOrCreateByName(machinName)
                machine.addApplication(app)
                machine.addServer(server)
                machine.save(failOnError: true)
                log.info("saveWebloApp() Machine find or create:" + machine)

                app.addServer(server)
                app.save(failOnError: true)
                result = result + app.name + " "
            }
        }
    }

    def static saveCheck(String machineName, String fileName, String confServerName) {
        if (machineName != null && fileName != null && confServerName!= null) {
            Check check = Check.findOrCreateByMachineNameAndFileNameAndConfServerName(machineName, fileName, confServerName)
            check.save(failOnError: true)
        }
    }

}
