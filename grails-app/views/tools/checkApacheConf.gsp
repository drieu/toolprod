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
    <g:applyLayout name="menu" />

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Choix des fichiers de configuration Apache à vérifier</h3>
        </div>
        <div class="panel-body">
            <g:if test="${flash.error}">
                <div class="alert alert-warning alert-dismissable">${flash.error}</div>
            </g:if>
            <g:if test="${flash.message}">
                <div class="alert alert-success alert-dismissable">${flash.message}</div>
            </g:if>

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
            <div class="alert alert-success" role="alert">Les données de la table de Check sont effacées à chaque clic !</div>
        </div>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">Erreurs dans les fichiers Apache  <span class="badge">${checks?.size()}</span></h3>
        </div>
        <div class="panel-body">

            <table class="table table-hover table-striped">
                <thead>
                    <caption>
                        Etat des fichiers de configuration Apache
                    </caption>
                    <tr>
                        <th>Machine</th>
                        <th>Fichier à vérifier</th>
                        <th>ServerName</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${checks}" var="check">
                        <g:if test="${!check?.machineName.equals(check?.confServerName)}">
                            <tr>
                                <td>${check?.machineName}</td>
                                <td>${check?.fileName}</td>
                                <td>${check?.confServerName}</td>
                                <td>
                                    <span class="label label-danger">KO</span>
                                </td>
                            </tr>
                        </g:if>
                    </g:each>
                </tbody>
            </table>
        </div>
    </div>
</div>
<r:layoutResources/>
</body>
</html>