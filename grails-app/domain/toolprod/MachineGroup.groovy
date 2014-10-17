package toolprod

/**
 * Group of machine.
 */
class MachineGroup {

    String groupName

    String description

    List<Machine> machines

    static hasMany = [machines : Machine ]

    static constraints = {
    }
}
