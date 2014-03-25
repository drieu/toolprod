package fr.edu.admin

import org.springframework.web.multipart.MultipartHttpServletRequest

class AdminController {

    def index() {
        println("Index action from AdminController !")
        redirect(action: 'upload')
    }

    def init() {
        println("Init action from AdminController !")

    }

    /**
     * Upload a file and read content.
     * @return flash.message for success and error.
     */
    def upload() {
        if (request instanceof MultipartHttpServletRequest) {
            def file = request.getFile('appLst')
            if((file != null) && (!file.isEmpty())) {
                InputStream inputStream;
                BufferedReader br;
                try {
                    inputStream = file.inputStream;
                    br = new BufferedReader(new InputStreamReader(inputStream))
                    String strLine

                    while ((strLine = br.readLine()) != null) {

                        final char SPACE = ' '
                        //ProxyPass               /appli http://webX.fr:PORT/APPLI
                        if (strLine.startsWith("ProxyPass" + SPACE)) {
                            def params = strLine.tokenize()
                            final int NB_LINE_ELEMENT = 3; // Number element in ProxyPass line.
                            if (params.size() == NB_LINE_ELEMENT ) {
                                String app = params.get(1);
                                app = app.substring(1); // don't want the / in /appli

                                String url = params.get(2);

                                // extract server and port from http://webX.fr:PORT/APPLI or https://webX.fr:PORT/APPLI
                                String server = extractServerFromHttpProxyPass(url);
                                String port = extractPortFromHttpProxyPass(url);

                                println("Extract from ProxyPass appli:" + app + " url:" + url + " server:" + server + " port:" + port);
                            }
                        }

                    }
                    flash.message="success"
                } catch (IOException e) {
                    flash.message = 'failed'
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            flash.message = "failed: " + e.getMessage()
                            e.printStackTrace();
                        }
                    }
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            flash.message = 'failed: ' + e.getMessage()
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                flash.message = 'failed'
            }
        }
        redirect(action:'init')
    }


    def extractServerFromHttpProxyPass(String url) {
        String server = "";
        String strProtocol = ""
        if (url.startsWith("http://")) {
            strProtocol = "http://";
        }
        if (url.startsWith("https://")) {
            strProtocol = "https://";
        }
        if ((url != null) && (!strProtocol.isEmpty()) && (url.contains(":"))) {
            // extract after http://
            String str = "";
            int beginIndex = strProtocol.size();
            str = url.substring(beginIndex);

            int pos = str.indexOf(':');
            if (pos > 0) {
                // extract before :
                server = str.substring(0, pos);
            } else { //webX.fr/appli/
                pos = str.indexOf('/');
                if (pos > 0) {
                    server = str.substring(0, pos);
                } else {
                    server = str;
                }
            }
        }
        return server;
    }

    def extractPortFromHttpProxyPass(String url) {
        String port = "";
        if ((url != null) && (url.startsWith("http://")) && (url.contains(":"))) {
            // extract after http://
            String str = "";
            int beginIndex = "http://".size();
            str = url.substring(beginIndex);

            int pos = str.indexOf(':');
            if (pos > 0) {
                // extract before :
                str = str.substring(pos+1);
                if ((str != null) && (str.contains('/'))) {
                    pos = str.indexOf('/');
                    if (pos > 0) {
                        port = str.substring(0, pos);
                    }
                }

            }
        }
        return port;
    }
}
