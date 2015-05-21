package toolprod

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font

class AppRetailController {


    private static final String EMPTY = ""

    private static final String PARAM_NAME = "name"

    private static final String PREFIX_PARENT_NODE_NAME = "source_"

    /**
     * Show details for an application
     * @return
     */
    def app() {
        def myApp = null
        String data = EMPTY
        def selectApp = params.get(PARAM_NAME)
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

/**
    // Use to test
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
**/
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
          if (name.contains(PARAM_NAME)) {
              result = name.substring(PARAM_NAME.size(), name.length())
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

        def data = "\nvar dataSet = [\n"
        for(App p : App.findAll()) {
            String link = "<a href=/toolprod/appRetail/app?name=" + p.name + ">" + p.name + "</a>"
            String servs = EMPTY
            for(Server serv:p.servers) {
                servs += serv.name + " "
            }
            String vips = EMPTY
            for( String portal : p.vips) {
                Vip vip = Vip.findByTechnicalName(portal)
                if (vip != null) {
                    String vipname = vip?.name + "_" + vip?.type
                    vips += vipname
                    vips += " "
                }
            }

            data += "['" + link + "','" + vips + "','" + servs + "'],"
        }
        data += "\n];"
        log.info(data)
        def count = App.findAll().size()
        return [count:count, data:data]
    }

    /**
     * Html print page.
     */
    def viplisting() {

        List<String> results = new ArrayList<>()
        def servers = Server.findAll()
        for (Server server : servers) {
            if (!results.contains(server.machineHostName) && !server.machineHostName.startsWith(PARAM_NAME)) {
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

        PDPageContentStream contentStream;

        int pageNumber = 1
        final int APPNUMBER_BY_SIZE = 30
        if (apps.size() > APPNUMBER_BY_SIZE) {
            pageNumber = apps.size()/APPNUMBER_BY_SIZE + 1
        }
        log.debug("Apps size :" + apps.size())
        log.debug("Page number :" + pageNumber)

        int countApp = 0
        if (apps.size() > 0) {
            int max
            PDPage page
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
    private static List<App> getApps(List<Server> servers) {
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
