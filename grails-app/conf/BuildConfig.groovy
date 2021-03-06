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
		compile 'com.bertramlabs.plugins:ember-asset-pipeline:2.13.1'
	}

	plugins {


		runtime ":asset-pipeline:2.13.1"

		build ':release:3.1.2', ':rest-client-builder:1.0.3', {
			export = false
		}
	}
}
