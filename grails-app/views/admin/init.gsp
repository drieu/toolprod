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
<div class="content scaffold-create" role="main">
    <h1>Upload Your apps list</h1>
    <g:if test="${flash.error}">
        <div class="alert alert-error" style="display: block">${flash.error}</div>
    </g:if>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:uploadForm action="init" controller="admin">
        <fieldset class="form">
            <input type="file" name="appLst" />
        </fieldset>
        Server name:<input type="text" name="servername" value="web3.ac-limoges.fr"/>
        <fieldset class="buttons">
            <g:submitButton name="upload" class="save" value="Upload" />
        </fieldset>
    </g:uploadForm>
</div>
<r:layoutResources/>
</body>
</html>