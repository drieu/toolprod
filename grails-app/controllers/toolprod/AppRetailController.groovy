package toolprod

import fr.edu.toolprod.bean.AppBean
import fr.edu.toolprod.bean.PrintAppBean

class AppRetailController {

    // Export service provided by Export plugin
    def exportService
    def grailsApplication  //inject GrailsApplication

    /**
     * We backup the choice of the user because when you click on pdf, you can't pass portal.
     */
    def backupChoice

    def app() {
        def myApp = null
        def selectApp = params.get("name")
        if (selectApp != null) {
            myApp = App.findByName((String)selectApp)
        }

        return [app:myApp]
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
           log.info(appBean.toString())
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

        def portals = Portal.findAll().unique{ it.name}
        return [appBeans:appBeans, portals:portals, portalChoice:portalChoice]
    }
}
