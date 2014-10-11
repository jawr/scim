package scim
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class ScimGroup {

    static hasMany = [users:User]

    String displayName
	// GRAILS handles these
	Long id
	Long version
	Date dateCreated
	Date lastUpdated

    static constraints = {
        displayName unique: true
    }

	static mapping = {
		autoTimestamp true
	}

    List getMembers() {
        def list = []
        users.each() {
            list.add([
                value: it.id,
                display: it.userName // perhaps formatted if available
            ])
        }
        return list
    }

	Map toMap() {
		def map = [:]
		map["id"] = id
		map["displayName"] = displayName
		map["meta"] = [
			"created": dateCreated,
			"lastModified": lastUpdated,
			"version": version
		]
        map["members"] = getMembers()
        return map
    }

	String toJSON() {
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
			    updated += updateAttribute(key, value)
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
