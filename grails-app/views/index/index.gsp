<%@ page import="toolprod.Vip; toolprod.IndexController" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Welcome to Production !</title>
    <asset:stylesheet href="jquery.dataTables.css"/>

    <asset:javascript src="jquery.js"/>

    <asset:javascript src="jquery.dataTables.min.js"/>
    <asset:javascript src="application.js"/>
    <asset:stylesheet href="bootstrap/bootstrap.css"/>
    <asset:javascript src="bootstrap/bootstrap.js"/>
</head>

<body>
    <div class="container">
        <g:applyLayout name="menu" />

        <div class="row">
            <div class="col-xs-12">
                    <blockquote>
                        <h1>Bienvenue sur Toolprod</h1>
                        <footer>Une boîte à outil pour la production</footer>
                    </blockquote>
                    <br/>
                    <br/>
            </div>
        </div>


        <div class="row">
            <div class="col-xs-12">
                <ul class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#sectionA">Liste des applications &nbsp;&nbsp;<span class="badge">${count}</span></a></li>
                    <li><a data-toggle="tab" href="#sectionB">VIP</a></li>
                </ul>

                <div class="tab-content">

                    <div id="sectionA" class="tab-pane fade in active">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="panel-body">
                                    <table id="applis" class="display">
                                        <thead>
                                        <tr>
                                            <th>Nom</th>
                                            <th>Url</th>
                                            <th>Vip</th>
                                            <th>Port</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>...</td>
                                            <td>...</td>
                                            <td>...</td>
                                            <td>...</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="sectionB" class="tab-pane fade">

                        <div class="row">
                            <div class="col-xs-12">
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

                    </div>
                </div>
                </div>
            </div>
            <g:render template="/layouts/footer"></g:render>
        </div>
</body>
</html>
<script>
    ${raw(data)}
    $(document).ready(function() {
        $('#applis').dataTable({
            "data": dataSet,
            "lengthMenu": [[10, 30, 40, -1], [10, 30, 40, "All"]],
            "columnDefs": [
                { "width": "15%", "targets": 0 },
                { "width": "10%", "targets": 1 },
                { "width": "30%", "targets": 2 },
                { "width": "45%", "targets": 3 }
            ],
            "language": {
                "lengthMenu": "Affiche _MENU_ résultats par page",
                "zeroRecords": "Aucun résultat - désolé",
                "info": "Affichage page _PAGE_ / _PAGES_",
                "infoEmpty": "Aucun enregistrement",
                "infoFiltered": "( nombre de résultats _MAX_ )"
            },
            "columns": [
                { "title": "Nom" },
                { "title": "Vip" },
                { "title": "Serveur" },
                { "title": "Port" }
            ]
        });
    });
</script>
