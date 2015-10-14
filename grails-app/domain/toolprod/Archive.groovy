package toolprod

/**
 * Store informations about servers, machines, applications
 * It is usefull to compare differences with new configuration
 */
class Archive {

    /**
     * Archive's name.
     * To now, it's set to OLD.We will certainly change it in the future.
     */
    String name

    /**
     * Date of this archive
     */
    String date

    /**
     * Number of applications
     */
    int countApp

    /**
     * Machines's list
     */
    List<String> machines

    /**
     * Apache webserver's list.
     */
    List<String> apacheWebServer

    /**
     * Weblogic webserver's list
     */
    List<String> weblogicWebServer

    /**
     * Apps list.
     */
    List<String> apps

    static hasMany = [machines : String, apacheWebServer : String, weblogicWebServer: String, apps :String]
}
