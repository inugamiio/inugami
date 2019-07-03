<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.inugami.plugins.root.jsp.ResourceLoaderJSP"%>
<!DOCTYPE html>
<html>

<head>
<title><%=ResourceLoaderJSP.getApplicationTitle()%></title>
<meta charset="UTF-8">
	<meta http-equiv='cache-control' content='no-cache'>
	<meta http-equiv='expires' content='0'>
	<meta http-equiv='pragma' content='no-cache'>
	<link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/favicon.ico">
	<base href="<%=request.getContextPath()%>/">
	<meta name="viewport" content="width=device-width, initial-scale=1">

 
	<script src="<%=request.getContextPath()%>/js/vendors/vendors.js"></script>



	<!-- CORE JS -->
	<script type="text/javascript">
		var APPLICATION_VERSION   = "<%=ResourceLoaderJSP.getApplicationVersion()%>/";
	</script>
	<script src="<%=request.getContextPath()%>/js/app/core/namespace.js"></script>
	<script src="<%=request.getContextPath()%>/js/app/core/logger.js"></script>
	<script src="<%=request.getContextPath()%>/js/app/core/asserts.js"></script>
	<script src="<%=request.getContextPath()%>/js/app/core/checks.js"></script>
	<script src="<%=request.getContextPath()%>/js/app/core/constants.js"></script>
	<script src="<%=request.getContextPath()%>/js/app/core/values.js"></script>
	<script src="<%=request.getContextPath()%>/js/app/core/formatters.js"></script>
	<script src="<%=request.getContextPath()%>/js/app/core/datas.extractors.js"></script>
	<script src="<%=request.getContextPath()%>/js/app/core/services.js"></script>
	<script src="<%=request.getContextPath()%>/js/app/core/events.js"></script>
	<script src="<%=request.getContextPath()%>/js/app/core/sse.js"></script>
	<script src="<%=request.getContextPath()%>/js/app/core/rendering.js"></script>
	<script src="<%=request.getContextPath()%>/js/app/core/initCore.js"></script>

	<%=ResourceLoaderJSP.getPluginJavaScripts(request.getContextPath())%>

	<!-- STYLES -->
	<link   href="<%=request.getContextPath()%>/css/core/vendors.css" rel="stylesheet" />
	

	<link   href="<%=request.getContextPath()%>/css/core/application.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/core/page-root-home.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/core/page-admin.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/core/page-login.css" rel="stylesheet" />

	<link   href="<%=request.getContextPath()%>/css/core/components.css" rel="stylesheet" />
	
	<%=ResourceLoaderJSP.getPluginCss(request.getContextPath())%>




<script type="text/javascript">
    // =====================================================================
    // GLOBALES VALUES
    // =====================================================================
    var CONTEXT_PATH       = "<%=request.getContextPath()%>/";
    var RESOURCES_PATH     = "<%=request.getContextPath()%>/js/";
    var APP_PATH           = RESOURCES_PATH+"app";
    var VENDOR_PATH        = RESOURCES_PATH+"vendors";

	var PLUGINS_GAVS       = <%=ResourceLoaderJSP.PLUGINS_GAVS%>;
	var MESSAGES           = <%=ResourceLoaderJSP.JAVASCRIPT_I18N%>;
	var PLUGINS_COMPONENTS = {};

	org.inugami.logger.rootLevel=org.inugami.logger.levels.debug;
	org.inugami.sse.enableLogger = false;

    // =====================================================================
    // SYSTEM JS CONFIG
    // =====================================================================
    (function(global) {
		SystemJS.typescriptOptions = {
			"target": "es5",
			"module": "system",
			"moduleResolution": "node",
			"sourceMap": true,
			"emitDecoratorMetadata": true,
			"experimentalDecorators": true,
			"noImplicitAny": true,
			"suppressImplicitAnyIndexErrors": true
		};
	
        var config = {
          transpiler: 'ts',
		  meta: {
            'typescript': {
              "exports": "ts"
            }
        },
    
        paths: {
            'vendor:': VENDOR_PATH+"/"
		},
		
        bundles: {
            '<%=request.getContextPath()%>/js/vendors/rxjs-system-bundle/Rx.system.min.js': [
              "rxjs",
              "rxjs/*",
              "rxjs/operator/*",
              "rxjs/operators/*",
              "rxjs/observable/*",
              "rxjs/scheduler/*",
              "rxjs/symbol/*",
              "rxjs/add/operator/*",
              "rxjs/add/observable/*",
              "rxjs/util/*"
			],
			'<%=request.getContextPath()%>/js/vendors/primeng/primeng-components.js': [
				"components/*",
		    ]
		}, 
        paths: {
            'vendor:': VENDOR_PATH+"/"
        },
          map:{
"app" 									: 'js/app',

"@angular/core"                     	: "vendor:@angular/core/bundles/core.umd.min.js",
"@angular/common"                   	: "vendor:@angular/common/bundles/common.umd.min.js",
"@angular/compiler"                 	: "vendor:@angular/compiler/bundles/compiler.umd.min.js",
'@angular/platform-browser'             : "vendor:@angular/platform-browser/bundles/platform-browser.umd.js",
"@angular/platform-browser-dynamic" 	: "vendor:@angular/platform-browser-dynamic/bundles/platform-browser-dynamic.umd.min.js",
"@angular/common/http"                  : "vendor:@angular/common/bundles/common-http.umd.js",
"@angular/forms"                    	: "vendor:@angular/forms/bundles/forms.umd.min.js",
"@angular/platform-browser/animations"	: "vendor:@angular/platform-browser/bundles/platform-browser-animations.umd.min.js",
"@angular/animations/browser"		    : "vendor:@angular/animations/bundles/animations-browser.umd.min.js",
"@angular/animations"				 	: "vendor:@angular/animations/bundles/animations.umd.min.js",
"@angular/router"                   	: "vendor:@angular/router/bundles/router.umd.min.js",

"ts"                                    : 'vendor:plugin-typescript/lib/plugin.js',
"typescript"                            : 'vendor:typescript/lib/typescript.js',
"core-js"                               : "vendor:core-js",

"primeng/primeng"				    	: "vendor:primeng/primeng.js",
"d3"									: "vendor:d3/d3.min.js"

<%=ResourceLoaderJSP.getPluginSystemMap()%>
          },
		  packages: {
			"app"     : { defaultExtension: 'ts' }
		  }
        };

        if (global.filterSystemConfig) {
           global.filterSystemConfig(config);
        }

         System.config(config);
         System.import(APP_PATH+'/app.boot.ts').catch(console.error.bind(console));

    })(this);


   </script>

</head>

<body class="inugami">

	<app-component>
		<div class="loading">
			<div class="info">
				<h1><%=ResourceLoaderJSP.getApplicationTitle()%></h1>
				<h2>loading...</h2>
				<div class="icon-loading">
	          <img src="<%=request.getContextPath()%>/images/loading.gif"/>
	      </div>
			</div>

		</div>
	</app-component>
</body>

</html>
