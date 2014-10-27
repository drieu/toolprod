<%@ page import="toolprod.Portal; toolprod.IndexController" %>
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
                    Liste des machines &nbsp;&nbsp;<span class="badge">${machines?.size()}</span>
                </h4>
            </div>
            <div class="panel-body">


                <table class="table table-hover table-striped">
                    <thead>
                    <caption>
                        Liste des machines
                    </caption>
                    <tr>
                        <th>#</th>
                        <th>Nom</th>
                        <th>IP</th>
                    </tr>
                    </thead>
                    <g:each in="${machines}" var="machine">
                        <tr>
                            <td></td>
                            <td>${machine?.name}</td>
                            <td>${machine?.ipAddress}</td>
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
