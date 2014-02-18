import org.codehaus.groovy.grails.commons.ApplicationHolder

eventAssetPrecompileStart = { assetSpecs ->
	def handlebarsAssetFile = classLoader.loadClass('asset.pipeline.handlebars.HandlebarsAssetFile')
	def emberHandlebarsProcessor = classLoader.loadClass('asset.pipeline.ember.EmberHandlebarsProcessor')
	handlebarsAssetFile.processors = [emberHandlebarsProcessor]

	def grailsApplication = ApplicationHolder.getApplication()
	if(!grailsApplication.config.grails.assets.plugin."ember-asset-pipeline".excludes || grailsApplication.config.grails.assets.plugin."ember-asset-pipeline".excludes.size() == 0) {
		grailsApplication.config.grails.assets.plugin."ember-asset-pipeline".excludes = ["ember/*", "ember-data/*"]
	}
}
