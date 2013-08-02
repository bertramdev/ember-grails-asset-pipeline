
includeTargets << grailsScript("_GrailsBootstrap")
includeTargets << new File(emberAssetPipelinePluginDir, "scripts/_EmberScriptHelpers.groovy")

emberBootstrap = { params ->

	println params
	createDirLayout params
	createRouterFile params
	createAppFile params
	createStoreFile params
	createApplicationTemplate params
	injectIntoApplicationFile params
}

createDirLayout = { params ->
	['models','controllers','views','routes','helpers','templates'].each { dir ->
		ant.mkdir dir: "${basedir}/${params.emberPath}/${dir}"
	}
}

createRouterFile = { params ->
	emberTemplate templateName: "router.${params.engineExtension}", destination: "${params.emberPath}/router.${params.engineExtension}", params: params
}

createAppFile = { params ->
	emberTemplate templateName: "app.${params.engineExtension}", destination: "${params.emberPath}/${params.application_name.underscore}.${params.engineExtension}", params: params
}

createStoreFile = { params ->
	emberTemplate templateName: "store.${params.engineExtension}", destination: "${params.emberPath}/store.${params.engineExtension}", params: params
}

createApplicationTemplate = { params ->
	emberTemplate templateName: "application.handlebars", destination: "${params.emberPath}/templates/application.handlebars", params: params
}

injectIntoApplicationFile = { params ->
	try {
		injectIntoFile file: "${params.emberPath}/application.${params.engineExtension}", text: emberTemplate(templateName: "application.${params.engineExtension}", params:params)
	} catch(e) {
		injectIntoFile file: "${params.emberPath}/application.js", text: emberTemplate(templateName: "application.js", params: params), autoCreate: true
	}
}
