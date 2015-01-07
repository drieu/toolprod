modules = {
    application {
        resource url:'js/application.js'
    }

//    jquery {
//        resource url:'jquery/jquery-2.1.0.min.js'
//    }

    ztree {
        resource url:'js/jquery.ztree.all-3.5.min.js'
        resource url:'css/zTreeStyle.css'
    }

    bootstrap {
        //dependsOn 'jquery'
        resource url:'bootstrap/css/bootstrap.css'
        resource url:'bootstrap/js/bootstrap.js'
    }
}