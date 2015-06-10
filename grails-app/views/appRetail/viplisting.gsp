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
                                <input type="hidden" name="select" value="server"/>
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
                    <tr height="35px">
                        <g:form name="print" action="renderFormPDF" method="POST">
                                <input type="hidden" name="select" value="vip"/>
                                <td width="25%">
                                    <select name="vipSelect">
                                        <option value="">Choisir une VIP</option>
                                        <g:each in="${Vip.findAll()}" var="vip">
                                            <option value="${vip.technicalName}">${vip.name}_${vip?.type?.toUpperCase()}</option>
                                        </g:each>
                                    </select>
                                    %{--<g:select name="vipSelect" from="${Vip.findAll()}" optionValue="name" optionKey="name" noSelection="['Topic': 'Choisir une vip']" />--}%
                                </td>
                                <td width="25%">
                                    <button type="submit" class="btn btn-success">Imprimer</button>
                                </td>
                        </g:form>
                    </tr>
                </table>
            </div>
         </g:form>
        <br/>
        <g:render template="/layouts/footer"></g:render>
    </div>
</body>
</html>