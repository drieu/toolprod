package fr.edu.admin

import org.springframework.web.multipart.MultipartHttpServletRequest

class AdminController {

    def index() {
        println("Index action from AdminController !")
    }

    def init() {
        println("Init action from AdminController !")

    }

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
                        println("line :" + strLine)
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
