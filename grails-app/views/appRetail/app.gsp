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


    <SCRIPT LANGUAGE="JavaScript">
        ${raw(data)}
        var zTreeObj;
        // zTree configuration information, refer to API documentation (setting details)
        var setting = {
        };
        $(document).ready(function(){
            zTreeObj = $.fn.zTree.init($("#treeApp"), setting, zNodes);
        });
    </SCRIPT>
</head>

<body>
<div class="container">
    <g:applyLayout name="menu"/>
    <g:if test="${app != null}">
        <div class="row">
        <h1>Application : ${app?.name}</h1>
        <br/>
        <pre>Description : <small>${app?.description}</small></pre>
        <br/>
            <ol class="breadcrumb">
                <li><span class="label label-success">ARENA</span></li>
                <g:each in="${app?.arenaPath}" var="path">
                    <li><em><var>${path}</var></em></li>
                </g:each>
             </ol>
        </div>
        <div class="row">
            <table class="table table-hover">
                <thead>
                    <th>Listes des URL(s)</th>
                </thead>
                <tbody>
                    <g:each in="${app?.urls}">
                        <tr>
                            <td>
                            <a href="${it}">${it}</a>&nbsp;<br/>
                            </td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </div>
        <div class="row">
            <br/>
        </div>
        <div class="row">
            <table class="table table-hover">
                <thead>
                <th>Listes des serveurs APACHE</th>
                </thead>
                <tbody>
                    <g:each in="${app?.servers}" var="server">
                        <g:if test="${server?.serverType?.toString() == "APACHE"}">
                            <tr>
                                <td>
                                    <a href="<g:createLink controller="WebServer" action="getWebServer" params="[name:server?.name, type:server?.serverType?.toString(), port:server?.portNumber]" />"><span class="glyphicon glyphicon-zoom-in"></span></a>&nbsp;&nbsp;${server?.name}:${server?.portNumber}
                                </td>
                            </tr>
                        </g:if>
                    </g:each>
                </tbody>
            </table>
        </div>
        <div class="row">
                <table class="table table-hover">
                    <thead>
                        <th>Listes des serveurs WEBLOGIC</th>
                    </thead>
                    <tbody>
                    <g:each in="${app?.servers}" var="server">
                        <g:if test="${server?.serverType?.toString() == "WEBLOGIC"}">
                            <tr>
                                <td>
                                    <a href="<g:createLink controller="WebServer" action="getWebServer" params="[name:server?.name, type:server?.serverType?.toString(), port:server?.portNumber]" />"><span class="glyphicon glyphicon-zoom-in"></span></a>&nbsp;&nbsp;${server?.name}:${server?.portNumber}
                                </td>
                            </tr>
                        </g:if>
                    </g:each>
                    </tbody>
                </table>
        </div>
        <div class="row">
            <br/>
        </div>
        <div class="row">
            <pre>
                <ul id="treeApp" class="ztree"></ul>
            </pre>
        </div>
    </g:if>
    <g:else>
        <div class="well well-lg">Pas d'application !</div>
    </g:else>

</div>
</body>
</html>