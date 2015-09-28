<%@ page import="toolprod.MachineGroup; toolprod.IndexController" %>
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
            <div class="col-xs-12">

                        <a href="#" class="list-group-item active">
                            Machines ${selectedMachineGroup}
                        </a>
                        <% int cpt =0; %>
                        <g:each in="${machines}" var="machine">
                            <g:form name="form-id_${cpt}" action="getMachineApps" method="POST">
                                <g:hiddenField name="machine" value="${machine.name}" />
                                <g:hiddenField name="group" value="${selectedMachineGroup}" />
                                <li class="list-group-item" >
                                    <a href="#" onclick="document.getElementById('form-id_${cpt}').submit();"> ${machine.name} </a>
                                </li>
                            </g:form>
                            <% cpt =cpt + 1; %>
                        </g:each>
                        <br/>
            </div>
        </div>
        <div class="row">

            <div class="col-xs-12">

                <g:if test="${machine != null}">
                        <h1>${machine?.name}</h1>

                        <div class="panel-group" id="accordion">
                            <div class="panel panel-info">
                                <div class="panel-heading">
                                    <h4 class="panel-title">
                                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                            Liste des serveurs web&nbsp;&nbsp;<span class="badge">${machineServers?.size()}</span>
                                        </a>
                                    </h4>
                                </div>
                                <div id="collapseOne" class="panel-collapse collapse in">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <table class="table table-hover table-striped table-condensed">
                                                <thead>
                                                <tr>
                                                    <th>#</th>
                                                    <th>Nom</th>
                                                    <th>Port</th>
                                                    <th>Liste des applications</th>
                                                    <th>type</th>
                                                </tr>
                                                </thead>
                                                <g:each in="${machineServers}" var="mServ">
                                                    <tr>
                                                        <td>
                                                            <a href="<g:createLink controller="WebServer" action="getWebServer" params="[name:mServ?.name, type:mServ?.serverType.toString(), port:mServ?.portNumber]" />">
                                                                <g:if test="${mServ?.serverType.toString() == 'APACHE'}">
                                                                    <asset:image src="apache_icon.jpg" width="32" height="32"/>
                                                                </g:if>
                                                                <g:elseif test="${mServ?.serverType.toString() == 'WEBLOGIC'}">
                                                                    <asset:image src="weblogic.jpg" width="32" height="32"/>
                                                                </g:elseif>
                                                                <g:else>
                                                                    <span class="glyphicon glyphicon-zoom-in"></span>
                                                                </g:else>
                                                            </a>
                                                        </td>
                                                        <td>
                                                            <a href="<g:createLink controller="WebServer" action="getWebServer" params="[name:mServ?.name, type:mServ?.serverType.toString(), port:mServ?.portNumber]" />">
                                                                ${mServ?.name}
                                                            </a>
                                                        </td>
                                                        <td>${mServ?.portNumber}</td>
                                                        <td><small>
                                                            <%
                                                                String result = "";
                                                                for(String str : mServ?.linkToApps) {
                                                                    result += str;
                                                                    result += " ";
                                                                }
                                                                print(result + " ")
                                                            %></small></td>
                                                        <td>${mServ?.serverType}</td>
                                                    </tr>
                                                </g:each>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            %{--UNUSED--}%
                            %{--<div class="panel panel-default">--}%
                                %{--<div class="panel-heading">--}%
                                    %{--<h4 class="panel-title">--}%
                                        %{--<a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">--}%
                                            %{--Liste des applications locales&nbsp;&nbsp;<span class="badge">${apps?.size()}--}%
                                        %{--</a>--}%
                                    %{--</h4>--}%
                                %{--</div>--}%
                                %{--<div id="collapseTwo" class="panel-collapse collapse">--}%
                                    %{--<div class="panel-body">--}%
                                        %{--<table class="table table-hover table-striped">--}%
                                            %{--<thead>--}%
                                            %{--<tr>--}%
                                                %{--<th>#</th>--}%
                                                %{--<th>Nom</th>--}%
                                                %{--<th>Description </th>--}%
                                                %{--<th>url</th>--}%
                                            %{--</tr>--}%
                                            %{--</thead>--}%
                                            %{--<g:each in="${apps}" var="app">--}%
                                                %{--<tr>--}%
                                                    %{--<td><a href="<g:createLink controller="AppRetail" action="app" params="[name:app?.name]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>--}%
                                                    %{--<td>${app?.name}</td>--}%
                                                    %{--<td>${app?.description}</td>--}%
                                                    %{--<td>--}%
                                                        %{--<g:each in="${app?.urls}">--}%
                                                            %{--<a href="${it}" target="_blank">${it}</a>&nbsp;<br/>--}%
                                                        %{--</g:each>--}%
                                                    %{--</td>--}%
                                                %{--</tr>--}%
                                            %{--</g:each>--}%
                                        %{--</table>--}%
                                    %{--</div>--}%
                                %{--</div>--}%
                            %{--</div>--}%
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
                </g:if>
            </div>
        </div>
        <g:render template="/layouts/footer"></g:render>
    </div>
</body>
</html>
