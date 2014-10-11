includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsBootstrap")
includeTargets << grailsScript("_GrailsClasspath")
includeTargets << grailsScript("_GrailsProxy")

target(update: "Update AngularJS files") {
	depends(packageApp, compile)
	def classLoader = new GroovyClassLoader()
	def angularjsConfigurationHolder = classLoader.loadClass("org.grails.plugins.angularjs.AngularjsConfigurationHolder")
	def config = angularjsConfigurationHolder.getAngularjsConfig(basedir, angularjsPluginDir)
	if (!config) {
		//println "AngularJS files are up to date"
		return
	}	
	def assetJs = "${angularjsPluginDir}/src/manifests/grails-angularjs.js"
	def assetCss = "${angularjsPluginDir}/src/manifests/grails-angularjs.css"
	def assetJsDir = "${angularjsPluginDir}/grails-app/assets/javascripts"
	def assetCssDir = "${angularjsPluginDir}/grails-app/assets/stylesheets"
	def webJsDir = "${angularjsPluginDir}/web-app/js"
	def webCssDir = "${angularjsPluginDir}/web-app/css"
	def version = config.version
	def i18n = config.i18n
	def modules = config.modules
	def baseUrl = "http://code.angularjs.org/${version}"
	def nonCspCssVersions = ["1.0.2", "1.0.3", "1.0.4", "1.0.5", "1.0.6", "1.0.7", "1.0.8", "1.1.0", "1.1.1", "1.1.2", "1.1.3", "1.1.4", "1.1.5"]
	def nonErrorsJsonVersions = ["1.0.2", "1.0.3", "1.0.4", "1.0.5", "1.0.6", "1.0.7", "1.0.8", "1.1.0", "1.1.1", "1.1.2", "1.1.3", "1.1.4", "1.1.5"]
	def nonVersionJsonVersions = ["1.0.2", "1.0.3", "1.1.0", "1.1.1"]

	println "\nDownloading AngularJS files ..."

	ant.delete(dir: assetJsDir)
	ant.delete(dir: assetCssDir)
	ant.mkdir(dir: assetJsDir)
	ant.mkdir(dir: assetCssDir)

	ant.delete(dir: "${webJsDir}/i18n")
	ant.delete(dir: "${webJsDir}/modules")
	ant.mkdir(dir: "${webJsDir}/i18n")
	ant.mkdir(dir: "${webJsDir}/modules")
	ant.mkdir(dir: webCssDir)

	try {
		ant.get(src: "${baseUrl}/angular.js", dest: webJsDir)
	} catch(e) {
		println "Error: $version is not a valid AngularJS version."
		println "The AngularJS files were not downloaded."
		println "Check grails.plugin.angularjs.version in your Config.groovy."
		println "Visit http://code.angularjs.org/ for valid versions."
		return
	}

	if (!nonCspCssVersions.contains(version)) {
		try {
			ant.get(src: "${baseUrl}/angular-csp.css", dest: webCssDir)
		} catch(e) {
			println "Warning: angular-csp.css was not downloaded."
		}
	}

	if (!nonErrorsJsonVersions.contains(version)) {
		try {
			ant.get(src: "${baseUrl}/errors.json", dest: webJsDir)
		} catch(e) {
			println "Warning: errors.json was not downloaded."
		}
	}

	if (!nonVersionJsonVersions.contains(version)) {
		try {
			ant.get(src: "${baseUrl}/version.json", dest: webJsDir)
		} catch(e) {
			println "Warning: version.json was not downloaded."
		}
	}

	i18n.each {
		def base = it.substring(0,2)
		def error = false
		try {
			ant.get(src: "${baseUrl}/i18n/angular-locale_${base}.js", dest: "${webJsDir}/i18n")
		} catch(e) {
			error = true
			println "Error: $base is not a valid AngularJS language identifier for version $version."
		}
		if (!error) {
			try {
				ant.get(src: "${baseUrl}/i18n/angular-locale_${it}.js", dest: "${webJsDir}/i18n")
			} catch(e) {
				println "Error: $it is not a valid AngularJS locale for version $version."
			}
		}
	}

	modules.each {
		try {
			ant.get(src: "${baseUrl}/angular-${it}.js", dest: "${webJsDir}/modules")
		} catch(e) {
			println "Error: $it is not a valid AngularJS module for version $version."
		}
	}

	ant.copy(file: assetJs, todir: assetJsDir)
	ant.copy(file: assetCss, todir: assetCssDir)
	ant.copy(todir: assetJsDir) {fileset(dir: webJsDir)}
	ant.copy(todir: assetCssDir) {fileset(dir: webCssDir)}

	println "Finished downloading AngularJS files."
}

setDefaultTarget(update)