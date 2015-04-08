<%@ page import="toolprod.Portal; toolprod.IndexController" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Welcome to Production !</title>

    <asset:stylesheet href="jquery.dataTables.css"/>

    <asset:javascript src="jquery.js"/>

    <asset:javascript src="jquery.dataTables.min.js"/>


    <asset:javascript src="application.js"/>
    <asset:stylesheet href="bootstrap/bootstrap.css"/>
    <asset:javascript src="bootstrap/bootstrap.js"/>
</head>

<body>
    <div class="container">
        <g:applyLayout name="menu" />

        <div class="row">
            <div class="col-md-9">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        Liste des applications &nbsp;&nbsp;<span class="badge">${count}</span>
                    </h4>
                </div>
                <div class="panel-body">
                    <table id="applis" class="display">
                        <thead>
                            <tr>
                                <th>Nom</th>
                                <th>Url</th>
                                <th>Vip</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>...</td>
                                <td>...</td>
                                <td>...</td>
                            </tr>
                        </tbody>
                    </table>
                    %{--<export:formats />--}%
                </div>
            </div>
        </div>
    </div>
</body>
</html>
<script>
    ${raw(data)};
    $(document).ready(function() {
        $('#applis').dataTable({
            "data": dataSet,
            "lengthMenu": [[10, 30, 40, -1], [10, 30, 40, "All"]],
            "columnDefs": [
                { "width": "15%", "targets": 0 },
                { "width": "10%", "targets": 1 },
                { "width": "75%", "targets": 2 }
            ],
            "language": {
                "lengthMenu": "Affiche _MENU_ résultats par page",
                "zeroRecords": "Aucun résultat - désolé",
                "info": "Affichage page _PAGE_ / _PAGES_",
                "infoEmpty": "Aucun enregistrement",
                "infoFiltered": "( nombre de résultats _MAX_ )"
            },
            "columns": [
                { "title": "Nom" },
                { "title": "Vip" },
                { "title": "Url" }
            ]
        });
    });
</script>
