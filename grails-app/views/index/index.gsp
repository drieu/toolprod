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

            <g:each in="${machineGroups}" var="machineGroup">
                <g:if test="${machineGroup.groupName.equals(selectedMachineGroup)}">
                    <a href="#" class="list-group-item active">
                        Machines ${machineGroup.groupName}
                    </a>
                    <g:each in="${machines}" var="machine">
                        <g:each in="${machineGroup.regex}" var="regex">
                                <g:if test="${machine.name.contains(regex)}">
                                    <a href="<g:createLink action="getMachineApps" params="[machine:machine.name,group:machineGroup.groupName]" />" class="list-group-item">
                                        ${machine.name}
                                    </a>
                                </g:if>
                        </g:each>
                    </g:each>
                    <br/>
                </g:if>
            </g:each>
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
                        <i>Résultats de la recherche&nbsp;&nbsp;<span class="badge">${searchResults?.size()}</span></i>
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <table class="table table-hover table-striped">
                            <tbody>
                            <g:each in="${searchResults}" var="app">
                                <tr>
                                    <td><a href="<g:createLink controller="AppRetail" action="app" params="[name:app?.name]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>
                                    <td>${app?.name}</td>
                                    <td>${app?.description}</td>
                                    <td>
                                        <g:each in="${app?.urls}">
                                            <a href="${it}" target="_blank">${it}</a>&nbsp;<br/>
                                        </g:each>
                                    </td>
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
                            %{--<dd>Ping machine :<%--}%
                                %{--boolean status = false--}%
                                %{--try {--}%
                                    %{--def address = InetAddress.getByName(machine?.name);--}%
                                    %{--def timeoutMillis = 3000 ;--}%
                                    %{--if (address.isReachable(timeoutMillis)) {--}%
                                        %{--status = true--}%
                                    %{--}--}%
                                %{--} catch( Exception e) {--}%
                                    %{--status = false--}%
                                %{--}--}%
                                %{--%>--}%
                                %{--<g:if test="${status}">--}%
                                    %{--<span class="label label-success">OK</span>--}%
                                %{--</g:if>--}%
                                %{--<g:else>--}%
                                    %{--<span class="label label-danger">KO</span>--}%
                                %{--</g:else>--}%
                                ${machine?.ipAddress}</dd>
                        </dl>
                    </p>
                </div>
                <div class="row">
                    <div class="panel-group" id="accordion">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                        Liste des serveurs web&nbsp;&nbsp;<span class="badge">${machineServers?.size()}</span>
                                    </a>
                                </h4>
                            </div>
                            <div id="collapseOne" class="panel-collapse collapse">
                                <div class="panel-body">
                                    <table class="table table-hover table-striped">
                                        <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Nom</th>
                                            <th>Liste des applications</th>
                                            <th>Port</th>
                                            <th>type</th>
                                        </tr>
                                        </thead>
                                        <g:each in="${machineServers}" var="mServ">
                                            <tr>
                                                <td><a href="<g:createLink controller="WebServer" action="getWebServer" params="[name:mServ?.name, type:mServ?.serverType.toString(), port:mServ?.portNumber]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>
                                                <td>${mServ?.name}</td>
                                                <td><small>
                                                    <%
                                                        String result = "";
                                                        for(String str : mServ?.linkToApps) {
                                                            result += str;
                                                            result += " ";
                                                        }
                                                        print(result + " ")
                                                    %></small></td>
                                                <td>${mServ?.portNumber}</td>
                                                <td>${mServ?.serverType}</td>
                                            </tr>
                                        </g:each>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
                                        Liste des applications locales&nbsp;&nbsp;<span class="badge">${apps?.size()}
                                    </a>
                                </h4>
                            </div>
                            <div id="collapseTwo" class="panel-collapse collapse in">
                                <div class="panel-body">
                                    <table class="table table-hover table-striped">
                                        <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Nom</th>
                                            <th>Description </th>
                                            <th>url</th>
                                        </tr>
                                        </thead>
                                        <g:each in="${apps}" var="app">
                                                <tr>
                                                    <td><a href="<g:createLink controller="AppRetail" action="app" params="[name:app?.name]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>
                                                    <td>${app?.name}</td>
                                                    <td>${app?.description}</td>
                                                    <td>
                                                        <g:each in="${app?.urls}">
                                                            <a href="${it}" target="_blank">${it}</a>&nbsp;<br/>
                                                        </g:each>
                                                    </td>
                                                </tr>
                                        </g:each>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseThree">
                                        Liste des applications référencées&nbsp;&nbsp;<span class="badge">${refs?.size()}</span>
                                    </a>
                                </h4>
                            </div>
                            <div id="collapseThree" class="panel-collapse collapse">
                                <div class="panel-body">
                                    <table class="table table-hover table-striped">
                                        <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Nom</th>
                                        </tr>
                                        </thead>
                                        <g:each in="${refs}" var="ref">
                                                <tr>
                                                    <td><a href="<g:createLink controller="AppRetail" action="app" params="[name:ref]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>
                                                    <td>${ref}</td>
                                                </tr>

                                        </g:each>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </g:if>
            <g:else>
                <g:if test="${searchResults == null}">

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
                                        <g:each in="${toolprod.Check.findAll()}" var="check">
                                            <tr>
                                                <td>${check?.machineName}</td>
                                                <td>${check?.fileName}</td>
                                                <td>${check?.confServerName}</td>
                                                <td>
                                                    <span class="label label-warning">WARNING</span>
                                                </td>
                                            </tr>
                                        </g:each>
                                        </tbody>
                            </table>
                        </div>
                    </div>
                </g:if>
            </g:else>
            </div>
        </div>
    </div>

<r:layoutResources/>
</body>
</html>
