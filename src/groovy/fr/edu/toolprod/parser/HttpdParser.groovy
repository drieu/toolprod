package fr.edu.toolprod.parser
/**
 * Parse httpd.conf file.
 * User: drieu
 * Date: 27/03/14
 * Time: 09:15
 */
class HttpdParser {


    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final Character COLON = ':';
    private static final String SLASH = '/'
    private static final String EMPTY = ""


    private static final String PROXY_PASS = "ProxyPass"
    private static final String SPACE = ' '


    def appName = EMPTY

    def appUrl = EMPTY

    def appServer = EMPTY

    def appPort = EMPTY

    /**
     * Parse file line by line
     * @param inputStream
     * @return true if no error occurs during parsing.
     */
    def parse(InputStream inputStream) {
        boolean bResult = false;
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream))
            String strLine

            while ((strLine = br.readLine()) != null) {

                //ProxyPass               /appli http://webX.fr:PORT/APPLI
                if (strLine.startsWith(PROXY_PASS + SPACE)) {
                    def params = strLine.tokenize()
                    final int NB_LINE_ELEMENT = 3; // Number element in ProxyPass line.
                    if (params.size() == NB_LINE_ELEMENT ) {
                        def tmpApp = params.get(1);
                        appName = tmpApp.substring(1); // don't want the / in /appli

                        appUrl = params.get(2);

                        // extract server and port from http://webX.fr:PORT/APPLI or https://webX.fr:PORT/APPLI
                        appServer = extractServerFromHttpProxyPass(appUrl);
                        appPort = extractPortFromHttpProxyPass(appUrl);

                        println("Extract from ProxyPass appli:" + appName + " url:" + appUrl + " server:" + appServer + " port:" + appPort);
                    }
                }

            }
            bResult = true;
        } catch (IOException e) {
            println("Failed to parse file : " + e.printStackTrace())
        } finally {
            bResult = false;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    println("Failed to parse file : " + e.printStackTrace())
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    println("Failed to parse file : " + e.getMessage())
                    e.printStackTrace();
                }
            }
        }
        return bResult
    }

    /**
     * Extract server in HTTP ProxyPass
     * @param myUrl e.g: http://webX.fr:PORT/APPLI
     * @return
     */
    def extractServerFromHttpProxyPass(String myUrl) {
        String result = EMPTY;
        String strProtocol = EMPTY
        if (myUrl.startsWith(HTTP)) {
            strProtocol = HTTP;
        }
        if (myUrl.startsWith(HTTPS)) {
            strProtocol = HTTPS;
        }
        if ((myUrl != null) && (!strProtocol.isEmpty()) && (myUrl.contains(COLON))) {
            // extract after http://
            String str = EMPTY;
            int beginIndex = strProtocol.size();
            str = myUrl.substring(beginIndex);

            int pos = str.indexOf(COLON);
            if (pos > 0) {
                // extract before :
                result = str.substring(0, pos);
            } else { //webX.fr/appli/
                pos = str.indexOf(SLASH);
                if (pos > 0) {
                    result = str.substring(0, pos);
                } else {
                    result = str;
                }
            }
        }
        return result;
    }

    /**
     * Extract server in HTTPS ProxyPass
     * @param myUrl e.g: http://webX.fr:PORT/APPLI
     * @return
     */
    def extractPortFromHttpProxyPass(String myUrl) {
        String strPort = EMPTY;
        if ((myUrl != null) && (myUrl.startsWith(HTTP)) && (myUrl.contains(COLON))) {
            // extract after http://
            String str = EMPTY;
            int beginIndex = HTTP.size();
            str = myUrl.substring(beginIndex);

            int pos = str.indexOf(COLON);
            if (pos > 0) {
                // extract before :
                str = str.substring(pos+1);
                if ((str != null) && (str.contains(SLASH))) {
                    pos = str.indexOf(SLASH);
                    if (pos > 0) {
                        strPort = str.substring(0, pos);
                    }
                }
            }
        }
        return strPort;
    }
}
