<%@ page import="toolprod.IndexController" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Welcome to Production !</title>
    <g:javascript library='jquery'/>
    <g:javascript library='bootstrap'/>
    <r:layoutResources/>
</head>

<body>
<div class="container">
    <g:applyLayout name="menu" />

    <div class="row">
        <div class="col-md-3">

        </div>
        <div class="col-md-9">
            <div class="panel-heading">
                <h4 class="panel-title">
                        Liste des applications &nbsp;&nbsp;<span class="badge">${apps?.size()}</span>
                </h4>
            </div>
            <div class="panel-body">
                <table class="table table-hover table-striped">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Nom</th>
                        <th>url</th>
                        <th>Status </th>
                    </tr>
                    </thead>
                    <g:each in="${apps}" var="app">
                        <tr>
                            <td><a href="<g:createLink controller="AppRetail" action="app" params="[name:app?.name]" />"><span class="glyphicon glyphicon-zoom-in"></span></a></td>
                            <td>${app?.name}</td>
                            <td>
                                <g:each in="${app?.urls}">
                                    <a href="${it}">${it}</a>&nbsp;<br/>
                                </g:each>
                            </td>
                            <td>
                                <%
//                                    boolean status = true
//                                    HttpURLConnection connection = null;
//                                    OutputStreamWriter wr = null;
//                                    BufferedReader rd  = null;
//                                    StringBuilder sb = null;
//                                    String line = null;
//
//                                    URL serverAddress = null;
//
//                                    try {
//                                        // TODO : compile error add a for urls
//                                        serverAddress = new URL(app?.url);
//                                        //set up out communications stuff
//                                        connection = null;
//
//                                        //Set up the initial connection
//                                        connection = (HttpURLConnection)serverAddress.openConnection();
//                                        connection.setRequestMethod("GET");
//                                        connection.setDoOutput(true);
//                                        connection.setReadTimeout(1000);
//
//                                        connection.connect();
//
//                                        //get the output stream writer and write the output to the server
//                                        //not needed in this example
//                                        //wr = new OutputStreamWriter(connection.getOutputStream());
//                                        //wr.write("");
//                                        //wr.flush();
//
//                                        //read the result from the server
//                                        rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                                        sb = new StringBuilder();
//
//                                        while ((line = rd.readLine()) != null)
//                                        {
//                                            sb.append(line + '\n');
//                                        }
//
//                                        System.out.println(sb.toString());
//
//                                    } catch (MalformedURLException e) {
//                                        status = false
//                                        e.printStackTrace();
//                                    } catch (ProtocolException e) {
//                                        status = false
//
//                                        e.printStackTrace();
//                                    } catch (IOException e) {
//                                        status = false
//
//                                        e.printStackTrace();
//                                    }
//                                    finally
//                                    {
//                                        //close the connection, set all objects to null
//                                        connection.disconnect();
//                                        rd = null;
//                                        sb = null;
//                                        wr = null;
//                                        connection = null;
//                                    }
                                %>
                                <g:if test="${status}">
                                <span class="label label-success">OK</span>
                                </g:if>
                                <g:else>
                                <span class="label label-danger">KO</span>
                                </g:else>


                            </td>
                        </tr>
                    </g:each>
                </table>
            </div>
        </div>

    </div>
</div>

<r:layoutResources/>
</body>
</html>
