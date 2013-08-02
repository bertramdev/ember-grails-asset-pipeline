Ember.Js Grails Asset-Pipeline
================================
The Grails `ember-asset-pipeline` is a plugin that provides ember handlebars template precompiler support to asset-pipeline as well as emberjs dependencies and scripts for getting started.

For more information on how to use asset-pipeline, visit [here](http://www.github.com/bertramdev/asset-pipeline).

## Getting started
1. Add the plugin to your BuildConfig:

```groovy
plugins {
	compile ":ember-asset-pipeline:1.0.0-RC6.1"
}
```

2. Next, generate the application structure:
```shell
grails ember-bootstrap
```

3. Add your application js file to your root gps file
```gsp
<head>
	<asset:javascript src="application.js"/>
</head>
```

## For CoffeeScript support
1. Add coffee-asset-pipeline to the BuildConfig
```groovy
	compile ":coffee-asset-pipeline:0.1"
```

2. Run the bootstrap generator in step 4 with an extra flag instead:
```sh
grails ember-bootstrap --javascript-engine=coffee
```

Note:

This plugin includes some flag options for the bootstrap generator:

```
--ember-path or -d # custom ember path
--javascript-engine  # engine for javascript (js or coffee)
--app-name or -n # custom ember app name
```


## Architecture

Ember does not require an organized file structure. However, this plugin allows you
to use `grails ember-bootstrap` to create the following directory structure under `app/assets/javascripts`:

    controllers/
    helpers/
    models/
    routes/
    templates/
    views/

Additionally, it will add the following lines to `app/assets/javascripts/application.js`.
By default, it uses the Grails Application's name and creates an `grails_app_name.js`
file to setup application namespace and initial requires:

    //= require handlebars
    //= require ember
    //= require ember-data
    //= require_self
    //= require grails_app_name
    GrailsAppName = Ember.Application.create();

Ask Grails to serve HandlebarsJS and pre-compile templates to Ember
by putting each template in a dedicated ".handlebars", or ".hbs" file
(e.g. `grails-app/assets/javascripts/templates/admin_panel.handlebars`)
and including the assets in your layout:
		<asset:javascript src="templates/admin_panel.js"/>

If you want to strip template root from template names, add `templates_root` option to your application configuration block.
By default, `templates_root` is `'templates'`.

```groovy
grails {
	assets {
		handlebars {
			templateRoot = 'ember_templates'
			templatePathSeperator = "/"
	  }
  }
}
```


If you store templates in a file like `grails-app/assets/javascripts/ember_templates/admin_panel.handlebars` after setting the above config,
it will be made available to Ember as the `admin_panel` template.



Bundle all templates together thanks to Grails Asset Pipeline,
e.g create `grails-app/assets/javascripts/templates/all.js` with:

    //= require_tree .

Now a single line in the layout loads everything:

    <asset:javascript src="templates/all.js"/>
