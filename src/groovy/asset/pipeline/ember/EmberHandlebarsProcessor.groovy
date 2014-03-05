package asset.pipeline.ember
import asset.pipeline.handlebars.*
import asset.pipeline.AssetHelper
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import org.springframework.core.io.ClassPathResource
// import org.codehaus.groovy.grails.commons.ApplicationHolder
import grails.util.Holders
// CoffeeScript engine uses Mozilla Rhino to compile the CoffeeScript template
// using existing javascript in-browser compiler
class EmberHandlebarsProcessor {

  Scriptable globalScope
  ClassLoader classLoader
  def precompilerMode
  EmberHandlebarsProcessor(precompiler=false){
    try {
      this.precompilerMode = precompiler
      classLoader = getClass().getClassLoader()

      def handlebarsJsResource = new ClassPathResource('asset/pipeline/handlebars/handlebars.js', classLoader)
      def emberCompilerResource = new ClassPathResource('asset/pipeline/ember/ember-template-compiler.js', classLoader)

      def handlebarsJsStream = handlebarsJsResource.inputStream
      def emberJsStream      = emberCompilerResource.inputStream

      Context cx = Context.enter()
      cx.setOptimizationLevel(-1)
      globalScope = cx.initStandardObjects()
      cx.evaluateReader(globalScope, new InputStreamReader(handlebarsJsStream, 'UTF-8'), handlebarsJsResource.filename, 0, null)
      cx.evaluateString globalScope, """
        function precompileEmberHandlebars(string) {
          return exports.precompile(string).toString();
        }
        """, "", 1, null

      cx.evaluateReader(globalScope, new InputStreamReader(emberJsStream, 'UTF-8'), emberCompilerResource.filename, 0, null)
    } catch (Exception e) {
      throw new Exception("Handlebars Engine initialization failed.", e)
    } finally {
      try {
        Context.exit()
      } catch (IllegalStateException e) {}
    }
  }

  def process(input, assetFile) {
    try {
      def cx = Context.enter()
      def compileScope = cx.newObject(globalScope)
      compileScope.setParentScope(globalScope)
      compileScope.put("handlebarsSrc", compileScope, input)
      def result = cx.evaluateString(compileScope, "exports.precompile(handlebarsSrc).toString();", "Handlebars compile command", 0, null)
      return wrapTemplate(templateNameForFile(assetFile), result)
    } catch (Exception e) {
      throw new Exception("""
        Handlebars Engine compilation of handlebars to javascript failed.
        $e
        """)
    } finally {
      Context.exit()
    }
  }

  def templateNameForFile(assetFile) {
    def grailsApplication  = Holders.grailsApplication
    def templateRoot       = grailsApplication.config.grails.assets.handlebars.templateRoot ?: 'templates'
    def templateSeperator  = grailsApplication.config.grails.assets.handlebars.templatePathSeperator ?: '/'
    def relativePath       = relativePathFromAssetPath(assetFile.file, true)


    def templateName = relativePath
    if(templateName.startsWith("${templateRoot}${AssetHelper.DIRECTIVE_FILE_SEPARATOR}")) {
      templateName = templateName.replace("${templateRoot}${AssetHelper.DIRECTIVE_FILE_SEPARATOR}","")
    }


    templateName.replaceAll(AssetHelper.DIRECTIVE_FILE_SEPARATOR, templateSeperator)
    templateName = AssetHelper.nameWithoutExtension(templateName)

    return templateName
  }


  def relativePathFromAssetPath(file, includeFileName=false) {
    def path
    if(includeFileName) {
      path = file.class.name == 'java.io.File' ? file.getCanonicalPath().split(AssetHelper.QUOTED_FILE_SEPARATOR) : file.file.getCanonicalPath().split(AssetHelper.QUOTED_FILE_SEPARATOR)
    } else {
      path = file.getParent().split(AssetHelper.QUOTED_FILE_SEPARATOR)
    }

    def startPosition = path.findLastIndexOf{ it == "grails-app" }
    if(startPosition == -1) {
      startPosition = path.findLastIndexOf{ it == 'web-app' }
      if(startPosition+2 >= path.length) {
        return ""
      }
      path = path[(startPosition+2)..-1]
    }
    else {
      if(startPosition+3 >= path.length) {
        return ""
      }
      path = path[(startPosition+3)..-1]
    }

    return path.join(AssetHelper.DIRECTIVE_FILE_SEPARATOR)
  }

  def wrapTemplate = { String templateName, String compiledTemplate ->
    """
    (function(){
      Ember.TEMPLATES['$templateName'] = Ember.Handlebars.template($compiledTemplate)
    }());
    """
  }
}
