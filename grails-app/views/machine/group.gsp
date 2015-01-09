<%@ page import="toolprod.IndexController" %>
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

            <g:if test="${machine != null}">
                <div class="row">
                    <h1>${machine?.name}</h1>
                <br/>
                <br/>
                <p class="text-left">
                <dl class="dl-horizontal">
                    <dt>Détails </dt>

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
        </div>
    </div>
</div>
</body>
</html>
