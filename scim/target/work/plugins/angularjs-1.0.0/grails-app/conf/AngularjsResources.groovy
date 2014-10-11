import org.codehaus.groovy.grails.plugins.GrailsPluginUtils
import org.grails.plugins.angularjs.AngularjsConfigurationHolder

modules = {
	def config = AngularjsConfigurationHolder.angularjsConfig
	def disposition = config.resourcesDisposition
	def nominify = config.resourcesNominify
	"grails-angularjs" {
		resource url: [plugin: "angularjs", dir: "js", file:"angular.js"], disposition: disposition, nominify: nominify
		resource url: [plugin: "angularjs", dir: "css", file:"angular-csp.css"], disposition: disposition, nominify: nominify
		getResources("i18n").each {
			resource url: [plugin: "angularjs", dir: "js/i18n", file:"$it"], disposition: disposition, nominify: nominify
		}
		getResources("modules").each {
			resource url: [plugin: "angularjs", dir: "js/modules", file:"$it"], disposition: disposition, nominify: nominify
		}
	}
}

def getResources(path) {

	def resourcePaths = []
	def pluginDir = GrailsPluginUtils.pluginInfos.find{ it.name == "angularjs" }.pluginDir.file.canonicalPath
	def dirPath = "$pluginDir/web-app/js/$path"
	def dir = new File(dirPath)

	dir.eachFileRecurse { File file ->
		if (file.isDirectory() || file.isHidden()) return
		resourcePaths << file.path.replace(dirPath, "")
	}

	resourcePaths
}