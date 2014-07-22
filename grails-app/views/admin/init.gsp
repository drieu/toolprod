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
                        <label for="appLst">Choix du fichier à parser</label>
                        <input type="file" id="appLst" name="appLst">
                        <p class="help-block">Vous devez choisir un fichier httpd.conf</p>
                    </div>
                    <div class="form-group">
                        <label for="machinename">Nom de la machine</label>
                        <input type="text" class="form-control" id="machinename" name="machinename" placeholder="web3.ac-limoges.fr" value="">
                    </div>
                    <button type="submit" class="btn btn-info">Submit</button>
                </form>

            </g:uploadForm>
        </div>
    </div>
</div>
<r:layoutResources/>
</body>
</html>