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
            <g:applyLayout name="menu"/>
            <h1>Bienvenue sur la page d'admin</h1>
            <br/>
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">Initialisation des données de l'application</h3>
                </div>
                <div class="panel-body">
                    <ul class="nav nav-pills nav-stacked">
                        <li><g:link controller="admin" action="initData">Etape 1 : Initialisation des données</g:link></li>
                        <li><g:link controller="admin" action="init">Etape 2 : Import de fichiers Httpd.conf</g:link></li>
                        <li><g:link controller="admin" action="initPortals">Etape 3 : Initialisation des portails</g:link></li>
                        <li><g:link controller="admin" action="initFromArena">Etape 4 : Initialisation à partir du fichier XML ARENA</g:link></li>
                        %{--<li><g:link controller="admin" action="initCrontab">Etape 5 : Initialisation à partir des fichiers de crontab ( EXPERIMENTAL )</g:link></li>--}%
                    </ul>
                </div>
            </div>
            <br/>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Autres actions</h3>
                </div>
                <div class="panel-body">
                    <ul class="nav nav-pills nav-stacked">

                        <li><g:link controller="admin" action="info">Informations techniques</g:link></li>
                    </ul>
                </div>
            </div>
            <g:render template="/layouts/footer"></g:render>
        </div>
    </body>
</html>
