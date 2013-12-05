import asset.pipeline.AssetHelper
import asset.pipeline.handlebars.HandlebarsAssetFile
import asset.pipeline.ember.EmberHandlebarsProcessor
class EmberAssetPipelineGrailsPlugin {

    def version = "1.2.0"
    def grailsVersion = "2.0 > *"
    def title = "Ember.js Asset-Pipeline Plugin"
    def author = "David Estes"
    def authorEmail = "destes@bcap.com"
    def description = "Provides Ember.js integration with asset-pipeline. Allows for handlebars precompilation as well as scaffolding for building an emberjs application."
    def documentation = "http://github.com/bertramdev/ember-grails-asset-pipeline"

    def license = "APACHE"
    def organization = [ name: "Bertram Capital", url: "http://www.bertramcapital.com/" ]
    def issueManagement = [ system: "GITHUB", url: "http://github.com/bertramdev/ember-grails-asset-pipeline/issues" ]
    def scm = [ url: "http://github.com/bertramdev/ember-grails-asset-pipeline" ]

    def doWithDynamicMethods = { ctx ->
        HandlebarsAssetFile.processors = [EmberHandlebarsProcessor]
    }
}
