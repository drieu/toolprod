class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.${format})?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:"index")
        "/admin"(view:"/admin")
        "500"(view:'/error')
	}
}
