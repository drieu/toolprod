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