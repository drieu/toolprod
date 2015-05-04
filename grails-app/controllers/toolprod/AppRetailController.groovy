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
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font

class AppRetailController {

    // Export service provided by Export plugin
    def exportService
    def grailsApplication  //inject GrailsApplication

    /**
     * We backup the choice of the user because when you click on pdf, you can't pass portal.
     */
    def backupChoice

    def pdfRenderingService

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
     * Html print page.
     */
    def viplisting() {

        List<String> results = new ArrayList<>()
        def servers = Server.findAll()
        for (Server server : servers) {
            if (!results.contains(server.machineHostName) && !server.machineHostName.startsWith("source_")) {
                def m = server.machineHostName =~ /web[0-9]/
                if (m) {
                    results.add(server.machineHostName)
                }
            }
        }
        results.sort()

       [ servers: results]
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

    /**
     * Render a pdf with a list of applications.
     */
    def renderFormPDF(){
        log.info("renderFormPDF()")
        List<App> apps = new ArrayList<>()
        String title = ""

        String param = params.get("select").toString()
        log.info("hidden value select is :" + param)

        if (param == null) {
            title= "Liste de toutes les applications"
            apps = App.findAll()

        } else if (param.equals("vip")) {
            final String nameParam = params.get("vipSelect")
            Vip vip= Vip.findByTechnicalName(nameParam)
            apps=getApps(vip.servers)
            title= "Liste de toutes les applications sur " + nameParam

        } else if (param.equals("server")) {
            final String nameParam = params.get("serverSelect")
            List<Server> servers = Server.findAllByMachineHostName(nameParam)
            apps=getApps(servers)
            title= "Liste de toutes les applications sur " + nameParam

        }
        title += " (" + apps.size() + ")"

        // Create pdf document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        PDPageContentStream contentStream;

        int pageNumber = 1
        final int APPNUMBER_BY_SIZE = 30
        if (apps.size() > APPNUMBER_BY_SIZE) {
            pageNumber = apps.size()/APPNUMBER_BY_SIZE + 1
        }
        log.debug("Apps size :" + apps.size())
        log.debug("Page number :" + pageNumber)

        int countApp = 0
        int max = APPNUMBER_BY_SIZE
        if (apps.size() < APPNUMBER_BY_SIZE) {
            max = apps.size()
        }
        if (apps.size() > 0) {
            for (int i=1; i<=pageNumber; i++) {
                page = new PDPage();
                contentStream = new PDPageContentStream(document, page);

                max = countApp + APPNUMBER_BY_SIZE
                if (max > apps.size()) {
                    max = apps.size() - 1
                }

                drawTable(page, contentStream, 700, 100, apps[countApp..max], title);
                contentStream.close();
                document.addPage(page);
                int num = countApp + APPNUMBER_BY_SIZE + 1
                if (num < apps.size()) {
                    countApp = countApp + APPNUMBER_BY_SIZE + 1
                } else {
                    break;
                }
            }
        }

        document.save("report.pdf");
        document.close();
        render( file:new File("report.pdf"), fileName: "report.pdf")
    }

    /**
     * Get a list of App for servers
     * @param servers
     * @return
     */
    private List<App> getApps(List<Server> servers) {
        List<App> apps = new ArrayList()
        if (servers != null) {
            for(Server server : servers ) {
                log.debug("Server name :" + server?.name)
                for(String name : server.linkToApps) {
                    App app = App.findByName(name)
                    if (app != null) {
                        if (!apps.contains(app)) {
                            apps.add(app)
                        }
                    } else {
                        log.error("Nothing found for " + name)
                    }
                }
            }
        }
        return apps

    }

    /**
     * Draw pdf table with list of App.
     * @param page
     * @param contentStream
     * @param y the y-coordinate of the first row
     * @param margin the padding on left and right of table
     * @param content a 2d array containing the table data
     * @throws IOException
     */
    def drawTable(PDPage page, PDPageContentStream contentStream,
                                 float y, float margin,
                                 List<App> apps, String title) throws IOException {
        final int rows = apps.size() + 1;
        final int cols = 2;
        final float rowHeight = 20f;
        final float tableWidth = page.findMediaBox().getWidth()-(2*margin);
        final float tableHeight = rowHeight * rows;
        final float colWidth = tableWidth/(float)cols;
        final float cellMargin=4f;

        //draw the rows
        float nexty = y ;
        for (int i = 0; i <= rows; i++) {
            contentStream.drawLine(margin,nexty,(float)(margin+tableWidth),nexty);
            nexty-= rowHeight;
        }

        //draw the columns
        float nextx = margin;
        for (int i = 0; i <= cols; i++) {
            contentStream.drawLine(nextx,y,nextx,(float)(y-tableHeight));
            nextx += colWidth;
        }

        //now add the text
        contentStream.setFont(PDType1Font.HELVETICA_BOLD,14);
        contentStream.beginText();
        contentStream.moveTextPositionByAmount((float)margin+cellMargin+10,(float)(y+20));
        contentStream.drawString(title);
        contentStream.endText();


        contentStream.setFont(PDType1Font.HELVETICA_BOLD,12);

        float textx = margin+cellMargin;
        float texty = y-15;

        //Define colunm title
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString("Nom");
        contentStream.endText();
        textx += colWidth;

        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString("Description");
        contentStream.endText();
        textx += colWidth;

//        contentStream.beginText();
//        contentStream.moveTextPositionByAmount(textx,texty);
//        contentStream.drawString("Chemin dans ARENA");
//        contentStream.endText();
//        textx += colWidth;

        texty-=rowHeight;
        textx = margin+cellMargin;

        apps.each {

            contentStream.setFont(PDType1Font.HELVETICA,9);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(textx,texty);
            contentStream.drawString(it.name);
            contentStream.endText();
            textx += colWidth;

            contentStream.setFont(PDType1Font.HELVETICA,8);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(textx,texty);
            String desc = it.description
            if (desc.equals("EMPTY")) {
                desc = ""
            }
            contentStream.drawString(desc);
            contentStream.endText();
            textx += colWidth;

//            contentStream.setFont(PDType1Font.HELVETICA,6);
//            contentStream.beginText();
//            contentStream.moveTextPositionByAmount(textx,texty);
//
//            String path = it.arenaPath
//            if (path == null) {
//                path = ""
//            } else {
//                String str = ""
//                StringTokenizer tokenizer = new StringTokenizer(path);
//                float pos = texty
//                while (tokenizer.hasMoreTokens())
//                {
//                    str = str + tokenizer.nextToken("/")
//                    str += "\n"
//                    contentStream.drawString(str);
//                    pos = pos -5
//                    contentStream.moveTextPositionByAmount(textx,(float)(pos));
//                }
//
//            }
//
//            contentStream.endText();
//            textx += colWidth;

            texty-=rowHeight;
            textx = margin+cellMargin;
        }
    }
}
