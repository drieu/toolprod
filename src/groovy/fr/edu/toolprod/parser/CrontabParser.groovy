package fr.edu.toolprod.parser

import toolprod.Crontab
import toolprod.Machine

/**
 * Parse crontab file and save results in database.
 */
class CrontabParser extends Parser{

    /**
     * Store the string result for parsing (e.g: Nothing to parse for this file).
     */
    String result = EMPTY

    /**
     * Machine name.
     */
    String machineName

    /**
     * Store the string result for close..
     */
    private String closeResult = EMPTY

    CrontabParser() {
    }

    /**
     * Constrcutor.
     * @param input
     */
    CrontabParser(InputStream input, String name) {
        inputStream = input
        machineName = name
    }

    @Override
    def parse() {
        String line
        boolean bResult = false
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))
        result = EMPTY
        try {
            String command = null
            String cron = null

            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#"))  {
                    command = getCommand(line)
                    cron = getCron(line)
                    bResult = true
                }
                if (command != null) {

                    result += " save line :"
                    result += line
                    Crontab crontab = new Crontab()
                    crontab.description = command
                    crontab.command = command
                    crontab.crontab = cron
                    Machine machine = Machine.findByName(machineName)
                    if (machine == null) {
                        machine = new Machine()
                        machine.name = machineName
                        machine.save()
                    }
                    crontab.machine = machine
                    crontab.save(failOnError: true)
                    println("Save line with command : " + command)

                }
            }
            if (!bResult) {
                result += "Nothing to parse for file."
            }
            // Save Crontab config in database.

        } catch (IOException e) {
            result += "Impossible de parser le fichier !<br/>"
            log.error("Failed to parse file : " + e.printStackTrace())
        } finally {
            closeResult = close()
            if (!closeResult.isEmpty()) {
                result += closeResult
                bResult = false
            }
        }
        log.debug("parse() result=" + result)
        return bResult
    }

    protected String getCommand(String linecron) {
        String command = null
        if ((linecron != null) && (linecron.contains("/"))) {
            int startCommand = linecron.indexOf("/")
            int length = linecron.length()
            if (startCommand < length) {
                command = linecron.substring(startCommand, length)
            }
        }
        return command
    }

    protected String getCron(String linecron) {
        String command = null
        if ((linecron != null) && (linecron.contains("/"))) {
            int endCommand = linecron.indexOf("/")
            if (endCommand > 0) {
                command = linecron.substring(0, endCommand)
            }
        }
        return command
    }
}
