package toolprod

/**
 * Unix crontab ( 00 22 * * * /root/shell.sh ) are parsed and save in database
 */
class Crontab {

    /**
     * For example : 00 22 * * *
     */
    String crontab

    /**
     *  /root/shell.sh
     */
    String command

    /**
     * Description.
     */
    String description

    /**
     * Link with a Machine.
     */
    Machine machine

    static constraints = {
        description(nullable: true)
    }
}
