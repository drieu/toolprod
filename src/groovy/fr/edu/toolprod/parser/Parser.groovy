package fr.edu.toolprod.parser

import org.apache.commons.logging.LogFactory

/**
 * Abstract class Parser.
 */
abstract class Parser {

    /**
     * Contains file to parse
     */
    public InputStream inputStream // TODO : delete because change in constructor

    public File parserFile

    /**
     * Use to read inpustream.
     */
    private BufferedReader br;


    public static final String EMPTY = ""


    /**
     * Logger.
     */
    private static final log = LogFactory.getLog(this)

    /**
     * Method for parsing file.
     * @return
     */
    public abstract parse();


    /**
     * Close inpustream fo file to parse.
     * @return String result EMPTY if there is no error or a message.
     */
    def close() {
        String result = EMPTY
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                result += "Impossible de parser le fichier !<br/>"
                log.error("Failed to parse file : " + e.printStackTrace())
            }
        }
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                result += "Impossible de parser le fichier !<br/>"
                log.error("Failed to parse file : " + e.getMessage())
                e.printStackTrace();
            }
        }
        return result
    }


}
