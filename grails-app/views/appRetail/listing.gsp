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
        <div class="col-md-9">
            <div class="panel-heading">
                <h4 class="panel-title">
                    Liste des applications &nbsp;&nbsp;<span class="badge">${apps?.size()}</span>
                </h4>
            </div>
            <div class="panel-body">
                <table class="table table-hover table-striped">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Nom</th>
                        <th>url</th>
                        <th>Portail </th>
                    </tr>
                    </thead>
                    <g:each in="${apps}" var="app">
                        <tr>
                            <td><a href="<g:createLink controller="AppRetail" action="app" params="[name:app?.name]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>
                            <td>${app?.name}</td>
                            <td>${app?.url}</td>
                        </tr>
                    </g:each>
                </table>
            </div>
        </div>
    </div>
</div>

<r:layoutResources/>
</body>
</html>
