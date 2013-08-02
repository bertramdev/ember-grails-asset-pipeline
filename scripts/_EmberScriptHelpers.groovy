String.metaClass.getCamelize = {
  delegate.split("-").inject(""){ before, word ->
    before += word[0].toUpperCase() + word[1..-1]
  }
}

String.metaClass.getUnderscore = {
	def output = delegate.replaceAll("-","_")
	output.replaceAll(/\B[A-Z]/) { '_' + it }.toLowerCase()
}

emberTemplate = { params ->
	def engine     = new groovy.text.GStringTemplateEngine()
	def template   = new File(emberAssetPipelinePluginDir, ["src/templates",params.templateName].join(File.separator))


	def generatedTemplateText = engine.createTemplate(template.text).make(params.params).toString()
	if(generatedTemplateText && params.destination) {
		def outputFile = new File(basedir, params.destination)
		outputFile.createNewFile()
		outputFile.text = generatedTemplateText
	} else if(generatedTemplateText) {
		return generatedTemplateText
	}
}

injectIntoFile = { params ->
	def outputFile = new File(basedir, params.file)
	if(!outputFile.exists() && !params.autoCreate) {
		throw "File Not Found For Injection"
	} else if(params.autoCreate) {
		outputFile.createNewFile()
	}
	outputFile.text = params.text + "\n" + outputFile.text
}
