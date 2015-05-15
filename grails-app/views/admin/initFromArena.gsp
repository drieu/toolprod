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

        <div class="panel panel-danger">
            <div class="panel-heading">
                <h3 class="panel-title">Import du fichier XML ARENA</h3>
            </div>
            <div class="panel-body">
                <g:if test="${flash.error}">
                    <div class="alert alert-warning alert-dismissable">${flash.error}</div>
                </g:if>
                <g:if test="${flash.message}">
                    <div class="alert alert-success alert-dismissable">${flash.message}</div>
                </g:if>


                <g:uploadForm action="initFromArena" controller="admin">

                    <form role="form">
                        <div class="form-group">
                            <label for="files">Choix du fichier XML ARENA</label>
                            <input type="file" id="files" name="files[]" multiple>
                            <p class="help-block">Vous devez choisir le fichier xml d'ARENA</p>
                        </div>
                        <button type="submit" class="btn btn-info">Submit</button>
                    </form>

                </g:uploadForm>
            </div>
        </div>
        <div class="well well-lg">Ce menu permet de créer des applications de type VIP</div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h3 class="panel-title">Données chargées en base</h3>
            </div>
            <div class="panel-body">
                <table class="table table-hover table-striped">
                    <caption>
                        Liste des applications non importées.
                    </caption>
                    <thead>
                    <tr>
                        <th>Nom</th>
                    </tr>
                    </thead>
                    <g:each in="${errs}" var="err">
                        <g:if test="${err != null}" >
                            <tr>
                                <td>${err}</td>
                            </tr>
                        </g:if>
                    </g:each>
                </table>

            </div>
        </div>
    </div>
</body>
</html>