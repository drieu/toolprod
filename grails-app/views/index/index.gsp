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
                    <form class="navbar-form navbar-left" role="search">
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="serveur, application ...">
                        </div>
                        <button type="submit" class="btn btn-default">Recherche</button>
                    </form>
                    <li><a href="#">Wiki</a></li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Outils <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="#">Recherche dans le LDAP</a></li>
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
                    Serveurs apache
                </a>
                <a href="#" class="list-group-item">web1</a>
                <a href="#" class="list-group-item">web2</a>
                <a href="#" class="list-group-item">web3</a>
                <a href="#" class="list-group-item">web4</a>
            </div>



        </div>

        <div class="col-md-9">
            test
        </div>
    </div>
</div>

<r:layoutResources/>
</body>
</html>
