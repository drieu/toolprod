<%@ page import="toolprod.TreeNode" %>

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
    <div class="dropdown">
        <button class="btn btn-default dropdown-toggle " type="button" id="dropdownMenu1" data-toggle="dropdown">
            Choix du portail
            <span class="caret"></span>
        </button>
        <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
            <g:each in="${portals}" var="portal">
                    <li role="presentation"><a role="menuitem" tabindex="-1" href="/toolprod/appRetail/viplisting/?choice=${portal}">${portal}</a></li>
            </g:each>
        </ul>
    </div>
    <table class="table table-hover table-striped">
        <caption>
            Liste des groupes de machines
        </caption>
        <thead>
        <tr>
            <th>Serveur</th>
        </tr>
        </thead>
        <g:each in="${vip?.servers}" var="server">
                <tr>
                    <td>${server?.name}:${server?.portNumber}</td>
                    <td></td>
                </tr>
        </g:each>
    </table>
    <ul id="treeApp" class="ztree"></ul>
</div>
</body>
</html>