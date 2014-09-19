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
 * This class will contains data methods usefull after parsing files.
 * User: drieu
 * Date: 16/09/14
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
class Data {

    private static final log = LogFactory.getLog(this)

    private Machine machine

    def result = ''


    Data(Machine machine) {
        this.machine = machine

    }
/**
     * Save weblogic server and weblogic application in database.
     * @param weblos List<String>
     * @param appBean AppBean
     */
    def saveWeblo(List<String> weblos, AppBean appBean) {
        if ((weblos == null) || (appBean == null)) {
            throw new IllegalArgumentException("Bad parameter fot saveWeblo() method !")
        }
        log.info("saveWeblo() weblos:" + weblos.toString() + " appBean:" + appBean.toString())

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

        log.info("saveWeblo() App find or create:" + app)
        for(String str : weblos) {

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
                log.info("saveWeblo() Server find or create:" + server)


                Machine machine = Machine.findOrCreateByName(machinName)
                machine.addApplication(app)
                machine.addServer(server)
                machine.save(failOnError: true)
                log.info("saveWeblo() Machine find or create:" + machine)

                app.addServer(server)
                app.save(failOnError: true)
            }
        }
    }


    /**
     * Save parsing data
     * @param serverBean
     * @param appBeans
     * @return
     */
    def saveParsingData(ServerBean serverBean, List<AppBean> appBeans) {
        log.info("ServerBean info:" + serverBean.toString());
        String port = 80
        boolean bResult = true

        // Create Server
        Server server;
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

            log.info("Number of application found in this file :" + appBeans.size())

            saveAppBean(appBeans, server)
        }
        return bResult;
    }

    /**
     * Save a List of AppBean found in parse file.
     * @param appBeans List<AppBean>
     * @param server Server
     */
    def saveAppBean(List<AppBean> appBeans, Server server) {

        if (appBeans == null) {
            throw new IllegalArgumentException("saveAppBean() appBeans must not be null ! ")
        }

        if (server == null) {
            throw new IllegalArgumentException("saveAppBean() server must not be null ! ")
        }

        for (AppBean appBean : appBeans) {
            log.debug("saveAppBean() appBean:" + appBean)
            App myApp = App.findOrCreateByNameAndDescriptionAndUrl(appBean.name, appBean.description, appBean.serverUrl)
            myApp.addServer(server)
            for (String portalName: appBean.portals) {
                Portal portal = Portal.findByName(portalName)
                if (!myApp.portals.contains(portal)) {
                    myApp.portals.add(portal)
                }
            }
            log.debug("saveAppBean() app:" + appBean)
            myApp.save(failOnError: true)
            result = result + appBean.name + "\n"

            if (myApp != null) {
                if (!server.linkToApps.contains(appBean.name)) {
                    log.debug("Save application:" + appBean.name + " in the app list of web server:" + server.name )
                    server.addToLinkApps(appBean.name);
                    server.save(failOnError: true);
                } else {
                    log.debug("Nothing to save application:" + appBean.name + " still exist in the app list of web server:" + server.name )
                }

                // Add to the machine app list only if it's a local application ( same name of machine in URL )
                if (myApp.url.contains(machine.name)) {
                    machine.addApplication(myApp)
                }
                machine.addServer(server)
                if (!machine.save(failOnError: true)) {
                    log.error("Can't Save machine " + machine)
                } else {
                    log.info("Save machine OK:" + machine)
                }
            }
        }
    }

    def saveCheck(String machineName, String fileName, String confServerName) {
        if (machineName != null && fileName != null && confServerName!= null) {
            Check check = Check.findOrCreateByMachineNameAndFileNameAndConfServerName(machineName, fileName, confServerName)
            check.save(failOnError: true)
        }


    }

}
