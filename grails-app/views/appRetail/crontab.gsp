<%@ page import="toolprod.Portal; toolprod.IndexController" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Welcome to Production !</title>
    <g:javascript library='jquery'/>
    <r:require module="jquery-ui"/>
    <g:javascript library='bootstrap'/>
    <g:javascript library='fullcalendar'/>
    <g:javascript library='bootstrap'/>
    <r:layoutResources/>
    <script>
        $(document).ready(function() {

            // page is now ready, initialize the calendar...

            $('#calendar').fullCalendar({
                events: [
                    {
                        title  : 'event1',
                        start  : '2014-11-01'
                    },
                    {
                        title  : 'event2',
                        start  : '2010-01-05',
                        end    : '2010-01-07'
                    },
                    {
                        title  : 'event3',
                        start  : '2014-11-01T00:30:00',
                        allDay : false // will make the time show
                    },
                    {
                        title  : 'event3',
                        start  : '2014-11-01T00:32:00',
                        allDay : false // will make the time show
                    },
                    {
                        title  : 'event3',
                        start  : '2014-11-01T00:32:00',
                        allDay : false // will make the time show
                    },
                    {
                        title  : 'event3',
                        start  : '2014-11-01T00:32:00',
                        allDay : false // will make the time show
                    },
                    {
                        title  : 'event3',
                        start  : '2014-11-01T00:32:00',
                        allDay : false // will make the time show
                    }
                ]
            })

        });
    </script>
</head>

<body>
<div class="container">
    <g:applyLayout name="menu" />

    <div class="row">
        <div class="col-md-9">
            <div id='calendar'></div>
        </div>
    </div>
</div>

<r:layoutResources/>
</body>
</html>
