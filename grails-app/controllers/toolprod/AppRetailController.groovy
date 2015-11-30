package toolprod

import com.cronutils.descriptor.CronDescriptor
import com.cronutils.htime.HDateTimeFormatBuilder
import com.cronutils.model.Cron
import com.cronutils.model.definition.CronDefinition
import com.cronutils.model.definition.CronDefinitionBuilder
import com.cronutils.model.time.ExecutionTime
import com.cronutils.parser.CronParser
import com.cronutils.model.CronType;
import com.cronutils.validator.CronValidator
import fr.edu.toolprod.gson.GSONParser
import org.apache.commons.logging.LogFactory
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

/**
 * AppRetailController.
 */
class AppRetailController {

    /**
     * Constant.
     */
    private static final String EMPTY = ""

    /**
     * name parameter.
     */
    private static final String PARAM_NAME = "name"

    /**
     * vip parameter passed through view.
     */
    private static final String PARAM_VIP = "vip"

    /**
     * vipSelect parameter passed through view.
     */
    private static final String PARAM_VIP_SELECT = "vipSelect"

    /**
     * vipSelect parameter passed through view.
     */
    private static final String PARAM_SERVER = "server"

    /**
     * select parameter passed through view.
     */
    private static final String PARAM_SELECT = "select"

    /**
     * Report file name.
     */
    private static final String REPORT_FILE = "report.pdf"

    private List<String> chartConfig

    /**
     * Logger.
     */
    private static final log = LogFactory.getLog(this)

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
            if (myApp != null) {
                GSONParser parser = new GSONParser()
                data += parser.createTree(myApp.node)
            }
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
                servs += serv?.machineHostName + " "
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
            for(Server serv:p.servers) {
                if ( serv != null) {
                    data += "['" + link + "','" + vips + "','" + serv?.machineHostName + "','" + serv?.portNumber + "'],"
                } else {
                    data += "['" + link + "','" + vips + "','',''],"

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
        log.info("viplisting()")
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


    /**
     * Render a pdf with a list of applications.
     */
    def renderFormPDF(){
        log.info("renderFormPDF()")
        List<App> apps = new ArrayList<>()
        String title = ""

        String param = params.get(PARAM_SELECT).toString()
        log.info("hidden value select is :" + param)

        if (param == null) {
            title= "Liste de toutes les applications"
            apps = App.findAll()

        } else if (param.equals(PARAM_VIP)) {
            final String nameParam = params.get(PARAM_VIP_SELECT)
            Vip vip= Vip.findByTechnicalName(nameParam)
            apps=getApps(vip.servers)
            title= "Liste de toutes les applications sur " + nameParam

        } else if (param.equals(PARAM_SERVER)) {
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
        log.info("Apps size :" + apps.size())
        log.info("Page number :" + pageNumber)

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
                final float y = 700
                final float margin = 100
                drawTable(page, contentStream, y, margin, apps[countApp..max], title);
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

        document.save(REPORT_FILE);
        document.close();
        render( file:new File(REPORT_FILE), fileName: REPORT_FILE)
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
        contentStream.moveTextPositionByAmount((float)(margin+cellMargin+10),(float)(y+20));
        log.info("==> title:" + title)
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
            log.info("==> name:" + it.name)
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

    /**
     * Affiche la liste des crontab dans un planing
     */
    def crontab() {

// TEST
//        Crontab crontab = new Crontab()
//        crontab.crontab = "00 11 * * *"
//        crontab.description = " /root/shell.sh"
//        crontab.command = " /root/shell.sh"
//
//        Machine machine1 = new Machine()
//        machine1.name = "serveur_1"
//        machine1.save()
//
//        crontab.machine = machine1
//        crontab.save(failOnError: true)




//        Crontab crontab2 = new Crontab()
//        crontab2.crontab = "0 12 ? * * 1-5 *"
//        crontab2.description = "ma description 2"
//        Machine machine2 = new Machine()
//        machine2.name = "titi"
//        machine2.save()
//        crontab2.save()

        String planning = ""

        List<Crontab> lst = Crontab.findAll()
        int size = lst.size()
        int cpt = 0
        for(Crontab cron : lst) {
            String definition = cron.command
            planning += getOnePlanningRow(definition, cron.crontab)
            if (cpt <= size -1) {
                planning += ","
            }
            cpt++
        }


        [planning:planning]
    }

    /**
     * Get one planning row.
     * @param cron
     */
    def getOnePlanningRow(String definition, String cron) {
        CronDefinition cronDefinition =
            CronDefinitionBuilder.defineCron()
                    .withMinutes().and()
                    .withHours().and()
                    .withDayOfMonth()
                    .supportsHash().supportsL().supportsW().and()
                    .withMonth().and()
                    .withDayOfWeek()
                    .withIntMapping(7, 0) //we support non-standard non-zero-based numbers!
                    .supportsHash().supportsL().supportsW().and()
                    .withYear().and()
                    .lastFieldOptional()
                    .instance();

        //or get a predefined instance
        //cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(com.cronutils.model.CronType.QUARTZ);

        //create a parser based on provided definition
        CronParser parser = new CronParser(cronDefinition);

        DateTime now = DateTime.now();
        ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(cron));
        DateTime nextExecution = executionTime.nextExecution(now);


        String tmpStr = nextExecution.toString()
        println("DATE:" + tmpStr)
        String nextDateStr = tmpStr.split("\\.")[0]

        String planning = "{"
        planning += "title  : '"
        planning  += definition
        planning  += "',"
        planning += "start  : '"
        planning += nextDateStr
        planning += "'"
        planning += "}"
        return planning
    }

    def graph() {

        chartConfig = new ArrayList<>()
        String dataVip = generateDataVip();

        String dataVipEnum = generateDataVipEnum()
        [dataVip:dataVip, dataVipEnum:dataVipEnum]
    }

    /**
     * Generate string with vip number
     * @return
     */
    String generateDataVipEnum() {
        String data = ""
        int cpt = 1
        for(String chartConf : chartConfig) {
            data += chartConf
            if (cpt <= (chartConfig.size() - 1))  {
                data += ","
            }
            cpt++
        }
        return data
    }

    String generateStrData(String nodeName, String parentName, String name, String title) {
        String data = ""

            String vipNumStr = "\n" + nodeName + " = {\n"
            data += vipNumStr
            data += "\t\t\t\tparent: " + parentName + ",\n"
            if (parentName.contains("root")) {
                data += "stackChildren: true,"
            }
            if (parentName.contains("root")) {
                data += "\t\t\t\tHTMLclass: 'blue',\n"
            }

            data += "\t\t\t\ttext: {\n"
            String strName = "\t\t\t\t\tname: \"" + name + "\",\n"
            data += strName

            String strTitle = "\t\t\t\t\ttitle: \"" + title + "\",\n"
            data += strTitle
            data += "\t\t\t\t},\n"
            String htmlId = "\n\t\t\t\tHTMLid: " + nodeName + "\n"
            data += htmlId
            data += "\t\t\t},\n\t"


            chartConfig.add(nodeName)

        return data
    }


    /**
     * Generate vip tree format with an existing list of vips.
     *  vip = {
     * text: {
     * name: "VIP",
     * title: "Description de la vip"
     },
     },
     * @return
     */
    String generateDataVip() {
        String data = ""
        List<Vip> vips = Vip.findAll()

        int cpt = 1
        for(Vip vip : vips) {
            String parent = "vip" + cpt
            String title = ""
            if (vip.technicalName.contains("_http")) {
                title = "HTTP"
            }
            if (vip.technicalName.contains("_ssl")) {
                title = "SSL"
            }
            data += generateStrData("vip" + cpt, "root", vip.name, title)

            List<String> machines = new ArrayList<>()
            Map<String, List<String>> maps = new TreeMap<>()
            for(Server server : vip.servers) {
                List<String> lst = new ArrayList<>()
                String appStr = ""
                for(String app : server.linkToApps) {
                    appStr += app
                    appStr += " "
                }

                if (!maps.containsKey(server.machineHostName)) {
                    lst.add(server.portNumber + ":" + appStr)

                } else {
                    lst = maps.get(server.machineHostName)
                    lst.add(server.portNumber + ":" + appStr)

                }
                maps.put(server.machineHostName, lst)
            }
            int i = 0
            for(String str : maps.keySet()) {
                String result = ""
                for(String strApp : maps.get(str) ) {
                    result += strApp
                    result += "    "
                }
                data += generateStrData("serv" + i + "_" + cpt, parent, str, result)
                i++
            }

//            int i = 0
//            for(String machineName : machines) {
//                data += generateStrData("serv" + i + "_" + cpt, parent, machineName, "app1,app2")
//                i++
//            }

//            String vipNumStr = "\nvip" + cpt + " = {\n"
//            data += vipNumStr
//            data += "\t\t\t\tparent: root,\n"
//            data += "\t\t\t\tHTMLclass: 'blue',\n"
//            data += "\t\t\t\ttext: {\n"
//            String name = "\t\t\t\t\tname: \"" + vip.name + "\",\n"
//            data += name
//
//            String title = "\t\t\t\t\ttitle: \"" + vip.technicalName + "\",\n"
//            data += title
//            data += "\t\t\t\t},\n"
//            String htmlId = "\n\t\t\t\tHTMLid: vip" + cpt + "\n"
//            data += htmlId
//            data += "\t\t\t},\n\t"
//
//
//            chartConfig.add("vip" + cpt)
            cpt++
        }
        return data
    }


    def network() {

    }



}
