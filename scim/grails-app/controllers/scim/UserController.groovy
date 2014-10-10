package scim
import grails.transaction.*
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.HttpMethod.*

class UserController {

	// GET
	def show() {
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
		println "ERROR?!"
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

}
