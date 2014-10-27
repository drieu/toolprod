package toolprod

/**
 * Group of machine.
 */
class MachineGroup {

    String groupName

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
