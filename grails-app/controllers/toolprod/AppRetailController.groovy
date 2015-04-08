package toolprod

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.edu.toolprod.bean.AppBean
import fr.edu.toolprod.bean.PrintAppBean
import fr.edu.toolprod.bean.ServerBean
import fr.edu.toolprod.gson.GSONBean
import fr.edu.toolprod.gson.GSONParser
import grails.converters.JSON
import org.apache.commons.lang.StringEscapeUtils

class AppRetailController {

    // Export service provided by Export plugin
    def exportService
    def grailsApplication  //inject GrailsApplication

    /**
     * We backup the choice of the user because when you click on pdf, you can't pass portal.
     */
    def backupChoice

    /**
     * Show details for an application
     * @return
     */
    def app() {
        def myApp = null
        String data
        def selectApp = params.get("name")
        if (selectApp != null) {
            myApp = App.findByName((String)selectApp)

            data += "\nvar zNodes = [\n"
            data += createTree(myApp.node)
            data += "\n];"
            log.info(data)
        }
//        Use to test
//        data += "\nvar zNodes = [\n"
//        data += createDataTest()
//        data += "\n];"
//        log.info(data)
        return [app:myApp, data:data]
    }

    public String createDataTest() {
        ServerBean serverBean

        ServerBean child
        ServerBean child2
        ServerBean child3

        ServerBean serverBeanChild
        ServerBean serverBeanChild2

        serverBean = new ServerBean()
        serverBean.name = "source_aeronautique"
        serverBean.portNumber = "80"

        child = new ServerBean()
        child.name = "appliloc6.ac-limoges.fr"
        child.portNumber = "8015"

        child2 = new ServerBean()
        child2.name = "appliloc7.ac-limoges.fr"
        child2.portNumber = "8016"

        child3 = new ServerBean()
        child3.name = "appliloc8.ac-limoges.fr"
        child3.portNumber = "8019"

        serverBeanChild = new ServerBean()
        serverBeanChild.name = "web1.ac-limoges.fr"
        serverBeanChild.portNumber = "8052"

        serverBeanChild2 = new ServerBean()
        serverBeanChild2.name = "web2.ac-limoges.fr"
        serverBeanChild2.portNumber = "8063"

        GSONBean gsonBean = new GSONBean()
        gsonBean.name = "source_aeronautique"

        gsonBean.nodeData = serverBean
        GSONBean nChild = gsonBean.addChild(child)
        GSONBean nChild2 = nChild.addChild(child2)
        GSONBean nChild3 = nChild2.addChild(child3)

        GSONBean sChild = gsonBean.addChild(serverBeanChild)
        GSONBean sChild2 = sChild.addChild(serverBeanChild2)


        GSONParser parser = new GSONParser()
        return parser.createTree(gsonBean)
    }

    /**
     * Generate GSON String from a TreeNode
     * @param node
     * @return GSON String e.g:{name:...
     */
    public String createTree(TreeNode node) {
       String data = ""
       if (node != null) {
           data += "{name:'" + getRealParentName(node?.nodeData?.name, node?.nodeData?.portNumber) + "',open:true"

           if (node.getChildren().size() != 0) {
               data += ", children:["

               int nbChild =  node.getChildren().size()
               int cpt = 0
               for (TreeNode child : node.getChildren()) {

                   if (child.getChildren().size() == 0) {
                       if (cpt == ( nbChild -1 )) { // if last
                           data += "{name:'" + child?.nodeData?.name + "_" + child?.nodeData?.portNumber  + "',open:true}"
                       } else {
                           data += "{name:'" + child?.nodeData?.name + "_" + child?.nodeData?.portNumber  + "',open:true},"
                       }
                   } else {
                       data += "\n"
                       data += createTree(child)
                       if (cpt != ( nbChild -1 )) {
                           data += ","
                       }
                       data += "\n"
                   }
                   cpt = cpt + 1
               }
               data += "]}"
           } else {
               data += "}"
           }
       }

       return data
    }

    public String getRealParentName(String name, Integer portNumber) {
       String result = "?"
       if (name != null) {
          if (name.contains("source_")) {
              result = name.substring("source_".size(), name.length())
          } else {
              result = name + "_" + portNumber.toString()
          }
       }
       return result
    }

    /**
     * Get all application to see their status.
     * TODO : asynchronous call.
     */
    def status() {
        def apps=App.findAll()
        return [apps:apps]
    }

    /**
     * Get a listing of all application.
     */
    def listing() {

        log.info("listing()")
//        def portalChoice = params.choice
//        if (portalChoice != null) {
//            backupChoice = portalChoice
//        }
//
//        List<AppBean> appBeans = new ArrayList<>()
//        List<PrintAppBean> printAppBeans = new ArrayList<>()
//        def apps = App.findAll()
//        for(App app : apps) {
//            log.debug("getAppBeans() : Application : " + app.toString())
//            AppBean appBean = new AppBean()
//            appBean.name = app.name
//            appBean.description = app.description
//            appBean.serverUrls = app.urls
//            appBean.portals = new ArrayList<>()
//
//            for(Portal portal : app.portals) {
//                if (portal != null) {
//                    if (portal.name != null) {
//                        if (portalChoice != null) {
//                            if (portal.name.equals(portalChoice)) {
//                                log.info("getAppBeans Add : name:" + appBean.name + " portail:" + portalChoice)
//                                appBean.portals.add(portal.name)
//                            }
//                        } else {
//                            appBean.portals.add(portal.name)
//                        }
//                    }
//                }
//            }
//            appBeans.add(appBean)
//            PrintAppBean printAppBean = getPrintAppBean()
//            if (backupChoice != null) {
//                if (appBean.portals.contains(backupChoice)) {
//                    printAppBeans.add(printAppBean)
//                }
//            } else {
//                log.debug("Ajout " + printAppBean.name)
//                printAppBeans.add(printAppBean)
//            }
//        }
//
//        if(!params.max) {
//            params.max = 10
//        }
//
//        if ((params.extension != null)) {
//            def format=params.extension
//            if ("xls".equals(params.extension)) {
//                format="excel"
//            }
//            if(format && format != "html"){
//                response.contentType = grailsApplication.config.grails.mime.types[format]
//                response.setHeader("Content-disposition", "attachment; filename=check.${params.extension}")
//                List fields = ["name", "urls", "portals"]
//                Map labels = ["name": "Nom", "urls": "url(s)", "portals":"Portail"]
//
//                Map formatters = new HashMap()
//                Map parameters = new HashMap()
//                log.info("SIZE : " + printAppBeans.size())
//                exportService.export(format, response.outputStream, printAppBeans, fields, labels, formatters, parameters)
//
//            }
//        }
//
//        def portals = Portal.findAll().unique{ it.name }
//        log.info("AppRetailController:listing() render()")

        def data = "\nvar dataSet = [\n"
        for(App p : App.findAll()) {
            String link = "<a href=/toolprod/appRetail/app?name=" + p.name + ">" + p.name + "</a>"
            String servs = ""
            for(Server serv:p.servers) {
                servs += serv.name + " "
            }
            String portals = ""
            String vips = ""
            for( String portal : p.portals) {
                vips += p.portals.toString()
            }

            data += "['" + link + "','" + vips + "','" + servs + "'],"
        }
        data += "\n];"
        log.info(data)
        def count = App.findAll().size()
        //return [appBeans:appBeans, portals:portals, portalChoice:portalChoice, data:data]
        return [count:count, data:data]
    }

    def ajaxApps() {
//        def results = App.findAll().toArray();
//        render(contentType: "text/json") {
//            results = array {
//                App.list().each {user->
//                    result "${user.id}" : "${user.name}"
//                }
//            }
//        }
    }

    /**
     * List Vip, Server and app
     * @return
     */
    def viplisting() {
        String data
        def choice = params.choice
        Vip vip = getVip(choice)
        if (vip != null) {
            //def servers = Server.findAllByNameAndPortNumber("web1.ac-limoges.fr", "8008")
            def servers = vip.servers

            data += "\nvar zNodes = [\n"
            for (Server server : servers) {
                List<TreeNode> nodes = TreeNode.findAllByNodeData(server)
                for(TreeNode node : nodes) {
                    TreeNode parent = this.getParent(node)
                    if (parent != null) {
                        log.info(parent.name)
                        data += createTree(parent)
                        data += ","
                    }
                }
            }
            data += "\n];"
            log.info(data)
        }
        List<String> portalNames = Vip.executeQuery("select name from Vip")


        log.info("names:" + portalNames.toString())
        return [data : data, portals: portalNames, vip: vip]
    }

    public TreeNode getParent(TreeNode node) {
        TreeNode result = node
        if (result != null) {
            if (result.parent != null) {
                result = getParent(result.parent)
            }
        }
        return result
    }

    def getVip(String choice) {
        log.info("getVip()")
        // Save VIP
        Vip vip = Vip.findByName("webclasseur")
        if ( vip == null ) {
            log.info("add vip()")
            vip = new Vip()
            vip.name = "webclasseur"
            vip.technicalName = "webclasseur.ac-limoges.fr_ssl"
            Server serv = Server.findByNameAndPortNumber("web1.ac-limoges.fr", "8062")
            if (serv == null) {
                serv = new Server()
                serv.name = "web1.ac-limoges.fr"
                serv.portNumber = 8062
                serv.machineHostName = "web1.ac-limoges.fr"
                serv.serverType = Server.TYPE.APACHE
                serv.save(failOnError: true, flush:true)
            }
            vip.servers.add(serv)
            Server serv2 = Server.findByNameAndPortNumber("web2.ac-limoges.fr", "8062")
            if (serv2 == null) {
                serv2 = new Server()
                serv2.name = "web2.ac-limoges.fr"
                serv2.portNumber = 8062
                serv2.machineHostName = "web2.ac-limoges.fr"
                serv2.serverType = Server.TYPE.APACHE
                serv2.save(failOnError: true, flush:true)
            }
            vip.servers.add(serv2)

            vip.save(failOnError: true, flush:true)
        }
        vip
    }


    def getPrintAppBean(AppBean appBean) {
        PrintAppBean printAppBean = new PrintAppBean()
        printAppBean.name = appBean.name


        for (String p : appBean.portals) {
            if (p != null) {
                printAppBean.portals += p
                printAppBean.portals += " "
            }
        }

        for(String url: appBean.serverUrls) {
            if (url != null) {
                printAppBean.urls += url
                printAppBean.urls += "\n"
            }
        }
        return printAppBean
    }


}
