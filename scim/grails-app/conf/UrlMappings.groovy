class UrlMappings {

	static mappings = {

        "/v1/$controller/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
	    action = [GET:"show", POST:"save", PUT:"update", DELETE:"remove"]
        }

        "/"(view:"/index")

        "500"(view:'/error')
	}
}
