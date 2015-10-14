<html>
<head>
    <style>
        table, tbody, tfoot, thead, tr, th, td {
        background: none;
        margin: 0; padding: 0;
        border: 0; font-size: 16px;
        font: inherit; vertical-align: middle;
        text-align: center;
        }

        table {
        border-collapse: collapse;
        border-spacing: 0;
        color: #333;
        font-family: Helvetica, Arial, sans-serif;
        width: 640px;
        border-collapse:
        collapse; border-spacing: 0;
        }

        td, th {
        border: 1px solid transparent;
        height: 30px; transition: all 0.3s;
        }

        th { background: #DFDFDF; font-weight: bold; }

        td { background: #FAFAFA; text-align: center; }

        tr:nth-child(even) td { background: #F1F1F1; }
        tr:nth-child(odd) td { background: #FEFEFE; }
        tr td:hover { background: #666; color: #FFF; } /* Hover cell effect! */
    </style>
</head>
<body>
<p>Bonjour,</p>
<p>un nouveau import de fichiers de configuration vient d'être r&eacute;alis&eacute; dans Toolprod.
Les diff&eacute;rences avec les configurations pr&eacute;c&eacute;dentes sont consign&eacute;es dans le tableau ci-dessous.</p>
<p>Merci de prendre contact avec le p&ocirc;le IE si des valeurs vous semblent anormales.</p>
<br/>
<table class="table table-hover table-striped">
    <tbody>
    <tr>
        <th colspan="3">Tableau des v&eacute;rifications de configuration ( derni&egrave;re archive le ${archDate} )</th>
    </tr>
    <tr>
        <td>Liste des nouvelles machines </td>
        <td>
            <g:each in="${machineDiffs}">${it} </g:each></td>
        <td>
            <g:if test="${machineDiffs.size() == 0}">
                <span class="label label-success">OK</span>
            </g:if>
            <g:else>
                <span class="label label-warning">warning</span>
            </g:else>
        </td>
    </tr>
    <tr>
        <td>Liste des applications en PLUS</td>
        <td><g:each in="${appDiffs}">${it} </g:each></td>
        <td>
            <g:if test="${appDiffs.size() == 0}">
                <span class="label label-success">OK</span>
            </g:if>
            <g:else>
                <span class="label label-warning">warning</span>
            </g:else>
        </td>
    </tr>
    <tr>
        <td>Liste des applications en MOINS</td>
        <td><g:each in="${disappearApps}">${it} </g:each></td>
        <td>
            <g:if test="${disappearApps.size() == 0}">
                <span class="label label-success">OK</span>
            </g:if>
            <g:else>
                <span class="label label-warning">warning</span>
            </g:else>
        </td>
    </tr>
    <tr>
        <td>Nombre d'applications</td>
        <td><g:if test="${count == 0}"></g:if>
            <g:else>${count}</g:else>
        </td>
        <td>
            <g:if test="${count == 0}">
                <span class="label label-success">OK</span>
            </g:if>
            <g:else>
                <span class="label label-warning">warning</span>
            </g:else>
        </td>
    </tr>
    </tbody>
</table>
<br/>
<br/>
<table cellspacing="15">
    <tbody>
    <tr>
        <td><img src="http://cache.media.education.gouv.fr/image/Logos/63/3/logo_academie_limoge_web_337633.jpg" height="85"></td>
        <td valign="middle">
            <p><small>Pole IE<br>
                Equipe Production<br>
                DIAL
            </small></p>
        </td>
        <td valign="middle">
            <p><small>Rectorat de l'acad&eacute;mie de Limoges<br>
                <a class="moz-txt-link-freetext" href="http://www.ac-limoges.fr">http://www.ac-limoges.fr</a></small></p>
        </td>
    </tr>
    </tbody>
</table>
<br/>
<br/>
<br/>
<table cellspacing="15">
    <tbody>
    <tr>
        <td valign="middle">
            <p><small>
                Centre d'assistance et de service de l'acad&eacute;mie
                (CASDAL) :<br>
                Acc&egrave;s guichet unique en ligne
                <a class="moz-txt-link-freetext" href="https://services.ac-limoges.fr">https://services.ac-limoges.fr</a> <br>
                Plus d'information sur
                <a class="moz-txt-link-freetext" href="http://intra.ac-limoges.fr/rubrique.php3?id_rubrique=1945">http://intra.ac-limoges.fr/rubrique.php3?id_rubrique=1945</a><br>
            </small></p>
        </td>
    </tr>
    </tbody>
</table>

</body>
</html>