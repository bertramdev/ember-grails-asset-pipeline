includeTargets << grailsScript("_GrailsBootstrap")

includeTargets << new File(emberAssetPipelinePluginDir, "scripts/_EmberJsBootstrap.groovy")

target(emberBootstrapIt: "Creates a base directory structure for an ember application!") {
	def extension = 'js'
	if(argsMap['javascript-engine'] == 'coffee') {
		extension = 'js.coffee'
	} else if(argsMap['javascript-engine'] == 'em') {
		extension = 'js.em'
	}
	def arguments = [
		emberPath: argsMap['ember-path'] ?: argsMap['d'] ?: "grails-app/assets/javascripts",
		javascriptEngine: argsMap['javascript-engine'] ?: null,
		engineExtension:  extension,
		application_name: argsMap['app-name'] ?: argsMap['n'] ?: metadata.'app.name'
	]
  emberBootstrap(arguments)
}

setDefaultTarget(emberBootstrapIt)

