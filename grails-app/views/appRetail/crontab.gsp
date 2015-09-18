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
    <asset:javascript src="moment-with-locales.min.js"/>
    <asset:stylesheet href="bootstrap/bootstrap.css"/>
    <asset:javascript src="bootstrap/bootstrap.js"/>
    <asset:stylesheet href="fullCalendar.min.css"/>
    <asset:javascript src="fullcalendar.min.js"/>
    <script>
        $(document).ready(function() {

            // page is now ready, initialize the calendar...

            $('#calendar').fullCalendar({
                events: [
                    {
                        title  : 'event1',
                        start  : '2015-09-01T00:32:00',
                        allDay : false // will make the time show
                    },
                    {
                        title  : 'event2',
                        start  : '2015-09-01T01:32:00',
                        allDay : false // will make the time show
                    },
                    {
                        title  : 'event3',
                        start  : '2015-09-01T02:32:00',
                        allDay : false // will make the time show
                    },
                    {
                        title  : 'event4',
                        start  : '2015-09-01T03:32:00',
                        allDay : false // will make the time show
                    },
                    {
                        title  : 'event5',
                        start  : '2015-09-01T04:32:00',
                        allDay : false // will make the time show
                    },
                    {
                        title  : 'event6',
                        start  : '2015-09-01T05:32:00',
                        allDay : false // will make the time show
                    },
                    {
                        title  : 'event7',
                        start  : '2015-09-01T06:32:00',
                        allDay : false // will make the time show
                    }

                ],
                timeFormat: 'HH(:mm)' // uppercase H for 24-hour clock
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

</body>
</html>
