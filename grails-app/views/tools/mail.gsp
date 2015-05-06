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

    <div class="container">

        <g:applyLayout name="menu" />
    </div>
    %{--Create mail status message--}%
        <div id="create_status_ok" class="alert alert-success" role="alert" style="display: none">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            <span class="sr-only">Info:</span>
            Vérification dans le LDAP OK ! vous pouvez créer cette adresse.
        </div>
        <div id="create_status_ko" class="alert alert-danger" role="alert" style="display: none">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            <span class="sr-only">Error:</span>
            Mail déjà présent dans le LDAP !
        </div>
        <div id="create_status_info" class="alert alert-info" role="alert" style="display: none">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            <span class="sr-only">Info:</span>
            Recherche dans le ldap ...
        </div>

    %{--Form to create ldap file--}%
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
                                        <input type="password" class="form-control" id="pwd" name="pwd" value="">
                                    </div>
                                    <div class="col-md-5"></div>
                             </div>
                        </div>
                    <div class="form-group">
                        <div class="row">
                            <label for="pwd" class="col-md-3">Type</label>
                            <div class="col-md-3">
                                <g:select noSelection="['0':'Choisir un type de boîte']" optionKey="shortNameType" optionValue="fullNameType" name="shortNameType"  id="selecttype" from="${toolprod.MailType.list()}"
                                onchange="
                                cleanMsg();
                                ${remoteFunction(
                                        controller:'tools',
                                        action:'ajaxCheckMailInLDAP',
                                        params:'\'id=\' + this.value + \'&rne=\' + document.getElementById(\'rne\').value',
                                        onSuccess:'checkLdapMail(data)')}"
                                ></g:select>
                            </div>
                            <div class="col-md-3">
                                <span class="glyphicon glyphicon-star" aria-hidden="true">
                                    <g:link controller="admin" action="initData"><small>Initialisation des données</small></g:link>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-md-3"></div>
                            <div class="col-md-5">
                                <button id="btnSubmit" type="submit" class="btn btn-info" disabled>Génération</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="col-md-5"></div>
        </div>
        </br>
    %{--Result panel--}%
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
    %{--Info panel--}%
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
    /**
     * Show result stored in data on html page
     * @param data JSON data send by ajaxCheckMailInLDAP in controller.
     */
    function checkLdapMail(data) {
        console.log("checkLdapMail()");

        document.getElementById('create_status_ko').style.display='none';
        document.getElementById('create_status_ok').style.display='none';
        document.getElementById('create_status_info').style.display='block';

        if (data.text == "KO") {
            document.getElementById('create_status_info').style.display='none';
            document.getElementById('create_status_ko').style.display='block';
            document.getElementById('create_status_ko').textContent= data.mail.toString() + " existe deja dans le LDAP !";
            document.getElementById('selecttype').value=0;
        }
        if (data.text == "OK") {
            document.getElementById('create_status_info').style.display='none';
            document.getElementById('create_status_ok').style.display='block';
            document.getElementById('btnSubmit').disabled = false;
        }

        if (data.text == "EMPTY") {
            document.getElementById('create_status_info').style.display='none';
            document.getElementById('create_status_ko').style.display='block';
            document.getElementById('create_status_ko').textContent= "le rne est vide !";
            document.getElementById('selecttype').value=0;

        }
        console.log("checkLdapMail() : end");
    }

    /**
    * Clean message status on web page.
    */
    function cleanMsg() {
        document.getElementById('create_status_ko').style.display='none';
        document.getElementById('create_status_ok').style.display='none';
        document.getElementById('create_status_info').style.display='none';
    }
</g:javascript>
