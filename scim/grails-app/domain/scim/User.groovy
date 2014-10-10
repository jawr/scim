package scim
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class User {
	Long id
	Long version
	String userName
	// GRAILS handles these
	Date dateCreated
	Date lastUpdated
	// spec specifies plain text password, not happy about this..
	String externalId = ""
	String password = ""
	String nickName = ""
	String profileURL = ""
	String title = ""
	Boolean active = ""

	// name:
	String familyName = ""
	String givenName = ""
	String middleName = ""
	String honorificPrefix = ""
	String honorificSuffix = ""

	// extend to include other attributes, i.e. locale, timezone, etc

    static constraints = {
		
    }

	static mapping = {
		autoTimestamp true
	}

	Map name() {
		def name = [:]
		def formatted = []
		// possibly neater to do with reflection
		if (honorificPrefix) { 
			formatted.add(honorificPrefix)
			name["honorifixPrefix"] = honorificPrefix
		}
		if (givenName) {
			formatted.add(givenName)
			name["givenName"] = givenName
		}
		if (middleName) {
			formatted.add(middleName.substring(0, 1))
			 name["middleName"] = middleName
		}
		if (familyName) {
			formatted.add(familyName)
			name["familyName"] = familyName
		}
		if (honorificSuffix) { 
			formatted.add(honorificSuffix)
			name["honorificSuffix"] = honorificSuffix
		}
		if (formatted.size() > 0) {
			name["formatted"] = formatted.join(" ")
		}
		return name
	}

	Map toMap() {
		def json = [:]
		json["id"] = id
		json["userName"] = userName
		json["meta"] = [
			"created": dateCreated,
			"lastModified": lastUpdated,
			"version": version
		]
		if (externalId) {
			json["externalId"] = externalId
		}
		if (password) {
			json["password"] = password
		}
		if (nickName) {
			json["nickName"] = nickName
		}
		if (profileURL) {
			json["profileURL"] = profileURL
		}
		if (title) {
			json["title"] = title
		}
		if (active) {
			json["active"] = active
		}
		def name = name()
		if (name.size() > 0) {
			json["name"] = name
		}
		return json
	}

	String toJSON() {
		// a custom marshaller could eliviate a lot of the above code
		def builder = new JsonBuilder()
		def m = toMap()
		// add SCIM related
		m["schemas"] = ["urn:scim:schemas:core:1.0"]
		builder(m)
		return builder.toString()
	}

	int updateFromJSON(result) {
		// need to handle versioning better
		// def slurper = new JsonSlurper()
		// def result = slurper.parseText(json)
		def updated = 0
		result.each() {
			key, value ->
				if (key == "name") {
					value.each() {
						k, v ->
							updated += updateAttribute(k, v)
					}
				} else {
					updated += updateAttribute(key, value)
				}
		}
		if (updated > 0) {
			this.save()
		}
		return updated
	}

	int updateAttribute(key, value) {
		if (this.hasProperty(key)) {
			this.setProperty(key, value)
			return 1
		} else {
			return 0
		}
	}
}
