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

    <div class="row">
        <div class="col-md-3">
            <div class="list-group">
                <a href="#" class="list-group-item active">
                    Serveurs ${type}
                </a>
                <g:each in="${servers}" var="server">
                    <a href="<g:createLink action="getMachineApps" params="[server:server.name]" />" class="list-group-item">
                        ${server.name}
                    </a>
                </g:each>
            </div>
        </div>

        <div class="col-md-9">

        </div>
    </div>
    </div>

    <r:layoutResources/>
</div>
</body>
</html>