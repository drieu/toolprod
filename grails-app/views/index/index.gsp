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

                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <h3 class="panel-title">Erreurs dans les fichiers Apache  <span class="badge">${count}</span></h3>
                        </div>

                        <div class="panel-body">
                            <table class="table table-hover table-striped">
                                <caption>
                                    Etat des fichiers de configuration Apache
                                </caption>
                                <thead>
                                <tr>
                                    <th>Machine</th>
                                    <th>Fichier à vérifier</th>
                                    <th>ServerName</th>
                                    <th>Status</th>
                                </tr>
                                </thead>
                                <tbody>
                                        <g:each in="${toolprod.Status.findAll()}" var="check">
                                            <tr>
                                                <td>${check?.machineName}</td>
                                                <td>${check?.fileName}</td>
                                                <td>${check?.name}</td>
                                                <td>
                                                    <span class="label label-warning">WARNING</span>
                                                </td>
                                            </tr>
                                        </g:each>
                                        </tbody>
                            </table>
                        </div>
                    </div>
            </div>
        </div>
    </div>

</body>
</html>
