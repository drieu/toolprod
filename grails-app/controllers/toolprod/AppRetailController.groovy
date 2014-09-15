package toolprod

import fr.edu.toolprod.bean.AppBean

class AppRetailController {

    def app() {
        def myApp = null
        def selectApp = params.get("name")
        if (selectApp != null) {
            myApp = App.findByName(selectApp)
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


        List<AppBean> appBeans = new ArrayList<>()
        def apps = App.findAll()
        for(App app : apps) {
           log.info(app.toString())
           AppBean appBean = new AppBean()
           appBean.name = app.name
           appBean.description = app.description
           appBean.serverUrl = app.url
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

        def portals = Portal.findAll().unique{ it.name}
        return [appBeans:appBeans, portals:portals, portalChoice:portalChoice]
    }
}
