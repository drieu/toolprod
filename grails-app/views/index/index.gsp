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
    <nav class="navbar navbar-default" role="navigation">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">Tprod</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Serveurs <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="#">Serveurs apache</a></li>
                            <li class="divider"></li>
                            <li><a href="#">Serveur Ldap</a></li>
                            <li class="divider"></li>
                            <li><a href="#">Autres Serveurs</a></li>
                            <li class="divider"></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Bases <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="#">Base DB2</a></li>
                            <li class="divider"></li>
                            <li><a href="#">Base Mysql</a></li>
                            <li class="divider"></li>
                            <li><a href="#">Base SQL Server</a></li>
                            <li class="divider"></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Applications <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="#">Application Weblogic</a></li>
                            <li class="divider"></li>
                            <li><a href="#">Application PHP</a></li>
                            <li class="divider"></li>
                            <li><a href="#">Autre</a></li>
                            <li class="divider"></li>
                        </ul>
                    </li>
                </ul>

                <ul class="nav navbar-nav navbar-right">
                    %{--<form class="navbar-form navbar-left" role="search" controller="IndexController" action="search" method="GET">--}%
                        <g:form class="navbar-form navbar-left" role="search" controller="index" action="search" method="GET">
                        <div class="form-group">
                                <g:textField class="form-control" name="query" value="${params.query}" placeholder="serveur, application ..."/>
                        </div>
                        </g:form>
                    %{--</form>--}%
                    <li><a href="#">Wiki</a></li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Outils <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="#">Recherche dans le LDAP</a></li>
                            <li><a href="/toolprod/admin">Page d'admin</a></li>
                        </ul>
                    </li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>

    <div class="row">
        <div class="col-md-3">
            <div class="list-group">
                <a href="#" class="list-group-item active">
                    Machines
                </a>
                <g:each in="${machines}" var="machine">
                    <a href="<g:createLink action="getMachineApps" params="[machine:machine.name]" />" class="list-group-item">
                            ${machine.name}
                    </a>
                </g:each>
            </div>



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
                        <i>Résultats de la recherche</i>
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <table class="table table-hover table-striped">
                            <tbody>
                            <g:each in="${searchResults}" var="app">
                                <tr>
                                    <td></td>
                                    <td>${app?.name}</td>
                                    <td>${app?.description}</td>
                                    <td>${app?.url}</td>
                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
                </div>
           </g:if>
            <div class="row">
                <p class="text-left">
                    <dl class="dl-horizontal">
                        <dt>Machine </dt>
                        <dd>Nom : ${machine?.name}</dd>
                        <dd>IP : ${machine?.ipAddress}</dd>
                    </dl>
                </p>
            </div>
            <div class="row">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Liste des serveurs web</h3>
                    </div>
                    <div class="panel-body">
                        <table class="table table-hover table-striped">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Nom</th>
                                <th>Description</th>
                                <th>type</th>
                            </tr>
                            </thead>
                            <g:each in="${machineServers}" var="mServ">
                                <tr>
                                    <td></td>
                                    <td>${mServ?.name}</td>
                                    <td>${mServ?.portNumber}</td>
                                    <td>${mServ?.serverType}</td>
                                </tr>
                            </g:each>
                        </table>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Liste des applications</h3>
                    </div>
                    <div class="panel-body">
                        <table class="table table-hover table-striped">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Nom</th>
                                <th>Description</th>
                                <th>url</th>
                            </tr>
                            </thead>
                            <g:each in="${apps}" var="app">
                                <tr>
                                    <td></td>
                                    <td>${app?.name}</td>
                                    <td>${app?.description}</td>
                                    <td>${app?.url}</td>
                                </tr>
                            </g:each>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<r:layoutResources/>
</body>
</html>
