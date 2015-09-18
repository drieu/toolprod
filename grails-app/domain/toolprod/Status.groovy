package toolprod

/**
 * Table with store check result from Apache file conf.
 */
class Status {

    /**
     * Name of the machine.
     */
    String machineName

    /**
     * Filename of the httpd.conf file
     */
    String fileName

    /**
     * Servername in httpd conf file.
     */
    String name

    static constraints = {
    }
}
