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
                                String app = ""
                                String url = ""
                                String server = ""
                                String port = ""

                                app = params.get(1);
                                app = app.substring(1); // don't want the / in /appli

                                url = params.get(2);

                                // extract server and port from http://webX.fr:PORT/APPLI
                                if ((url != null) && (url.startsWith("http://")) && (url.contains(":"))) {
                                    // extract after http://
                                    String str = "";
                                    int beginIndex = "http://".size();
                                    str = url.substring(beginIndex);

                                    int pos = str.indexOf(':');
                                    if (pos > 0) {
                                        // extract before :
                                        server = str.substring(0, pos);
                                        str = str.substring(pos+1);
                                        if ((str != null) && (str.contains('/'))) {
                                            pos = str.indexOf('/');
                                            if (pos > 0) {
                                                port = str.substring(0, pos);
                                            }
                                        }

                                    } else { //webX.fr/appli/
                                        pos = str.indexOf('/');
                                        if (pos > 0) {
                                            server = str.substring(0, pos);
                                        } else {
                                            server = str;
                                        }
                                    }
                                }

                                // extract server and port from https://webX.fr:PORT/APPLI
                                if ((url != null) && (url.startsWith("https://")) && (url.contains(":"))) {
                                    // extract after http://
                                    String str = "";
                                    int beginIndex = "https://".size();
                                    str = url.substring(beginIndex);

                                    int pos = url.indexOf(':');
                                    println("beginIndex:" + beginIndex + " pos:" + pos + " str:" + str);
                                    // extract before :
                                    server = str.substring(0, pos);

                                    str = url.substring( pos, url.length());
                                    if ((str != null) && (str.contains('/'))) {
                                        pos = url.indexOf('/');
                                        port = str.substring(0, pos)
                                    }
                                }
                                println("Extract from ProxyPass appli:" + app + " url:" + url + " server:" + server + " port:" + port);
                            }
                        }

                        //println("line :" + strLine)
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
}
