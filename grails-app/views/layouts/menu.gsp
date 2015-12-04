<%@ page import="toolprod.MachineGroup" %>
<asset:stylesheet href="mybootstrap.css"/>

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
                <a class="navbar-brand" href="/toolprod/">
                    <asset:image src="logo.jpg"/>
                </a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Machines <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <g:each in="${MachineGroup.findAll()}" var="machineGroup">
                                <li class="smenu"><a href="/toolprod/machine/group?group=${machineGroup.groupName}">Machine ${machineGroup.groupName}</a></li>
                            </g:each>
                            <li class="divider"></li>
                            <li><a href="/toolprod/machine/group?group=ALL">Toutes les machines</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Serveurs <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="/toolprod/webServer/apache">Serveurs apache</a></li>
                            <li class="divider"></li>
                            <li><a href="/toolprod/webServer/weblogic">Serveur weblogic</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Applications<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="/toolprod/appRetail/listing">Liste des applications</a></li>
                            <li><a href="/toolprod/appRetail/viplisting">Imprimer</a></li>
                            <li><a href="/toolprod/appRetail/graph" target="_blank">Représentation graphiques des VIPS et des applications associees</a></li>
                            <li><a href="/toolprod/appRetail/graphmach" target="_blank">Représentation graphiques des machines et applications</a></li>
                            %{--<li><a href="/toolprod/appRetail/network" target="_blank">Network graph</a></li>--}%
                            %{--<li><a href="/toolprod/appRetail/status">Status</a></li>--}%
                        </ul>
                    </li>
                    <li><a href="http://dialdev2.ac-limoges.fr/wikipie/doku.php?id=accueil" target="_blank">Wiki</a></li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Outils <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            %{--<li><a href="">Recherche dans le LDAP</a></li>--}%
                            <li><a href="/toolprod/admin">Page d'admin</a></li>
                            <li><a href="/toolprod/tools/checkApacheConf">Vérification des fichiers Apache</a></li>
                            <li><a href="/toolprod/tools/checkAll">Vérification de la configuration</a></li>
                            <li><a href="/toolprod/tools/mail">Création automatique de fiche LDAP</a></li>
                        </ul>
                    </li>
                    %{--<li class="dropdown">--}%
                        %{--<a href="#" class="dropdown-toggle" data-toggle="dropdown">Bases <b class="caret"></b></a>--}%
                        %{--<ul class="dropdown-menu">--}%
                            %{--<li class="disabled"><a href="#">Base DB2</a></li>--}%
                            %{--<li class="divider"></li>--}%
                            %{--<li class="disabled"><a href="#">Base Mysql</a></li>--}%
                            %{--<li class="divider"></li>--}%
                            %{--<li class="disabled"><a href="#">Base SQL Server</a></li>--}%
                            %{--<li class="divider"></li>--}%
                        %{--</ul>--}%
                    %{--</li>--}%
                    %{--<li class="dropdown">--}%
                        %{--<a href="#" class="dropdown-toggle" data-toggle="dropdown">Applications <b class="caret"></b></a>--}%
                        %{--<ul class="dropdown-menu">--}%
                            %{--<li class="disabled"><a href="#">Application Weblogic</a></li>--}%
                            %{--<li class="divider"></li>--}%
                            %{--<li class="disabled"><a href="#">Application PHP</a></li>--}%
                            %{--<li class="divider"></li>--}%
                        %{--</ul>--}%
                    %{--</li>--}%
                </ul>

                <ul class="nav navbar-nav navbar-right">
                    <g:form class="navbar-form navbar-left" role="search" controller="machine" action="search" method="GET">
                        <div class="form-group">
                            <g:textField class="form-control" name="query" value="${params.query}" placeholder="recherche d'application ..."/>
                        </div>
                    </g:form>
                </ul>
            </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
