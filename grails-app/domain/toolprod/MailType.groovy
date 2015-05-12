package toolprod

/**
 * Use for ldap feature to find the link between real name ( fullNameType ) and technical name ( shortNameType ).
 */
class MailType {

    /**
     * ex : intendance
     */
    String fullNameType

    /**
     * ex : int
     */
    String shortNameType

    static constraints = {
    }
}
