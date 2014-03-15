<%@ page import="toolprod.IndexController" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Production !</title>

        <!-- BOOTSTRAP -->
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">

        <!-- Optional theme -->
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">

        <!-- Latest compiled and minified JavaScript -->
        <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

    </head>
	<body>
		<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="page-body" role="main">
			<h1>Bienvenue sur ce site</h1>
			<p>Commentaire ...</p>
		</div>
        <span class="glyphicon glyphicon-search"></span>
        <button type="button" class="btn btn-default btn-lg">
            <span class="glyphicon glyphicon-star"></span> Star
        </button>
        <g:link url="admin">Page Admin</g:link>
        <g:each in="${lst}" var="server">
            <p>Nom du serveur : ${server.name}</p>
            <g:each in="${server.apps}" var="app">
                <p>Nom de l'application : ${app.name}</p>
            </g:each>
        </g:each>
	</body>
</html>
