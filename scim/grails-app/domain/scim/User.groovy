package scim
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class User {

    	static hasMany = [groups:ScimGroup]    
    	static belongsTo = ScimGroup

	String userName
	// GRAILS handles these
	Long id
	Long version
	Date dateCreated
	Date lastUpdated
	// spec specifies plain text password, not happy about this..
	String externalID = ""
	String password = ""
	String nickName = ""
	String profileURL = ""
	Boolean active = ""

	// name:
	String title = ""
	String familyName = ""
	String givenName = ""
	String middleName = ""
	String honorificPrefix = ""
	String honorificSuffix = ""

	// extend to include other attributes, i.e. locale, timezone, etc

    	static constraints = {
		userName unique: true
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
		def map = [:]
		map["id"] = id
		map["userName"] = userName
		map["meta"] = [
			"created": dateCreated,
			"lastModified": lastUpdated,
			"version": version
		]
		if (externalID) {
			map["externalID"] = externalID
		}
		if (password) {
			map["password"] = password
		}
		if (nickName) {
			map["nickName"] = nickName
		}
		if (profileURL) {
			map["profileURL"] = profileURL
		}
		if (title) {
			map["title"] = title
		}
		if (active) {
			map["active"] = active
		}
        	if (this.groups) {
            		map["groups"] = []
            		this.groups.each() {
                		map["groups"].add([
                    			value: it.id,
                    			display: it.displayName
                		])
            		}
        	}
		def name = name()
		if (name.size() > 0) {
			map["name"] = name
		}
		return map
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
                		} else if (key == "groups") {
                    			// inefficient
                    			if (this.groups) {
                        			this.groups.each() { group -> this.removeFromGroups(group) }
                        			this.groups.clear()
                    			}
                    			value.each() {
                   				def group = ScimGroup.get(it.value)
                        			if (group) {
                        				this.addToGroups(group)
                            				updated++
                        			}
                    			}
				} else {
					updated += updateAttribute(key, value)
				}
		}
		if (updated > 0) {
			this.save(flush: true)
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
