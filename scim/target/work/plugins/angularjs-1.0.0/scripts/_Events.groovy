def angularjsPluginCount = 0

eventCompileEnd = {
	if (angularjsPluginDir && (angularjsPluginCount == 0)) {
		angularjsPluginCount++
		updateAngularjs(basedir)
	}
}

def updateAngularjs(basedir) {
	def classLoader = new GroovyClassLoader()
	def angularjsConfigurationHolder = classLoader.loadClass("org.grails.plugins.angularjs.AngularjsConfigurationHolder")
	def autoDownload = angularjsConfigurationHolder.getAngularjsAutoDownload(basedir, angularjsPluginDir)
	if (autoDownload) {
		includeTargets << new File("${angularjsPluginDir}/scripts/AngularjsUpdate.groovy")
		update()
	}
}
