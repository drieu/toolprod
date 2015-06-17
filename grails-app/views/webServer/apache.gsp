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
    <asset:stylesheet href="mybootstrap.css"/>
    <asset:javascript src="bootstrap/bootstrap.js"/>
</head>
<body>
    <div class="container">
        <g:applyLayout name="menu"/>

        <div class="row">
                <div class="panel-group" id="accordion">
                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                        Liste des serveurs web&nbsp;&nbsp;<span class="badge">${map?.size()}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<small>Merci de cliquer pour voir la liste !</small>
                                </a>
                            </h4>
                        </div>
                        <div id="collapseOne" class="panel-collapse collapse">
                            <div class="panel-body">
                                <div class="table-responsive">
                                   <table class="table table-hover">
                                       <tr class="info">
                                           <th>Machine(s)</th>
                                           <th>Port(s)</th>
                                       </tr>
                                        <g:each in="${map?.keySet()}" var="servername">

                                            <g:if test="${!((String)servername).startsWith('source_')}">
                                                   <tr>
                                                       <td>
                                                        ${servername}
                                                       </td>
                                                       <td>
                                                            <g:each in="${map.get(servername)}" var="portNumber">
                                                                <a href="<g:createLink action="getWebServer" params="[name:servername, type:'apache', port: portNumber]" />">
                                                                    ${portNumber}
                                                                </a>
                                                            </g:each>
                                                       </td>
                                                   </tr>

                                            </g:if>
                                        </g:each>
                                   </table>
                                </div>
                            </div>
                        </div>
                    </div>
                <br/>
        </div>
        <div class="row">

                <g:if test="${selectServer != null}">
                        <h1>${selectServer?.name}:${selectServer?.portNumber} </h1>
                        <br/>
                        <table class="table table-hover">
                            <tbody>
                                <tr scope="row">
                                    <td>Serveur web</td>
                                    <td>${selectServer?.name}:${selectServer?.portNumber}</td>
                                </tr>
                                <tr scope="row">
                                    <td>Type</td>
                                    <td>${selectServer?.serverType}</td>
                                </tr>
                                <tr scope="row">
                                    <td>Machine</td>
                                    <td>${selectServer?.machineHostName}</td>
                                </tr>
                            </tbody>
                        </table>
                        <br/>

                        <div class="panel panel-warning">
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
                        <div class="panel panel-warning">
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
                </g:if>
                <g:else>
                    <div class="well well-lg">Merci de choisir un serveur apache dans la liste !</div>
                </g:else>
        </div>
        <g:render template="/layouts/footer"></g:render>
    </div>
</body>
</html>