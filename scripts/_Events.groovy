// includeTargets << new File(assetPipelinePluginDir, "scripts/_AssetCompile.groovy")

eventAssetPrecompileStart = {
	asset.pipeline.handlebars.HandlebarsAssetFile.processors = [asset.pipeline.ember.EmberHandlebarsProcessor]
}
