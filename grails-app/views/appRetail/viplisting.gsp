<%@ page import="toolprod.Vip; toolprod.TreeNode" %>

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
    <g:applyLayout name="menu"/>
    <br/>
    <g:form name="print" action="renderFormPDF" method="POST">
        <div class="row">
            <table>
                <tr height="35px">
                    <td width="25%">
                        <g:form name="print" action="renderFormPDF" method="POST">
                            <select name="serverSelect">
                                <option value="">Choisir une machine web</option>
                                <g:each in="${servers}" var="server">
                                    <option value="${server}">${server}</option>
                                </g:each>
                            </select>
                        </g:form>
                    </td>
                    <td width="25%">
                        <button type="submit" class="btn btn-info">Imprimer</button>
                    </td>
                </tr>
                <tr height="35px">
                    <td></td>
                    <td></td>
                </tr>
                %{--<tr height="35px">--}%
                    %{--<g:form name="print" action="renderFormPDF" method="POST">--}%
                            %{--<td width="25%">--}%
                                %{--<g:select name="machineSelect" from="${toolprod.Machine.findAll().sort()}" optionValue="name" optionKey="name" noSelection="['Topic': 'Choisir une machine']" />--}%
                            %{--</td>--}%
                            %{--<td width="25%">--}%
                                %{--<button type="submit" class="btn btn-success">Imprimer</button>--}%
                            %{--</td>--}%
                    %{--</g:form>--}%
                %{--</tr>--}%
            </table>
        </div>
     </g:form>
    <br/>

</div>
</body>
</html>