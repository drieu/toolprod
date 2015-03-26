<%@ page import="toolprod.IndexController" %>
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
    <asset:javascript src="prototype.js"/>
    <asset:stylesheet href="bootstrap/bootstrap.css"/>
    <asset:stylesheet href="form.css"/>
    <asset:javascript src="bootstrap/bootstrap.js"/>
</head>
<body>

    <g:applyLayout name="menu" />

            <div class="row">
                <div class="col-md-2"></div>
                <div class="col-md-5 toolprod_form">

                    <form role="form-horizontal" data-toggle="validator">
                            <div class="form-group">
                                <h2>Création d'une fiche LDAP</h2>
                            </div>
                            <div class="row"></div>
                            <div class="form-group">
                                <div class="row">
                                    <label for="rne" class="control-label col-md-3">RNE</label>
                                    <div class="col-md-5">
                                        <input type="text" class="form-control" id="rne" name="rne" placeholder="....087XXX" value="">
                                        <div class="help-block with-errors"></div>avec le 0 devant !</span>
                                    </div>

                                </div>
                            </div>
                            <div class="form-group">
                                <div class="row">
                                        <label for="pwd" class="control-label col-md-3">Mot de passe</label>
                                        <div class="col-md-5">
                                            <input type="text" class="form-control" id="pwd" name="pwd" placeholder="****" value="">
                                        </div>
                                        <div class="col-md-5"></div>
                                 </div>
                            </div>
                        <div class="form-group">
                            <div class="row">
                                <label for="pwd" class="col-md-3">Type</label>
                                <div class="col-md-5">
                                    <script>
                                        var rneVal = document.getElementById('rne').text;

                                    </script>
                                    <g:select optionKey="id" optionValue="fullNameType" name="shortNameType" id="selecttype" from="${toolprod.MailType.list()}"
                                    onchange="${remoteFunction(
                                            controller:'tools',
                                            action:'ajaxCheckMailInLDAP',
                                            params:'\'id=\' + this.value + \'&rne=\' + document.getElementById(\'rne\').value',
                                            onSuccess:'checkLdapMail(data)')}"
                                    ></g:select>
                                    %{--<select name="type" class="form-control" id="selecttype">--}%
                                        %{--<option value="NONE" selected>Choix du type de boîte</option>--}%
                                        %{--<g:each in="${toolprod.MailType.findAll()}" var="type">--}%
                                            %{--<option value="${type.shortNameType}">${type?.fullNameType}</option>--}%
                                        %{--</g:each>--}%
                                    %{--</select>--}%
                                </div>
                                <div class="col-md-5"></div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-3"></div>
                                <div class="col-md-5">
                                    <button type="submit" class="btn btn-info">Génération</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-5"></div>
            </div>
    </br>
<div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-5">
        <div class="panel panel-success">
            <div class="panel-heading">
                <h3 class="panel-title">Résultat </h3>
            </div>
            <div class="panel-body">
                <br/>
                <table class="table table-hover">
                    <tr>
                        <td>Identifiant</td>
                        <td>${uid}</td>
                    </tr>
                    <tr>
                        <td>Mail</td>
                        <td>${mail}</td>
                    </tr>
                    <tr>
                        <td>Mot de passe</td>
                        <td>${pwd}</td>
                    </tr>
                    <tr>
                        <td>Fichier LDAP</td>
                        <td><g:link action="downloadUID" params="[uid: uid, rne: rne, type: type, mail: mail, pwd: pwd]">Fichier LDAP</g:link></td>
                    </tr>
                </table>
                <br/>
            </div>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h3 class="panel-title">Récapitulatif des normes </h3>
            </div>
            <div class="panel-body">
                <br/>
                <table class="table table-hover">
                    <tr>
                        <th>Type de boîte</th>
                        <th>Norme pour l'uid</th>
                        <th>Norme pour le mail</th>
                    </tr>
                    <g:each in="${toolprod.MailType.findAll()}" var="type">
                        <tr>
                            <td>${type.fullNameType}</td>
                            <td>${type.shortNameType}</td>
                            <td>${type.shortNameType}.RNE@ac-limoges.fr</td>
                        </tr>
                    </g:each>
                </table>
                <br/>
            </div>
        </div>
    </div>
    </div>
</body>
</html>
<g:javascript>

    function checkLdapMail(data) {
        alert(data.text);
//        var response = '{"result":true,"count":1}';
//        var val = JSON.parse(response);
//        console.log("VAL:" + val.result);
//        var results = data;
//        for (var i=0; i < results.length; i++) {
//            var message = results[i];
//            var status = results[i];
//            alert("message:" + message);
//            alert("status:" + status);
//        }
        alert("end");

    }
</g:javascript>
