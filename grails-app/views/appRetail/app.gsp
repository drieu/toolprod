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
    <g:javascript library='application'/>
    <g:javascript library='jqtree'/>
    <r:layoutResources/>
    <script>
    ${raw(data)}
    $(document).ready(function () {
        $('#tree_node').tree({
            data:data,
            autoOpen: true
        });
    });
    </script>
</head>

<body onload="CreateTree();">
<div class="container">
    <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
    <g:applyLayout name="menu"/>
    <g:if test="${app != null}">
        <h1>Application : ${app?.name}</h1>
        <br/>
        <pre>Description : <small>${app?.description}</small></pre>
        <br/>
        <dl class="dl-horizontal">
            <dt>URL</dt>
            <dd>
                <g:each in="${app?.urls}">
                      <a href="${it}">${it}</a>&nbsp;<br/>
                </g:each>
            </dd>
        </dl>
        <dl class="dl-horizontal">
            <dt>Serveur APACHE</dt>
            <dd>
                <g:each in="${app?.servers}" var="server">
                    <g:if test="${server?.serverType?.toString() == "APACHE"}">
                        <a href="<g:createLink controller="WebServer" action="getWebServer" params="[name:server?.name, type:server?.serverType?.toString(), port:server?.portNumber]" />"><span class="glyphicon glyphicon-zoom-in"></span></a>&nbsp;&nbsp;${server?.name}:${server?.portNumber}</br>
                    </g:if>
                </g:each>
            </dd>
        </dl>
        <br/>
        <dl class="dl-horizontal">
            <dt>Serveur WEBLOGIC</dt>
            <dd>
                <g:each in="${app?.servers}" var="server">
                    <g:if test="${server?.serverType?.toString() == "WEBLOGIC"}">
                        <a href="<g:createLink controller="WebServer" action="getWebServer" params="[name:server?.name, type:server?.serverType?.toString(), port:server?.portNumber]" />"><span class="glyphicon glyphicon-zoom-in"></span></a>&nbsp;&nbsp;${server?.name}:${server?.portNumber}</br>
                    </g:if>
                </g:each>
            </dd>
        </dl>

        <br/>

        %{--<g:render template="/appRetail/machineTree" model="[nodes:app?.node]"/>--}%
        %{--<div id="jstree_demo">--}%
        %{--</div>--}%
    </g:if>
    <g:else>
        <div class="well well-lg">Pas d'application !</div>
    </g:else>

    %{--DATA :--}%
    <div id="tree_node"></div>
    <br/>
    %{--<br/>--}%
    %{--DATA 2 :--}%

    %{--END--}%

</div>
<r:layoutResources/>
</body>
</html>