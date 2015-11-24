<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Welcome to Production !</title>

    <asset:javascript src="jquery.js"/>

    <asset:stylesheet href="treant/Treant.css"/>
    <asset:stylesheet href="treant/custom-colored.css"/>

    <asset:javascript src="treant/Treant.min.js"/>
    <asset:javascript src="treant/raphael.js"/>
    %{--<asset:javascript src="treant/custom-colored.js"/>--}%

    <asset:javascript src="application.js"/>
    <asset:stylesheet href="bootstrap/bootstrap.css"/>
    <asset:javascript src="bootstrap/bootstrap.js"/>
    <script>
    var config = {
                container: "#custom-colored",

                nodeAlign: "BOTTOM",

                connectors: {
                    type: 'step'
                },
                node: {
                    HTMLclass: 'nodeExample1'
                }
            },
            vip = {
                text: {
                    name: "Entry",
                },
            },
            ${raw(dataVip)}

            chart_config = [
                config,
                ${raw(dataVipEnum)}
            ];

    </script>
</head>

    <body>
        <div class="container">
            <div class="chart" id="custom-colored"> --@-- </div>
            <script>
                new Treant( chart_config );
            </script>
        </div>
    </body>
</html>