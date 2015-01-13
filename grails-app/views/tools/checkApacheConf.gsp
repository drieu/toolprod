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
%{--    // TODO : voir export plugin et export format !--}%
</head>
<body>
<div class="container">
    <g:applyLayout name="menu" />
    <g:if test="${flash.error}">
        <div class="alert alert-warning alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            ${flash.error}</div>
    </g:if>
    <g:if test="${flash.message}">
        <div class="alert alert-success alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            ${flash.message}
        </div>
    </g:if>

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Choix des fichiers de configuration Apache à vérifier</h3>
        </div>
        <div class="panel-body">
            <g:uploadForm action="checkApacheConf" controller="tools">
                <form role="form">
                    <div class="form-group">
                        <label for="files">Choix du ou des fichiers à parser</label>
                        <input type="file" id="files" name="files[]" multiple>
                        <p class="help-block">Vous devez choisir un ou plusieurs fichier httpd.conf</p>
                    </div>
                    <div class="form-group">
                        <label for="machinename">Nom de la machine</label>
                        <input type="text" class="form-control" id="machinename" name="machinename" placeholder="....ac-limoges.fr" value="">
                    </div>
                    <button type="submit" class="btn btn-info">Vérification</button>
                </form>
            </g:uploadForm>
            <br/>
            <g:uploadForm action="clearCheckTable" controller="tools">
                <form role="form">
                    <button type="submit" class="btn btn-info">Effacer les données</button>
                </form>
            </g:uploadForm>
            <br/>
            <div class="alert alert-success" role="alert">Les données de la table de Check sont effacées à chaque clic !</div>
        </div>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">Erreurs dans les fichiers Apache  <span class="badge">${count}</span></h3>
        </div>
        <div class="panel-body">

            <table class="table table-hover table-striped">
                <caption>
                    Etat des fichiers de configuration Apache
                </caption>
                <thead>
                    <tr>
                        <th>Machine</th>
                        <th>Fichier à vérifier</th>
                        <th>ServerName</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${checks}" var="check">
                        <tr>
                            <td>${check?.machineName}</td>
                            <td>${check?.fileName}</td>
                            <td>${check?.name}</td>
                            <td>
                                <span class="label label-warning">WARNING</span>
                            </td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            %{--<export:formats />--}%
        </div>
    </div>
</div>
</body>
</html>