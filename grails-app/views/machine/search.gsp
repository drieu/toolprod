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

    <div class="row">
        <div class="col-md-3">
        </div>
        <div class="col-md-9">
            <g:if test="${searchResults == null}">
                <div class="alert alert-success alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <p class="alert-link">Search return no result !</p>
                </div>
            </g:if>
            <g:if test="${searchResults != null && searchResults.isEmpty()}">
                <div class="alert alert-success alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <p class="alert-link">Search return no result !</p>
                </div>
            </g:if>
            <g:if test="${searchResults != null && !searchResults.isEmpty()}">
                <div class="row">

                    <div class="alert alert-success alert-dismissable">
                        <i>Résultats de la recherche&nbsp;&nbsp;<span class="badge">${searchResults?.size()}</span></i>
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <table class="table table-hover table-striped">
                            <tbody>
                            <g:each in="${searchResults}" var="app">
                                <tr>
                                    <td><a href="<g:createLink controller="AppRetail" action="app" params="[name:app?.name]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>
                                    <td>${app?.name}</td>
                                    <td>${app?.description}</td>
                                    <td>
                                        <g:each in="${app?.urls}">
                                            <a href="${it}" target="_blank">${it}</a>&nbsp;<br/>
                                        </g:each>
                                    </td>
                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
                </div>
            </g:if>
        </div>
    </div>
</div>
</body>
</html>
