grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		grailsPlugins()
		mavenCentral()
	}

	dependencies {
		compile 'com.bertramlabs.plugins:ember-asset-pipeline:2.0.5'
	}

	plugins {


		runtime ":asset-pipeline:2.0.13"

		build ':release:3.0.0', ':rest-client-builder:1.0.3', {
			export = false
		}
	}
}
