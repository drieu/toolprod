<%@ page import="toolprod.Vip; toolprod.TreeNode" %>

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
    <g:applyLayout name="menu"/>

    <div class="row">
        <div class="col-xs-3">
            <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    Choix d'une VIP <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <g:each in="${Vip.findAll()}" var="vip">
                        <li><a href="#">${vip?.name}</a></li>
                    </g:each>
                </ul>
            </div>
        </div>
    </div>
    <br/>
    <div class="row">
        <div class="col-xs-3">
            <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    Choix d'un serveur web <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <g:each in="${toolprod.Server.findByNameIlike("web%")}" var="server">
                        <li><a href="#">${server?.name}</a></li>
                    </g:each>
                </ul>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-3">

        </div>
        <div class="col-xs-9">
            <button type="button" class="btn btn-danger">Imprimer</button>
        </div>
    </div>
</div>
</body>
</html>