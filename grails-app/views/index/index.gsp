<%@ page import="toolprod.IndexController" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Welcome to Production !</title>
    <asset:javascript src="jquery.js"/>
    <asset:javascript src="application.js"/>
    <asset:stylesheet href="bootstrap/bootstrap.css"/>
    <asset:javascript src="bootstrap/bootstrap.js"/>
</head>

<body>
    <div class="container">
        <g:applyLayout name="menu" />

        <div class="row">
                    <blockquote>
                        <h1>Bienvenue sur Toolprod</h1>
                        <footer>Une boîte à outil pour la production</footer>
                    </blockquote>
                    <br/>
                    <div class="well well-lg">
                        Pour le moment, cet outil permet de :
                        <ul>
                            <li>Répertoriez les applications existantes</li>
                            <li>Répertoriez les machines, les serveurs apache et weblogic pour chaque application</li>
                            <li>Faire un contrôle simple des fichiers Apache</li>
                        </ul>
                    </div>
                    <br/>
            </div>
        </div>
    </div>

</body>
</html>
