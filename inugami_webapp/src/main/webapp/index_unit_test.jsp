<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.inugami.webapp.jsp.ResourceLoaderJSP"%>
<!DOCTYPE html>
<html>

<head>
<title><%=ResourceLoaderJSP.getApplicationTitle()%></title>
<meta charset="UTF-8">
	<base href="<%=request.getContextPath()%>/">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<script src="<%=request.getContextPath()%>/js/vendors/jquery/dist/jquery.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/vendors.js"></script>
	<script src="<%=request.getContextPath()%>/js/vendors/chartjs/Chart.bundle.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/vendors/chartjs/Chart.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/vendors/d3/build/d3.min.js"></script>
	

	<!-- CORE JS -->
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


	<!-- UNIT TESTS -->
	<script src="<%=request.getContextPath()%>/js/vendors/qunit/qunit.js"></script>
	<script src="<%=request.getContextPath()%>/js/test/core/formattersTest.js"></script>
	<script src="<%=request.getContextPath()%>/js/test/core/dataExtractorsTest.js"></script>
	<script src="<%=request.getContextPath()%>/js/test/core/renderingTest.js"></script>


	<!-- STYLES -->
	<link   href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/bootstrap-theme.min.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/bootstrap-responsive.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/font-awesome.min.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/fontello.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/mfglabs.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/typicons.css" rel="stylesheet" />
	
	<link   href="<%=request.getContextPath()%>/js/vendors/primeng/resources/primeng.min.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/js/vendors/primeng/resources/themes/bootstrap/theme.css" rel="stylesheet" />
	
	<link   href="<%=request.getContextPath()%>/js/vendors/qunit/qunit.css" rel="stylesheet" />

	<link   href="<%=request.getContextPath()%>/css/font.css" rel="stylesheet" />

	<link   href="<%=request.getContextPath()%>/css/application.css" rel="stylesheet" />
	<link   href="<%=request.getContextPath()%>/css/components.css" rel="stylesheet" />



</head>

<body class="inugami">

<div id="qunit"></div>
<div id="qunit-fixture"></div>


</body>

</html>
