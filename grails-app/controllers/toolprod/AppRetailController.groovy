package toolprod

import fr.edu.toolprod.bean.AppBean
import fr.edu.toolprod.bean.PrintAppBean
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
            data = createDataFromNode(myApp.node)
        }
        return [app:myApp, data:data]
    }

    /**
     * Produce String in JSON format
     * e.g : var zNodes={ ... }
     * @param node
     * @return String
     */
    private String createDataFromNode(TreeNode node) {
        String data = "var zNodes = [\n"
        for (TreeNode c : node.getChildren()) {
            data += "{\n"
            data +=  "name:'" + node.name + "',open:true,\n"
                data += createDataChildFromNode(c)
            data += "},\n"
        }
        data += "];"
        return data
    }

    private String createDataChildFromNode(TreeNode cbis) {
        String result = ""
        result += "\t\tchildren: [\n"
        log.info("NAME:" + cbis?.nodeData?.name)
        result += "                    { name: '" + cbis?.nodeData?.name + "',open:true,\n"
        log.info("SIZE:" + cbis.getChildren().size())
            if (cbis.getChildren().size() != 0) {
                result += "\t\tchildren: [\n"
                int count = cbis.getChildren().size()
                int cpt = 0
                for (TreeNode node : cbis.getChildren())  {
                    result +=  "{name:'" + node?.nodeData?.name + "'}\n"
                    if (cpt != count - 1) {
                        result += ","
                    }
                    cpt = cpt + 1
//                    result += createDataChildFromNode(node)
                }
                result += "\t\t]}\n"
            }
        result += "\t\t]\n"
        return result

    }



    private static String createIndent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(' ');
        }
        return sb.toString();
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

        log.info("AppRetailController:listing()")
        def portalChoice = params.choice
        if (portalChoice != null) {
            backupChoice = portalChoice
        }
        List<AppBean> appBeans = new ArrayList<>()
        def apps = App.findAll()

        for(App app : apps) {
           log.info(app.toString())
           AppBean appBean = new AppBean()
           appBean.name = app.name
           appBean.description = app.description
           appBean.serverUrls = app.urls
           appBean.portals = new ArrayList<>()

            for(Portal portal : app.portals) {
               if (portal != null) {
                   if (portal.name != null) {
                       if (portalChoice != null) {
                           if (portal.name.equals(portalChoice)) {
                               log.info("Add : name:" + appBean.name + " portail:" + portalChoice)
                                appBean.portals.add(portal.name)
                           }
                       } else {
                           appBean.portals.add(portal.name)
                       }
                   }
               }
           }
           appBeans.add(appBean)
        }
        List<PrintAppBean> printAppBeans = new ArrayList<>()
        for(AppBean appBean : appBeans) {
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
            if (backupChoice != null) {

                if (appBean.portals.contains(backupChoice)) {
                    printAppBeans.add(printAppBean)
                }
            } else {
                log.info("Ajout " + printAppBean.name)
                printAppBeans.add(printAppBean)
            }
        }

        if(!params.max) {
            params.max = 10
        }

        if ((params.extension != null)) {
            def format=params.extension
            if ("xls".equals(params.extension)) {
                format="excel"
            }
            if(format && format != "html"){
                response.contentType = grailsApplication.config.grails.mime.types[format]
                response.setHeader("Content-disposition", "attachment; filename=check.${params.extension}")
                List fields = ["name", "urls", "portals"]
                Map labels = ["name": "Nom", "urls": "url(s)", "portals":"Portail"]

                Map formatters = new HashMap()
                Map parameters = new HashMap()
                log.info("SIZE : " + printAppBeans.size())
                exportService.export(format, response.outputStream, printAppBeans, fields, labels, formatters, parameters)

            }
        }



        def portals = Portal.findAll().unique{ it.name }
        return [appBeans:appBeans, portals:portals, portalChoice:portalChoice]
    }

    def renderJSONOutput() {
        log.info("renderJSONOutput()")
//        def name = params.get("name")
//        if (name == null) {
//            log.warn("renderJSONOutput() No data")
//            render("Pas de données")
//        }
        def name = "aeronautique"
        App myApp = App.findByName(name)
        if (myApp != null) {
//            TreeNode node = myApp.node
//            log.info("====>Parent:" + node.toString())
//            for (TreeNode c : node.getChildren()) {
//                if (c != null && c.nodeData != null) {
//                    log.info("Child 1:" + c.nodeData.toString())
//                    for (TreeNode cbis : c.getChildren()) {
//                        if (c != null && c.nodeData != null) {
//                            log.info("Child 2:" + cbis.toString())
//                        }
//                    }
//                }
//            }
            def myHomeAddress = [
                    building:"25",
                    street: "High Street",
                    city:"Cambridge",
                    country:"UK",
                    pref: true]

            def myWorkAddress = [
                    building:"1",
                    street: "Science Park",
                    city:"Cambridge",
                    country:"UK"]

            def dave = [
                    name: "David Bower",
                    address: [myHomeAddress, myWorkAddress]]

            def people = [people:[dave]]
            //log.info("Render JSON")
//            people = "    {\n" +
//                    "        label: 'node1', id: 1,\n" +
//                    "        children: [\n" +
//                    "            { label: 'child1', id: 2 },\n" +
//                    "            { label: 'child2', id: 3 }\n" +
//                    "        ]\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "        label: 'node2', id: 4,\n" +
//                    "        children: [\n" +
//                    "            { label: 'child3', id: 5 }\n" +
//                    "        ]\n" +
//                    "    }"
            render people as JSON
        } else {
            //log.warn("renderJSONOutput()  No app found with name:" + name)
        }
        //log.info("renderJSONOutput() No data")
    }


}
