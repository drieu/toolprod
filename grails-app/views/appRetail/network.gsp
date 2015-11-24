<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Welcome to Production !</title>

    <asset:javascript src="jquery.js"/>

    <asset:stylesheet href="vis.min.css"/>

    <asset:javascript src="vis.min.js"/>

    <asset:javascript src="application.js"/>
    <asset:stylesheet href="bootstrap/bootstrap.css"/>
    <asset:javascript src="bootstrap/bootstrap.js"/>


    <style type="text/css">
    #mynetwork {
        width: 600px;
        height: 400px;
        border: 1px solid lightgray;
    }
    </style>
</head>

<body>
<div class="container">
    <div id="mynetwork"></div>

    <script type="text/javascript">
        // create an array with nodes
        var nodes = new vis.DataSet([
            {id: 1, label: 'Node 1'},
            {id: 2, label: 'Node 2'},
            {id: 3, label: 'Node 3'},
            {id: 4, label: 'Node 4'},
            {id: 5, label: 'Node 5'}
        ]);

        // create an array with edges
        var edges = new vis.DataSet([
            {from: 1, to: 3},
            {from: 1, to: 2},
            {from: 2, to: 4},
            {from: 2, to: 5}
        ]);

        // create a network
        var container = document.getElementById('mynetwork');

        // provide the data in the vis format
        var data = {
            nodes: nodes,
            edges: edges
        };
        var options = {
            nodes:{
                shape: 'box'
            }

        };

        // initialize your network!
        var network = new vis.Network(container, data, options);
    </script>

</div>
</body>
</html>