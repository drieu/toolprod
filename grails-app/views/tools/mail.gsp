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
            <h3 class="panel-title">Génération </h3>
        </div>
        <div class="panel-body">
            <form role="form">
                <div class="form-group">
                    <label for="rne">RNE</label>
                    <input type="text" class="form-control" id="rne" name="rne" placeholder="....087XXX" value="">
                </div>
                <br/>
                <select name="type" class="form-control">
                    <option selected>Choix du type de boîte</option>
                    <g:each in="${types}" var="type">
                        <option>${type}</option>
                    </g:each>
                </select>
                <br/>
                <button type="submit" class="btn btn-info">Génération</button>
            </form>
            <br/>
            <br/>
        </div>
    </div>

    <div class="panel panel-success">
        <div class="panel-heading">
            <h3 class="panel-title">Résultat </h3>
        </div>
        <div class="panel-body">
            <br/>
            <table class="table table-hover">
                <tr>
                    <td>Identifiant</td>
                    <td>${uid}</td>
                </tr>
                <tr>
                    <td>Mail</td>
                    <td>${mail}</td>
                </tr>
                <tr>
                    <td>Fichier LDAP</td>
                    <td><g:link action="downloadUID" params="[uid: uid, rne: rne, type: type, mail: mail]">Fichier LDAP</g:link></td>
                </tr>
            </table>
            <br/>
        </div>
    </div>
</div>
<r:layoutResources/>
</body>
</html>