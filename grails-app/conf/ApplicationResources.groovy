modules = {
    application {
        resource url:'js/application.js'
    }

    jquery {
        resource url:'jquery/jquery-2.1.0.min.js'
    }

    jqtree {
        resource url:'js/tree.jquery.js'
        resource url:'css/jqtree.css'
    }

    bootstrap {
        dependsOn 'jquery'
        resource url:'bootstrap/css/bootstrap.css'
        resource url:'bootstrap/js/bootstrap.js'
    }
}