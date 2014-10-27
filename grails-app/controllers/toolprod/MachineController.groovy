package toolprod

class MachineController {

    def index() {}

    def list() {
        List<Machine> machines = Machine.findAll()

        return [machines:machines]
    }
}
