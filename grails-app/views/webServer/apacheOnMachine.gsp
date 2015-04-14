<%@ page import="toolprod.Server" %>
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
        <div class="col-md-3">
            <div class="list-group">
                <g:each in="${servers}" var="serv">
                      ${serv.name}:${serv.portNumber}
                      </br>
                </g:each>
                %{--<g:each in="${map.keySet()}" var="servername">--}%

                    %{--<g:if test="${!((String)servername).startsWith('source_')}">--}%

                            %{--<a href="#" class="list-group-item list-group-item-warning">--}%
                                %{--${servername}--}%
                            %{--</a>--}%
                            %{--<g:each in="${map.get(servername)}" var="portNumber">--}%
                                %{--<a href="<g:createLink action="getWebServer" params="[name:servername, type:'apache', port: portNumber]" />">--}%
                                    %{--${portNumber}--}%
                                %{--</a>--}%
                            %{--</g:each>--}%


                    %{--</g:if>--}%
                %{--</g:each>--}%
           </div>
            <br/>
        </div>

        %{--<div class="col-md-9">--}%
            %{--<g:if test="${selectServer != null}">--}%
                %{--<div class="row">--}%
                    %{--<h1>${selectServer?.name}:${selectServer?.portNumber} </h1>--}%
                    %{--<br/>--}%
                    %{--<table class="table table-hover table-striped">--}%
                        %{--<tbody>--}%
                            %{--<tr>--}%
                                %{--<td>Serveur web</td>--}%
                                %{--<td>${selectServer?.name}:${selectServer?.portNumber}</td>--}%
                            %{--</tr>--}%
                            %{--<tr>--}%
                                %{--<td>Type</td>--}%
                                %{--<td>${selectServer?.serverType}</td>--}%
                            %{--</tr>--}%
                            %{--<tr>--}%
                                %{--<td>Machine</td>--}%
                                %{--<td>${selectServer?.machineHostName}</td>--}%
                            %{--</tr>--}%
                        %{--</tbody>--}%
                    %{--</table>--}%
                    %{--<br/>--}%
                %{--</div>--}%

                %{--<div class="row">--}%
                    %{--<div class="panel panel-warning">--}%
                        %{--<div class="panel-heading">--}%
                            %{--<h3 class="panel-title">Liste des applications référencées</h3>--}%
                        %{--</div>--}%

                        %{--<div class="panel-body">--}%
                            %{--<table class="table table-hover table-striped">--}%
                                %{--<thead>--}%
                                %{--<tr>--}%
                                    %{--<th>#</th>--}%
                                    %{--<th>Nom de l'application</th>--}%
                                %{--</tr>--}%
                                %{--</thead>--}%
                                %{--<g:each in="${selectServer?.linkToApps}" var="linkAppName">--}%
                                    %{--<tr>--}%
                                        %{--<td><a href="<g:createLink controller="AppRetail" action="app" params="[name:linkAppName]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>--}%
                                        %{--<td>${linkAppName}</td>--}%
                                    %{--</tr>--}%
                                %{--</g:each>--}%
                            %{--</table>--}%
                        %{--</div>--}%
                    %{--</div>--}%
                %{--</div>--}%
                %{--<div class="row">--}%
                    %{--<div class="panel panel-warning">--}%
                        %{--<div class="panel-heading">--}%
                            %{--<h3 class="panel-title">Liste des modules</h3>--}%
                        %{--</div>--}%

                        %{--<div class="panel-body">--}%
                            %{--<table class="table table-hover table-striped">--}%
                                %{--<thead>--}%
                                %{--<tr>--}%
                                    %{--<th>#</th>--}%
                                    %{--<th>Nom du modules</th>--}%
                                %{--</tr>--}%
                                %{--</thead>--}%
                                %{--<g:each in="${selectServer?.modules}" var="module">--}%
                                    %{--<tr>--}%
                                        %{--<td></td>--}%
                                        %{--<td>${module}</td>--}%
                                    %{--</tr>--}%
                                %{--</g:each>--}%
                            %{--</table>--}%
                        %{--</div>--}%
                    %{--</div>--}%
                %{--</div>--}%
            %{--</g:if>--}%
            %{--<g:else>--}%
                %{--<div class="well well-lg">Merci de choisir un serveur apache dans la liste !</div>--}%
            %{--</g:else>--}%
        %{--</div>--}%
    </div>
</div>
</body>
</html>