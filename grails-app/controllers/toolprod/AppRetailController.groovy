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
                    appBean.portals.add(portal.name)
                   }
               }
           }
           log.info(appBean.toString())
           appBeans.add(appBean)
        }
        return [appBeans:appBeans]
    }
}
