includeTargets << grailsScript("_GrailsBootstrap")

includeTargets << new File(emberAssetPipelinePluginDir, "scripts/_EmberJsBootstrap.groovy")

target(emberBootstrapIt: "Creates a base directory structure for an ember application!") {
	def arguments = [
		emberPath: argsMap['ember-path'] ?: argsMap['d'] ?: "grails-app/assets/javascripts",
		javascriptEngine: argsMap['javascript-engine'] ?: null,
		engineExtension:  argsMap['javascript-engine'] == 'coffee' ? 'js.coffee' : 'js',
		application_name: argsMap['app-name'] ?: argsMap['n'] ?: metadata.'app.name'
	]
  emberBootstrap(arguments)
}

setDefaultTarget(emberBootstrapIt)

