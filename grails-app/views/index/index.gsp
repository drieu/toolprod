<%@ page import="toolprod.Vip; toolprod.IndexController" %>
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
                    %{--<div class="well well-lg">--}%
                        %{--Pour le moment, cet outil permet de :--}%
                        %{--<ul>--}%
                            %{--<li>Répertoriez les applications existantes</li>--}%
                            %{--<li>Répertoriez les machines, les serveurs apache et weblogic pour chaque application</li>--}%
                            %{--<li>Faire un contrôle simple des fichiers Apache</li>--}%
                        %{--</ul>--}%
                    %{--</div>--}%
                    <br/>
        </div>
        <div class="row">
            <h3>Liste des VIP </h3>
            <div class="panel-group" id="accordion">
                <% def panel = "panel-info" %>
                <g:each in="${Vip.findAll()}" var="vip">
                        <g:if test="${panel=='panel-info'}">
                            <% panel = "panel-success" %>
                        </g:if>
                        <g:else>
                            <% panel = "panel-info" %>
                        </g:else>

                        <div class="panel ${panel}">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a data-toggle="collapse" data-parent="#accordion" href="#collapse${vip?.name}_${vip?.type}">
                                        ${vip?.name}
                                    </a>
                                    &nbsp;&nbsp&nbsp<small>( nom technique : ${vip.technicalName} )</small>
                                </h4>
                            </div>
                            <div id="collapse${vip?.name}_${vip?.type}" class="panel-collapse collapse">
                                <div class="panel-body">
                                    <table class="table table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Nom</th>
                                                <th>Port</th>
                                                <th>Applications</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <g:each in="${vip.servers}" var="server">
                                                <tr>
                                                    <td><a href="<g:createLink controller="webServer" action="getWebServerByMachineName" params="[name:server?.machineHostName, type:'apache', port: server?.portNumber]" />">
                                                        <span class="glyphicon glyphicon-search" aria-hidden="true"></span></a>
                                                    </td>
                                                    <td>
                                                       ${server?.name}
                                                    </td>
                                                    <td>${server?.portNumber}</td>
                                                    <td>
                                                        <g:each in="${server?.linkToApps}" var="linkAppName">
                                                                <a href="<g:createLink controller="AppRetail" action="app" params="[name:linkAppName]" />">${linkAppName}</a>
                                                        </g:each>
                                                    </td>
                                                </tr>
                                            </g:each>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                </g:each>

            </div>
        </div>

    </div>

</body>
</html>
