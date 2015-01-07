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
    <asset:javascript src="jquery.js"/>
    <asset:javascript src="application.js"/>
    <asset:stylesheet href="bootstrap/bootstrap.css"/>
    <asset:javascript src="bootstrap/bootstrap.js"/>
    <asset:stylesheet href="ztree/zTreeStyle.css"/>
    <asset:javascript src="ztree/jquery.ztree.all-3.5.min.js"/>

    %{--<g:javascript library='jquery'/>--}%
    %{--<g:javascript library='bootstrap'/>--}%
    %{--<g:javascript library='application'/>--}%
    %{--<g:javascript library='ztree'/>--}%
    %{--<r:layoutResources/>--}%

    <SCRIPT LANGUAGE="JavaScript">
        ${raw(data)}
        var zTreeObj;
        // zTree configuration information, refer to API documentation (setting details)
        var setting = {
        };
        // zTree data attributes, refer to the API documentation (treeNode data details)
//        var zNodes = [
//            {name:"test1", open:true, children:[
//                {name:"test1_1"}, {name:"test1_2"}]},
//            {name:"test2", open:true, children:[
//                {name:"test2_1"}, {name:"test2_2"}]}
//        ];
        $(document).ready(function(){
            zTreeObj = $.fn.zTree.init($("#treeApp"), setting, zNodes);
        });
    </SCRIPT>
</head>

<body>
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

    <ul id="treeApp" class="ztree"></ul>
</div>
<r:layoutResources/>
</body>
</html>