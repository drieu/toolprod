package toolprod

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
}
