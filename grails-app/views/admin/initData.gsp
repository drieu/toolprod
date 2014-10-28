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

    <div class="panel panel-success">
    <div class="panel-heading">
        <h3 class="panel-title">Import de fichier de configuration des données</h3>
    </div>
    <div class="panel-body">
        <g:if test="${flash.error}">
            <div class="alert alert-warning alert-dismissable">${flash.error}</div>
        </g:if>
        <g:if test="${flash.message}">
            <div class="alert alert-success alert-dismissable">${flash.message}</div>
        </g:if>


        <g:uploadForm action="initData" controller="admin">

            <form role="form">
                <div class="form-group">
                    <label for="files">Choix du fichier à parser</label>
                    <input type="file" id="files" name="files[]" multiple>
                    <p class="help-block">Vous devez choisir un fichier de config</p>
                </div>
                <button type="submit" class="btn btn-info">Submit</button>
            </form>

        </g:uploadForm>
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
                    Liste des Portails en base
                </caption>
                <thead>
                    <tr>
                        <th>Nom</th>
                    </tr>
                </thead>
                <g:each in="${portals}" var="portal">
                    <g:if test="${portal != null}" >
                        <tr>
                            <td>${portal?.name}</td>
                        </tr>
                    </g:if>
                </g:each>
            </table>
            <br/>
            <table class="table table-hover table-striped">
                <caption>
                    Liste des groupes de machines
                </caption>
                <thead>
                    <tr>
                        <th>Nom</th>
                    </tr>
                </thead>
                <g:each in="${machinesGroups}" var="machinesGroup">
                    <g:if test="${machinesGroup != null}" >
                        <tr>
                            <td>${machinesGroup?.groupName}</td>
                        </tr>
                    </g:if>
                </g:each>
            </table>

        </div>
    </div>
</div>
<r:layoutResources/>
</body>
</html>