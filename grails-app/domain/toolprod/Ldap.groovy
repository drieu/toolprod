package toolprod

/**
 * Store LDAP connection information.
 */
class Ldap {

    /**
     * LDAP NAME
     */
    String name

    /**
     * LDAP host.
     */
    String host

    /**
     * LDAP port.
     */
    String port

    /**
     * LDAP user.
     */
    String user

    /**
     * LDAP password.
     */
    String pwd


    static constraints = {
    }
}
