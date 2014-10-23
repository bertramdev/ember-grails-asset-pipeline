grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		grailsPlugins()
		mavenCentral()
	}

	plugins {


		runtime ":asset-pipeline:1.9.9"
		runtime ":handlebars-asset-pipeline:1.3.0.3"

		build ':release:3.0.0', ':rest-client-builder:1.0.3', {
			export = false
		}
	}
}
