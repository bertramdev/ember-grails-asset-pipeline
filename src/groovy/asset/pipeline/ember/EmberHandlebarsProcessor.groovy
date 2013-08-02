package asset.pipeline.ember
import asset.pipeline.handlebars.*
import asset.pipeline.AssetHelper
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import org.springframework.core.io.ClassPathResource
import org.codehaus.groovy.grails.commons.ApplicationHolder
// CoffeeScript engine uses Mozilla Rhino to compile the CoffeeScript template
// using existing javascript in-browser compiler
class EmberHandlebarsProcessor {

  Scriptable globalScope
  ClassLoader classLoader

  EmberHandlebarsProcessor(){
    try {
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
    def grailsApplication  = ApplicationHolder.getApplication()
    def templateRoot       = grailsApplication.config.grails.assets.handlebars.templateRoot ?: 'templates'
    def templateSeperator  = grailsApplication.config.grails.assets.handlebars.templatePathSeperator ?: '/'
    def relativePath       = relativePath(assetFile.file, templateRoot, templateSeperator)
    def templateName           = AssetHelper.nameWithoutExtension(assetFile.file.getName())
    if(relativePath) {
      templateName = [relativePath,templateName].join(templateSeperator)
    }
    return templateName
  }

  def relativePath(file, templateRoot, templateSeperator) {
    def path          = file.getParent().split(AssetHelper.QUOTED_FILE_SEPARATOR)
    def startPosition = path.findLastIndexOf{ it == templateRoot }

    if(startPosition+1 >= path.length) {
      return ""
    }

    path = path[(startPosition+1)..-1]
    return path.join(templateSeperator)
  }

  def wrapTemplate = { String templateName, String compiledTemplate ->
    """
    (function(){
      Ember.TEMPLATES['$templateName'] = Ember.Handlebars.template($compiledTemplate)
    }());
    """
  }
}
