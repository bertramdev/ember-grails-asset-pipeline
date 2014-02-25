eventAssetPrecompileStart = { assetSpecs ->
	def handlebarsAssetFile = classLoader.loadClass('asset.pipeline.handlebars.HandlebarsAssetFile')
	def emberHandlebarsProcessor = classLoader.loadClass('asset.pipeline.ember.EmberHandlebarsProcessor')
	handlebarsAssetFile.processors = [emberHandlebarsProcessor]

	if(!config.grails.assets.plugin."ember-asset-pipeline".excludes || config.grails.assets.plugin."ember-asset-pipeline".excludes.size() == 0) {
		config.grails.assets.plugin."ember-asset-pipeline".excludes = ["ember/*", "ember-data/*"]
	}
}
