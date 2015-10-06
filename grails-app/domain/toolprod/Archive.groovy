package toolprod

/**
 * Created with IntelliJ IDEA.
 * User: drieu
 * Date: 05/10/15
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
class Archive {

    String name

    int countApp

    List<String> machines

    List<String> apacheWebServer

    List<String> weblogicWebServer

    List<String> apps

    static hasMany = [machines : String, apacheWebServer : String, weblogicWebServer: String, apps :String]
}
