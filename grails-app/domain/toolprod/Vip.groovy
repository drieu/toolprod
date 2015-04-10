package toolprod

/**
 * Store vip name and associate server list.
 */
class Vip {

    /**
     * Current name for vip ( portal name )
     */
    String name

    /**
     * http,ssl
     */
    String type

    /**
     * technical name found in bigip.conf
     */
    String technicalName

    /**
     * Server list for a vip.
     */
    List<Server> servers = []


    static hasMany = [servers : Server]

}
