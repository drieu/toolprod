<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Welcome to Production !</title>
    <g:javascript library='jquery'/>
    <g:javascript library='bootstrap'/>
    <r:layoutResources/>
</head>

<body>
<div class="container">
    <g:applyLayout name="menu"/>

    <div class="row">
        <div class="col-md-3">
                    <g:each in="${map.keySet()}" var="servername">
                        <div class="list-group">
                            <a href="#" class="list-group-item list-group-item-warning">
                                Serveur ${servername}
                            </a>
                            <g:each in="${map.get(servername)}" var="portNumber">
                                <a href="<g:createLink action="getWebServer" params="[name:servername, type:'weblogic', port: portNumber]" />" class="list-group-item">
                                    ${portNumber}
                                </a>
                            </g:each>

                        </div>
                    </g:each>

        </div>

        <div class="col-md-9">
            <g:if test="${selectServer != null}">
                <div class="row">
                    <div class="panel panel-success">
                        <div class="panel-heading">
                            <h3 class="panel-title">Serveur web</h3>
                        </div>

                        <div class="panel-body">
                            <table class="table table-hover table-striped">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Nom</th>
                                    <th>Port</th>
                                </tr>
                                </thead>
                                <tr>
                                    <td></td>
                                    <td>${selectServer?.name}</td>
                                    <td>${selectServer?.portNumber}</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="panel panel-success">
                        <div class="panel-heading">
                            <h3 class="panel-title">Liste des applications référencées</h3>
                        </div>

                        <div class="panel-body">
                            <table class="table table-hover table-striped">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Nom de l'application</th>
                                </tr>
                                </thead>
                                <g:each in="${selectServer?.linkToApps}" var="linkAppName">
                                    <tr>
                                        <td><a href="<g:createLink controller="AppRetail" action="app" params="[name:linkAppName]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>
                                        <td>${linkAppName}</td>
                                    </tr>
                                </g:each>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="panel panel-success">
                        <div class="panel-heading">
                            <h3 class="panel-title">Liste des modules</h3>
                        </div>

                        <div class="panel-body">
                            <table class="table table-hover table-striped">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Nom du modules</th>
                                </tr>
                                </thead>
                                <g:each in="${selectServer?.modules}" var="module">
                                    <tr>
                                        <td></td>
                                        <td>${module}</td>
                                    </tr>
                                </g:each>
                            </table>
                        </div>
                    </div>
                </div>
            </g:if>
            <g:else>
                <div class="well well-lg">Merci de choisir un serveur apache dans la liste !</div>
            </g:else>
        </div>
    </div>
</div>

<r:layoutResources/>
</body>
</html>