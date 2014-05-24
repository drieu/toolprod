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
    <g:applyLayout name="menu"/>
    <div class="col-md-4">

        <ul class="list-group">
            <a href="#" class="list-group-item active">Application status</a>
            <li class="list-group-item">App version: <g:meta name="app.version"/></li>
            <li class="list-group-item">Grails version: <g:meta name="app.grails.version"/></li>
            <li class="list-group-item">Groovy version: ${GroovySystem.getVersion()}</li>
            <li class="list-group-item">JVM version: ${System.getProperty('java.version')}</li>
            <li class="list-group-item">Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
            <li class="list-group-item">Controllers: ${grailsApplication.controllerClasses.size()}</li>
            <li class="list-group-item">Domains: ${grailsApplication.domainClasses.size()}</li>
            <li class="list-group-item">Services: ${grailsApplication.serviceClasses.size()}</li>
            <li class="list-group-item">Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
        </ul>

        <ul class="list-group">
            <a href="#" class="list-group-item active">Liste des controllers</a>
            <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName }}">
                <li class="list-group-item"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
            </g:each>
        </ul>
    </div>

    <div class="col-md-8">

        <div id="page-body" role="main">
            <h1>Bienvenue sur la page d'admin</h1>

            <ul class="nav nav-pills nav-stacked">
                <li><g:link controller="admin" action="init">Import de fichiers Httpd.conf</g:link></li>
                <li></li>
            </ul>


        </div>

        <br/><br/>
        <table class="table table-hover">
            <caption>Plugins</caption>
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Version</th>
                </tr>
            </thead>
            <tbody>
                <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
                    <tr>
                        <td>${plugin.name}</td>
                        <td>${plugin.version}</td>
                    </tr>
                </g:each>
            </tbody>
        </table>
    </div>
</div>
<r:layoutResources/>
</body>
</html>
