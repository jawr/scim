package scim

class ScimGroupController {

	// GET
	def show() {
		if (params.id) {
			def group = ScimGroup.get(params.id)
			if (group) {
				return render(
					status: 200,
					text: group.toJSON()
				)
            		}
            		return render(
                		status: 404,
                		text: "No Group found with that ID."
            		)   
		} else {
			def groups = []
            		// check this against spec/implement spec's filters
            		if (params.displayName) {
        			def search = params.displayName.replaceAll(" ", "%")
        		    	groups = ScimGroup.findAllByDisplayNameLike(
                    			"%" + search + "%"
                		)
            		} else {
                		groups = ScimGroup.list()
            		}
			if (groups.size() > 0) {
				def groupsParsed = []
				groups.each() {
					groupsParsed.add(it.toJSON())
				}
				return render(
					status: 200,
					text: groupsParsed
				)
            		}
			return render(
                		status: 404,
                		text: "No Groups found."
            		)
		}
	}

	// POST
	def save() {	
		if (ScimGroup.countByDisplayName(request.JSON.groupName) > 0) {
			return render(
				status: 409,
				text: "Group with that displayName already exists."
			)
		}
		def group = new ScimGroup(request.JSON)
		if (group.save(flush: true)) {
			// take advantage of our own renderer (yet to be made)
			return render(
				status: 201,
				text: group.toJSON()
			)
		}
        return render(
            status: 500,
            text: "Error. Unable to save new Group." // more info
        )
	}

	// PUT
	// The spec says to PUT an entire resource, however, as we do not require a
	// full resource on creation, I have opted to allow attribute creation via 
	// this method; if a field is missing in the request body, it is not removed
	// from the underlying object
	def update() {
		def group = ScimGroup.get(params.id)
		if (group) {
			// what to do if nothing updated?
			//def updated = group.updateFromJSON(request.JSON)
			def updated = group.updateFromJSON(request.JSON)
            		if (updated > 0) {
                		return render(
                    			status: 200,
                    			text: group.toJSON()
                		)
            		}
            		return render(
                		status: 500,
                		text: "Error. Unable to save Group." // more info
            		)
		}
		return render(
			status: 404,
			text: "No Group found with that ID."
		)
	}

	// DELETE
	def remove() {
		def group = ScimGroup.get(params.id)
		if (group) {
			try {
				group.delete(flush: true)
				return render(
					status: 200,
					text: ''
				)
			} 
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				render(
					status: 500,
					text: "Error. Unable to delete Group." // improve
				)
			}
		}
		return render(
			status: 404,
			text: "No Group found with that ID."
		)
	}
}

