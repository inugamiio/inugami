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

    <script src="<%=request.getContextPath()%>/js/vendors/platform-js/platform.js"></script>
	<script src="<%=request.getContextPath()%>/js/vendors/jquery/dist/jquery.min.js"></script>
	
	<script src="<%=request.getContextPath()%>/js/vendors.js"></script>
	<script src="<%=request.getContextPath()%>/js/vendors/chartjs/Chart.bundle.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/vendors/chartjs/Chart.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/vendors/d3/build/d3.min.js"></script>

	<script src="<%=request.getContextPath()%>/js/vendors/bootstrap/bootstrap.bundle.min.js"></script>
	

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
	<link   href="<%=request.getContextPath()%>/css/core/bootstrap.min.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/core/bootstrap-reboot.min.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/core/bootstrap-grid.min.css" rel="stylesheet" />
	
	<link   href="<%=request.getContextPath()%>/css/core/font.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/core/font-awesome.min.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/core/fontello.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/core/mfglabs.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/core/typicons.css" rel="stylesheet" />
	
	<link   href="<%=request.getContextPath()%>/js/vendors/primeng/resources/primeng.min.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/js/vendors/primeng/resources/themes/bootstrap/theme.css" rel="stylesheet" />


	

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
        var config = {
          transpiler: 'typescript',
          typescriptOptions: {
            emitDecoratorMetadata: true
          },

          packages: {
            '<%=request.getContextPath()%>/js': {
              defaultExtension: 'ts'
            },
            '<%=request.getContextPath()%>/js/vendors': {
              defaultExtension: 'js'
            }
          },
          map:{
"js/app/controllers"                	: APP_PATH+"/controllers",
"js/app/components"                 	: APP_PATH+"/components",
"js/app/scopes"                     	: APP_PATH+"/scopes",
"js/app/models"                     	: APP_PATH+"/models",
"js/app/services"                   	: APP_PATH+"/services",

"@angular/router"                   	: VENDOR_PATH+"/@angular/router/bundles/router.umd.min",
"@angular/core"                     	: VENDOR_PATH+"/@angular/core/bundles/core.umd.min",
"@angular/common"                   	: VENDOR_PATH+"/@angular/common/bundles/common.umd.min",
"@angular/http"                     	: VENDOR_PATH+"/@angular/http/bundles/http.umd.min.js",
"@angular/platform-browser-dynamic" 	: VENDOR_PATH+"/@angular/platform-browser-dynamic/bundles/platform-browser-dynamic.umd.min",
"@angular/compiler"                 	: VENDOR_PATH+"/@angular/compiler/bundles/compiler.umd.min",
"@angular/platform-browser"         	: VENDOR_PATH+"/@angular/platform-browser/bundles/platform-browser.umd.min",
"@angular/forms"                    	: VENDOR_PATH+"/@angular/forms/bundles/forms.umd.min",
"@angular/platform-browser/animations"	: VENDOR_PATH+"/@angular/platform-browser/bundles/platform-browser-animations.umd.min",
"@angular/animations/browser"			: VENDOR_PATH+"/@angular/animations/bundles/animations-browser.umd.min",
"@angular/animations"				 	: VENDOR_PATH+"/@angular/animations/bundles/animations.umd.min",


"rxjs"                              	: VENDOR_PATH+"/rxjs",
"primeng/primeng"				    	: VENDOR_PATH+"/primeng/primeng",
"d3"									: VENDOR_PATH+"/d3/build/d3.min"

<%=ResourceLoaderJSP.getPluginSystemMap()%>
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
