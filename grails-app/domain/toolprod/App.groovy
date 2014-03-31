package toolprod

/**
 * Application class.
 */
class App {

    /**
     * Application name.
     */
    String name

    /**
     * Application description.
     */
    String description

    /**
     * url.
     * TODO : list of urls ?
     */
    String url

    static hasMany = [servers : Server]


    static constraints = {
        name(blank:false)
        description(size:0..400)
        url(size:0..350, url:true)
    }
}
