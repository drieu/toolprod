<%--
  Created by IntelliJ IDEA.
  User: drieu
  Date: 27/05/14
  Time: 14:29
  To change this template use File | Settings | File Templates.
--%>
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
    <g:applyLayout name="menu"/>
    <g:if test="${app != null}">
        <h1>Application : ${app.name}</h1>
        <br/>
    </g:if>
    <g:else>
        <div class="well well-lg">Pas d'application !</div>
    </g:else>
</div>

<r:layoutResources/>
</body>
</html>