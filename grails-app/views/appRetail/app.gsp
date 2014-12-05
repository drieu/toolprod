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
    <r:layoutResources/>
</head>

<body>
<div class="container">
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

        <g:render template="/appRetail/machineTree" model="[nodes:app?.node]"/>
        %{--<div id="jstree_demo">--}%
        %{--</div>--}%
    </g:if>
    <g:else>
        <div class="well well-lg">Pas d'application !</div>
    </g:else>
</div>
    %{--<script>--}%
        %{--$(function () {--}%
            %{--$('#jstree_demo').jstree({--}%
                %{--"core" : {--}%
                    %{--"animation" : 0,--}%
                    %{--"check_callback" : true,--}%
                    %{--"themes" : { "stripes" : true },--}%
                    %{--'data' : {--}%
                        %{--'url' : function (node) {--}%
                            %{--return node.id === '#' ?--}%
                                    %{--'ajax_demo_roots.json' : 'ajax_demo_children.json';--}%
                        %{--},--}%
                        %{--'data' : function (node) {--}%
                            %{--return { 'id' : node.id };--}%
                        %{--}--}%
                    %{--}--}%
                %{--},--}%
                %{--"types" : {--}%
                    %{--"#" : {--}%
                        %{--"max_children" : 1,--}%
                        %{--"max_depth" : 4,--}%
                        %{--"valid_children" : ["root"]--}%
                    %{--},--}%
                    %{--"root" : {--}%
                        %{--"icon" : "/static/3.0.8/assets/images/tree_icon.png",--}%
                        %{--"valid_children" : ["default"]--}%
                    %{--},--}%
                    %{--"default" : {--}%
                        %{--"valid_children" : ["default","file"]--}%
                    %{--},--}%
                    %{--"file" : {--}%
                        %{--"icon" : "glyphicon glyphicon-file",--}%
                        %{--"valid_children" : []--}%
                    %{--}--}%
                %{--},--}%
                %{--"plugins" : [--}%
                    %{--"contextmenu", "dnd", "search",--}%
                    %{--"state", "types", "wholerow"--}%
                %{--]--}%
            %{--});--}%
        %{--});--}%


    %{--</script>--}%
<r:layoutResources/>
</body>
</html>