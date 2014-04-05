import toolprod.App
import toolprod.Machine
import toolprod.Server

class BootStrap {

    def init = { servletContext ->

        createWebLogicData("weblo.ac-test.fr", "app1", Server.TYPE.WEBLOGIC, "8080", "ts", "Teleservice", "http://teleservices.ac-limoges.fr/ts"  )
        createWebLogicData("web3.ac-limoges.fr", "app2", Server.TYPE.APACHE, "80", "app2", "Teleservice", "http://app2.ac-limoges.fr/app2"  )

    }

    /**
     * Create Test data at startUp.
     * @param machineName
     * @param serverName
     * @param portNumber
     * @param appName
     * @param appDesc
     * @param appUrl
     * @return
     */
    def createWebLogicData(String machineName, String serverName, Server.TYPE myType, String portNumber, String appName, String appDesc, String appUrl) {
        Machine machine = new Machine(name: machineName, ipAddress: "127.0.0.1")
        Server server = new Server(name: serverName, serverType: myType, portNumber: portNumber)
        server.save();

        App tsApp = new App(name: appName, description: appDesc, url: appUrl )
        if (!tsApp.save()) {
            log.error("Bootstrap : Can't save ts App")
        } else {
            log.info("Bootstrap : Save ts App OK.")
        }
        List servers= new ArrayList();
        servers.add(server);
        tsApp.servers = servers;

        Set apps = new HashSet();
        apps.add(tsApp);
        machine.apps = apps;
        machine.save()

    }


    def destroy = {
    }
}
