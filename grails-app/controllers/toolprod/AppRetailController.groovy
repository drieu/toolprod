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
}
