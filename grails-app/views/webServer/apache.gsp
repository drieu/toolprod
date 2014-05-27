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
    <g:applyLayout name="menu" />

    <div class="row">
        <div class="col-md-3">
            <div class="list-group">
                <a href="#" class="list-group-item active">
                    Serveurs ${type}
                </a>
                <g:each in="${servers}" var="server">
                    <a href="<g:createLink action="getWebServer" params="[name:server.name]" />" class="list-group-item">
                        ${server.name}
                    </a>
                </g:each>
            </div>
        </div>

        <div class="col-md-9">
            <g:if test="${selectServer != null}">
                <div class="row">
                    <div class="panel panel-info">
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
                                        <td></td>
                                        <td>${linkAppName}</td>
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
</div>
</body>
</html>