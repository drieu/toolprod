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

    <div class="panel panel-info">
        <div class="panel-heading">
            <h3 class="panel-title">Import du fichier de VIP</h3>
        </div>
        <div class="panel-body">
            <g:if test="${flash.error}">
                <div class="alert alert-warning alert-dismissable">${flash.error}</div>
            </g:if>
            <g:if test="${flash.message}">
                <div class="alert alert-success alert-dismissable">${flash.message}</div>
            </g:if>


            <g:uploadForm action="initPortals" controller="admin">

                <form role="form">
                    <div class="form-group">
                        <label for="files">Choix du fichier à parser</label>
                        <input type="file" id="files" name="files[]" multiple>
                        <p class="help-block">Vous devez choisir le fichier bigip.conf</p>
                    </div>
                    <button type="submit" class="btn btn-info">Submit</button>
                </form>

            </g:uploadForm>
            <g:link controller="admin" action="initFromArena">Etape 4 : Initialisation à partir du fichier XML ARENA</g:link>
        </div>
    </div>

    <br/>
    <div class="well well-lg">Les données en base ne sont ni effacées ni mises à jour.Si elles n'existent pas, elles sont créees.</div>
    <br/>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Données chargées en base</h3>
        </div>
        <div class="panel-body">
            <table class="table table-hover table-striped">
                <caption>
                    Liste des Vip en base
                </caption>
                <thead>
                <tr>
                    <th>Nom</th>
                </tr>
                </thead>
                <g:each in="${toolprod.Vip.findAll()}" var="vip">
                    <g:if test="${vip != null}" >
                        <tr>
                            <td>${vip?.name}</td>
                        </tr>
                    </g:if>
                </g:each>
            </table>
        </div>
    </div>
</div>
</body>
</html>