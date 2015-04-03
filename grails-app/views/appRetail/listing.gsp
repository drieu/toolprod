<%@ page import="toolprod.Portal; toolprod.IndexController" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Welcome to Production !</title>

    <asset:stylesheet href="jquery.dataTables.css"/>

    %{--<asset:javascript src="jquery-1.11.1.min.js"/>--}%
    <asset:javascript src="jquery.js"/>

    <asset:javascript src="jquery.dataTables.min.js"/>
    <asset:image src="sort_both.png"/>


    <asset:javascript src="application.js"/>
    <asset:stylesheet href="bootstrap/bootstrap.css"/>
    <asset:javascript src="bootstrap/bootstrap.js"/>
</head>

<body>
<div class="container">
    <g:applyLayout name="menu" />

    <div class="row">
        <div class="col-md-9">
            <div class="panel-heading">
                <h4 class="panel-title">
                    Liste des applications &nbsp;&nbsp;<span class="badge">${appBeans?.size()}</span>
                </h4>
            </div>
            <div class="panel-body">


                <div class="dropdown">
                    <button class="btn btn-default dropdown-toggle " type="button" id="dropdownMenu1" data-toggle="dropdown">
                        Choix du portail
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                        <g:each in="${portals}" var="portal">

                            <g:if test="${portalChoice.equals(portal.name)}">
                                <li role="presentation" class="active"><a role="menuitem" tabindex="-1" href="/toolprod/appRetail/listing/?choice=${portal.name}">${portal.name}</a></li>
                            </g:if>
                            <g:else>
                                <li role="presentation"><a role="menuitem" tabindex="-1" href="/toolprod/appRetail/listing/?choice=${portal.name}">${portal.name}</a></li>
                            </g:else>
                        </g:each>
                    </ul>
                </div>
                <br/>
                <br/>
                %{--<table class="table table-hover table-striped">--}%
                    %{--<thead>--}%
                        %{--<caption>--}%
                            %{--${portalChoice}--}%
                        %{--</caption>--}%
                        %{--<tr>--}%
                            %{--<th>#</th>--}%
                            %{--<th>Nom</th>--}%
                            %{--<th>url</th>--}%
                            %{--<th>Portail </th>--}%
                        %{--</tr>--}%
                    %{--</thead>--}%
                    %{--<g:each in="${appBeans}" var="app">--}%
                        %{--<g:if test="${portalChoice != null}" >--}%

                            %{--<g:if test="${app.portals.contains(portalChoice)}" >--}%
                                %{--<tr>--}%
                                    %{--<td><a href="<g:createLink controller="AppRetail" action="app" params="[name:app?.name]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>--}%
                                    %{--<td>${app?.name}</td>--}%
                                    %{--<td>--}%
                                        %{--<g:each in="${app?.serverUrls}">--}%
                                            %{--<a href="${it}">${it}</a>--}%
                                            %{--<br/>--}%
                                        %{--</g:each>--}%
                                        %{--</td>--}%
                                    %{--<th>--}%
                                        %{--<g:each in="${app?.portals?.findAll()}">--}%
                                            %{--<span class="label label-info">${it}</span>--}%
                                        %{--</g:each>--}%
                                    %{--</th>--}%
                                %{--</tr>--}%
                            %{--</g:if>--}%
                        %{--</g:if>--}%
                        %{--<g:else>--}%
                            %{--<tr>--}%
                                %{--<td><a href="<g:createLink controller="AppRetail" action="app" params="[name:app?.name]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>--}%
                                %{--<td>${app?.name}</td>--}%
                                %{--<td>--}%
                                    %{--<g:each in="${app?.serverUrls}">--}%
                                        %{--<a href="${it}">${it}</a>--}%
                                        %{--<br/>--}%
                                    %{--</g:each>--}%
                                %{--</td>--}%
                                %{--<th>--}%
                                    %{--<g:each in="${app?.portals?.findAll()}">--}%
                                        %{--<span class="label label-info">${it}</span>--}%
                                    %{--</g:each>--}%
                                %{--</th>--}%
                            %{--</tr>--}%
                        %{--</g:else>--}%
                    %{--</g:each>--}%
                %{--</table>--}%
                %{--<export:formats />--}%

                <table id="applis" class="display">
                    <thead>
                        <tr>
                            <th>Nom</th>
                            <th>Url</th>
                            <th>Vip</th>
                            <th>Vip</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>T. Nixon</td>
                            <td>System Architect</td>
                            <td>Edinburgh</td>
                            <td>61</td>
                        </tr>
                    </tbody>
                </table>

            </div>
        </div>
    </div>
</div>

</body>
</html>
<script>
    $(document).ready(function() {
        $('#applis').dataTable();
    });
</script>
