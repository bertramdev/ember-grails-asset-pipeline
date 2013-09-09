eventAssetPrecompileStart = { assetSpecs ->
	def handlebarsAssetFile = classLoader.loadClass('asset.pipeline.handlebars.HandlebarsAssetFile')
	def emberHandlebarsProcessor = classLoader.loadClass('asset.pipeline.ember.EmberHandlebarsProcessor')
	handlebarsAssetFile.processors = [emberHandlebarsProcessor]
}
