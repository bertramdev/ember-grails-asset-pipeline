eventAssetPrecompileStart = { assetSpecs ->
	if(!config.grails.assets.plugin."ember-asset-pipeline".excludes || config.grails.assets.plugin."ember-asset-pipeline".excludes.size() == 0) {
		config.grails.assets.plugin."ember-asset-pipeline".excludes = ["ember/*", "ember-data/*"]
	}
}
