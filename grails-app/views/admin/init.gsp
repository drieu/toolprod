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

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Import de fichier de configuration Apache</h3>
        </div>
        <div class="panel-body">
            <g:if test="${flash.error}">
                <div class="alert alert-warning alert-dismissable">${flash.error}</div>
            </g:if>
            <g:if test="${flash.message}">
                <div class="alert alert-success alert-dismissable">${flash.message}</div>
            </g:if>

            <g:uploadForm action="init" controller="admin">
                <form role="form">
                    <div class="form-group">
                        <label for="files">Choix du fichier à parser</label>
                        <input type="file" id="files" name="files[]" multiple>
                        <p class="help-block">Vous devez choisir un fichier httpd.conf</p>
                    </div>
                    <div class="form-group">
                        <label for="machinename">Nom de la machine</label>
                        <input type="text" class="form-control" id="machinename" name="machinename" placeholder="....ac-limoges.fr" value="">
                    </div>
                    <div class="form-group">
                        <label for="machinename">Portails</label>
                        <select name='portalsChoice' multiple class="form-control">
                            <g:each in="${portals}" var="portal">
                                <option>${portal.name}</option>
                            </g:each>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-info">Submit</button>
                </form>
            </g:uploadForm>
        </div>
    </div>
</div>
</body>
</html>