package org.grails.plugins.angularjs

import grails.util.Environment
import grails.util.Holders

class AngularjsConfigurationHolder {

	static private ConfigObject getConfig(basedir, angularjsPluginDir) {
		def customConfig  = new ConfigSlurper().parse(new File("${basedir}/grails-app/conf/Config.groovy").toURI().toURL()).grails.plugin.angularjs
		def defaultConfig = new ConfigSlurper().parse(new File("${angularjsPluginDir}/grails-app/conf/AngularjsConfig.groovy").toURI().toURL()).grails.plugin.angularjs
		def config = customConfig ? defaultConfig.merge(customConfig) : defaultConfig
	}
	
	static Boolean getAngularjsAutoDownload(basedir, angularjsPluginDir) {
		getConfig(basedir, angularjsPluginDir).autoDownload		
	}
	
	static ConfigObject getAngularjsConfig() {
		def classLoader = new GroovyClassLoader(AngularjsConfigurationHolder.class.classLoader)
		def defaultConfigClass = classLoader.loadClass("AngularjsConfig")
		def slurper = new ConfigSlurper(Environment.current.name)
		def defaultConfig = slurper.parse(defaultConfigClass).grails.plugin.angularjs
		def grailsApplication = Holders.grailsApplication
		def customConfig = grailsApplication.config.grails?.plugin?.angularjs
		def config = customConfig ? defaultConfig.merge(customConfig) : defaultConfig
		return config
	}

	static ConfigObject getAngularjsConfig(basedir, angularjsPluginDir) {
		def savedConfigFile = "${angularjsPluginDir}/grails-app/conf/AngularjsConfigSaved.groovy"
		def savedConfig = new ConfigSlurper().parse(new File(savedConfigFile).toURI().toURL())
		def config = getConfig(basedir, angularjsPluginDir)
		
		if (config.equals(savedConfig)) {
			return
		} else {
			new File(savedConfigFile).withWriter { writer ->
				config.writeTo(writer)
			}
			return config
		}
	}
}
