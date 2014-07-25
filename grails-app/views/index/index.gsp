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
            <g:render template="/shared/identity" var="${machines}" />
            <g:render template="/shared/frontaux" var="${machines}" />
            <g:render template="/shared/webgrh" var="${machines}" />
            <g:render template="/shared/sco" var="${machines}" />
            <g:render template="/shared/other" var="${machines}" />
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
                                    <td><a href="<g:createLink controller="AppRetail" action="app" params="[name:app?.name]" />""><span class="glyphicon glyphicon-zoom-in"></span></a></td>
                                    <td>${app?.name}</td>
                                    <td>${app?.description}</td>
                                    <td><a href="${app?.url}"  target="_blank">${app?.url}</a></td>
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
                                %{--${machine?.ipAddress}</dd>--}%
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
                                                    %>${result}</small></td>
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
                                                    <td><a target="_blank" href="${app?.url}">${app?.url}</a></td>
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
                    <div class="well well-lg">Merci de choisir une machine dans la liste !</div>
                </g:if>
            </g:else>
            </div>
        </div>
    </div>
</div>

<r:layoutResources/>
</body>
</html>
