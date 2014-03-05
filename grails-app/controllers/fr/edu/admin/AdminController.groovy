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
            def file = request.getFile('myFile')
            if(!file.isEmpty()) {
                flash.message='success'
            } else {
                flash.message = 'failed'
            }
        }
    }
}
