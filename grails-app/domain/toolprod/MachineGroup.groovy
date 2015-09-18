package toolprod

/**
 * Group of machine.
 * Group parsed from config file then saved in MachineGroup table.
 */
class MachineGroup {

    /**
     * Name of group.
     */
    String groupName

    /**
     * Description.
     */
    String description

    /**
     * List of regex to obtain machine name
     * e.g : for example regex could be web for all machine which start with web.
     */
    List<String> regex = []

    static hasMany = [regex: String]

    static constraints = {
        description(nullable: true)
    }
}
