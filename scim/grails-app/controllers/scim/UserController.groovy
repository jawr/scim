package scim

class UserController {

	// GET
	def show() {
		if (params.id) {
			def user = User.get(params.id)
			if (user) {
				render(
					status: 200,
					text: user.toJSON()
				)
			}
			render(
				status: 404,
				text: "No User found with that ID."
			)
		} else {
			def users = User.list()
			if (users.size() > 0) {
				def usersParsed = []
				users.each() {
					usersParsed.add(it.toJSON())
				}
				render(
					status: 200,
					text: usersParsed
				)
			} else {
				render(
					status: 404,
					text: "No users found."
				)
			}
		}
	}

	// POST
	def save() {	
		if (User.countByUserName(request.JSON.userName) > 0) {
			render(
				status: 409,
				text: "User with that userName already exists."
			)
		}
		def user = new User(request.JSON)
		if (user.save()) {
			// take advantage of our own renderer (yet to be made)
			render(
				status: 201,
				text: user.toJSON()
			)
		} else {
			render(
				status: 500,
				text: "Error. Unable to save new User." // more info
			)
		}
	}

	// PUT
	// The spec says to PUT an entire resource, however, as we do not require a
	// full resource on creation, I have opted to allow attribute creation via 
	// this method; if a field is missing in the request body, it is not removed
	// from the underlying object
	def update() {
		def user = User.get(params.id)
		if (user) {
			// what to do if nothing updated?
			//def updated = user.updateFromJSON(request.JSON)
			user.updateFromJSON(request.JSON)
			render(
				status: 200,
				text: user.toJSON()
			)
		}
		render(
			status: 404,
			text: "No User found with that ID."
		)
	}

	// DELETE
	def remove() {
		def user = User.get(params.id)
		if (user) {
			try {
				user.delete(flush: true)
				render(
					status: 200,
					text: ''
				)
			} 
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				render(
					status: 500,
					text: "Error. Unable to delete User." // improve
				)
			}
		}
		render(
			status: 404,
			text: "No User found with that ID."
		)
	}
}
