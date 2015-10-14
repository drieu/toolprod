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
                <h3 class="panel-title">Archivage</h3>
            </div>
            <div class="panel-body">
                <g:uploadForm action="archive" controller="tools">
                    <form role="form">
                        <button type="submit" class="btn btn-info">Archivage</button>
                    </form>
                </g:uploadForm>
                <br/>
                <label>Dernière archive : ${archive?.date}</label>
                <br/>
                <g:uploadForm action="sendMail" controller="tools">
                    <form role="form">
                        <label for="dest">Envoyer à :</label>
                        <input type="text" id="dest" name="dest" />
                        <button type="submit" class="btn btn-info">Send mail</button>
                    </form>
                </g:uploadForm>
                <br/>
            </div>
        </div>
        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">Listes des vérifications</h3>
            </div>
            <div class="panel-body">

                <table class="table table-hover table-striped">
                    <tbody>
                            <tr>
                                <td>Liste des nouvelles machines </td>
                                <td>${machineDiffs.toString()}</td>
                                <td>
                                    <g:if test="${machineDiffs.size() == 0}">
                                        <span class="label label-success">OK</span>
                                    </g:if>
                                    <g:else>
                                        <span class="label label-warning">warning</span>
                                    </g:else>
                                </td>
                            </tr>
                            <tr>
                                <td>Listes des applications en PLUS</td>
                                <td>${appDiffs.toString()}</td>
                                <td>
                                    <g:if test="${appDiffs.size() == 0}">
                                        <span class="label label-success">OK</span>
                                    </g:if>
                                    <g:else>
                                        <span class="label label-warning">warning</span>
                                    </g:else>
                                </td>
                            </tr>
                            <tr>
                                <td>Listes des applications en MOINS</td>
                                <td>${disappearApps.toString()}</td>
                                <td>
                                    <g:if test="${disappearApps.size() == 0}">
                                        <span class="label label-success">OK</span>
                                    </g:if>
                                    <g:else>
                                        <span class="label label-warning">warning</span>
                                    </g:else>
                                </td>
                            </tr>
                            <tr>
                                <td>Nombre d'applications</td>
                                <td>${count}</td>
                                <td>
                                    <g:if test="${count == 0}">
                                        <span class="label label-success">OK</span>
                                    </g:if>
                                    <g:else>
                                        <span class="label label-warning">warning</span>
                                    </g:else>
                                </td>
                            </tr>
                    </tbody>
                </table>
                %{--<export:formats />--}%
            </div>
        </div>
        <g:render template="/layouts/footer"></g:render>
    </div>
</body>
</html>