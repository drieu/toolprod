<%@ page import="toolprod.IndexController" %>
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
                    Machines Identité
                </a>
                <g:each in="${machines}" var="machine">
                    <g:if test="${machine.name.contains('serid')}">
                        <a href="<g:createLink action="getMachineApps" params="[machine:machine.name]" />" class="list-group-item">
                            ${machine.name}
                        </a>
                    </g:if>
                </g:each>
            </div>
            <div class="list-group">
                <a href="#" class="list-group-item active">
                    Frontaux WEB
                </a>
                <g:each in="${machines}" var="machine">
                    <g:if test="${machine.name.contains('web')}">
                        <g:if test="${!machine.name.contains('webexaco') && !machine.name.contains('webgrh')}">
                            <a href="<g:createLink action="getMachineApps" params="[machine:machine.name]" />" class="list-group-item">
                            ${machine.name}
                            </a>
                        </g:if>
                    </g:if>
                </g:each>
            </div>
            <div class="list-group">
                <a href="#" class="list-group-item active">
                    Machines WEBGRH
                </a>
                <g:each in="${machines}" var="machine">
                    <g:if test="${machine.name.contains('webgrh')}">
                            <a href="<g:createLink action="getMachineApps" params="[machine:machine.name]" />" class="list-group-item">
                                ${machine.name}
                            </a>

                    </g:if>
                </g:each>
            </div>
            <div class="list-group">
                <a href="#" class="list-group-item active">
                    Machines Scolarité
                </a>
                <g:each in="${machines}" var="machine">
                    <g:if test="${machine.name.contains('wappsco')}">
                        <a href="<g:createLink action="getMachineApps" params="[machine:machine.name]" />" class="list-group-item">
                            ${machine.name}
                        </a>
                    </g:if>
                </g:each>
            </div>
            <div class="list-group">
                <a href="#" class="list-group-item active">
                    Machines
                </a>
                <g:each in="${machines}" var="machine">
                    <g:if test="${!machine.name.startsWith('web') && !machine.name.startsWith('serid') && !machine.name.startsWith('wappsco')}">

                        <a href="<g:createLink action="getMachineApps" params="[machine:machine.name]" />" class="list-group-item">
                        ${machine.name}
                        </a>
                    </g:if>

                </g:each>
            </div>

        </div>

        <div class="col-md-9">
            <g:if test="${searchResults != null && searchResults.isEmpty()}">
                <div class="alert alert-success alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <p class="alert-link">Search return no result !</p>
                </div>
            </g:if>
            <g:if test="${searchResults != null && !searchResults.isEmpty()}">
                <div class="row">

                    <div class="alert alert-success alert-dismissable">
                        <i>Résultats de la recherche</i>
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <table class="table table-hover table-striped">
                            <tbody>
                            <g:each in="${searchResults}" var="app">
                                <tr>
                                    <td></td>
                                    <td>${app?.name}</td>
                                    <td>${app?.description}</td>
                                    <td><a href="${app?.url}">${app?.url}</a></td>
                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
                </div>
            </g:if>
            <g:if test="${machine != null}">
                <div class="row">
                    <h1>${machine?.name}</h1>
                    <br/>
                    <br/>
                    <p class="text-left">
                        <dl class="dl-horizontal">
                            <dt>Détails </dt>
                            <dd>Nom : ${machine?.name}</dd>
                            <dd>IP : ${machine?.ipAddress}</dd>
                        </dl>
                    </p>
                </div>
                <div class="row">
                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <h3 class="panel-title">Liste des serveurs web</h3>
                        </div>
                        <div class="panel-body">
                            <table class="table table-hover table-striped">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Nom</th>
                                    <th>Port</th>
                                    <th>type</th>
                                </tr>
                                </thead>
                                <g:each in="${machineServers}" var="mServ">
                                        <tr>
                                            <td><a href="<g:createLink controller="WebServer" action="getWebServer" params="[name:mServ?.name, type:mServ?.serverType.toString(), port:mServ?.portNumber]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>
                                            <td>${mServ?.name}</td>
                                            <td>${mServ?.portNumber}</td>
                                            <td>${mServ?.serverType}</td>
                                        </tr>
                                </g:each>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="panel panel-success">
                        <div class="panel-heading">
                            <h3 class="panel-title">Liste des applications locales</h3>
                        </div>
                        <div class="panel-body">
                            <table class="table table-hover table-striped">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Nom</th>
                                    <th>Description</th>
                                    <th>url</th>
                                </tr>
                                </thead>
                                <g:each in="${apps}" var="app">
                                    <g:if test="${app.url.contains(machine.name)}" >
                                    <tr>
                                        <td><a href="<g:createLink controller="AppRetail" action="app" params="[name:app?.name]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>
                                        <td>${app?.name}</td>
                                        <td>${app?.description}</td>
                                        <td><a target="_blank" href="${app?.url}">${app?.url}</a></td>
                                    </tr>
                                    </g:if>
                                </g:each>
                            </table>
                        </div>
                </div>
                %{--<div class="row">--}%
                        %{--<div class="panel panel-success">--}%
                            %{--<div class="panel-heading">--}%
                                %{--<h3 class="panel-title">Liste des applications référencées</h3>--}%
                            %{--</div>--}%
                            %{--<div class="panel-body">--}%
                                %{--<table class="table table-hover table-striped">--}%
                                    %{--<thead>--}%
                                    %{--<tr>--}%
                                        %{--<th>#</th>--}%
                                        %{--<th>Nom</th>--}%
                                        %{--<th>Description</th>--}%
                                        %{--<th>url</th>--}%
                                    %{--</tr>--}%
                                    %{--</thead>--}%
                    %{--<g:each in="${apps}" var="app">--}%
                        %{--<g:if test="${!app.url.contains(machine.name)}" >--}%
                            %{--<tr>--}%
                                %{--<td><a href="<g:createLink controller="AppRetail" action="app" params="[name:app?.name]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>--}%
                                %{--<td>${app?.name}</td>--}%
                                %{--<td>${app?.description}</td>--}%
                                %{--<td><a target="_blank" href="${app?.url}">${app?.url}</a></td>--}%
                            %{--</tr>--}%
                        %{--</g:if>--}%
                    %{--</g:each>--}%
                    %{--</table>--}%
                %{--</div>--}%
            %{--</div>--}%
            </g:if>
            <g:else>
                <div class="well well-lg">Merci de choisir une machine dans la liste !</div>
            </g:else>
            </div>
        </div>
    </div>
</div>

<r:layoutResources/>
</body>
</html>
