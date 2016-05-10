<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 10.05.2016
  Time: 18:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
    <title>XChart using JSP, Servlet</title>
    <link rel="stylesheet"
          href="../css/charts/chart.css">
    <link rel="stylesheet"
          href="../css/charts/xcharts.min.css">
    <link rel="stylesheet"
          href="../css/charts/bootstrap.min.css">
    <link rel="stylesheet"
          href="../css/daterangepicker.css">
    <script type='text/javascript'
            src='../js/jquery-1.9.1.min.js'></script>
    <script type='text/javascript'
            src='../js/jquery-migrate-1.2.1.js'></script>
    <script type='text/javascript'
            src='../js/jquery-ui-1.10.3-custom.min.js'></script>
    <script type='text/javascript'
            src='../js/charts/d3.min.js'></script>
    <script type='text/javascript'
            src='../js/charts/sugar.min.js'></script>
    <script type='text/javascript'
            src='../js/charts/xcharts.min.js'></script>
    <script type='text/javascript'
            src='../js/charts/script.js'></script>
    <script type='text/javascript'
            src='../js/daterangepicker.js'></script>
</head>
<body>
<div style="margin: 10px 0 0 10px;">
    <h3>XChart Example using AJAX, JSP and Servlet</h3>
    <form class="form-horizontal">
        <fieldset>
            <div class="input-prepend">
                <span class="add-on"><i class="icon-calendar"></i> </span> <input
                    type="text" name="range" id="range" />
            </div>
        </fieldset>
    </form>
    <div id="msg"></div>
    <div id="placeholder">
        <figure id="chart"></figure>
    </div>
</div>
</body>
</html>


<script type="text/javascript">

    $(function() {

        // Set the default dates
        var startDate = Date.create().addDays(-6), // 7 days ago
                endDate = Date.create(); // today

        var range = $('#range');

        // Show the dates in the range input
        range.val(startDate.format('{MM}/{dd}/{yyyy}') + ' - '
                + endDate.format('{MM}/{dd}/{yyyy}'));

        // Load chart
        ajaxLoadChart(startDate, endDate);

        range.daterangepicker({
            startDate : startDate,
            endDate : endDate,
            ranges : {
                'Today' : [ 'today', 'today' ],
                'Yesterday' : [ 'yesterday', 'yesterday' ],
                'Last 7 Days' : [ Date.create().addDays(-6), 'today' ],
                'Last 30 Days' : [ Date.create().addDays(-29), 'today' ]
            }
        }, function(start, end) {
            ajaxLoadChart(start, end);
        });

        // The tooltip shown over the chart
        var tt = $('<div class="ex-tooltip">').appendTo('body'), topOffset = -32;

        var data = {
            "xScale" : "time",
            "yScale" : "linear",
            "main" : [ {
                className : ".stats",
                "data" : []
            } ]
        };

        var opts = {
            paddingLeft : 50,
            paddingTop : 20,
            paddingRight : 10,
            axisPaddingLeft : 25,
            tickHintX : 9, // How many ticks to show horizontally

            dataFormatX : function(x) {
                // This turns converts the timestamps coming from
                // ajax.php into a proper JavaScript Date object
                return Date.create(x);
            },
            tickFormatX : function(x) {
                // Provide formatting for the x-axis tick labels.
                // This uses sugar's format method of the date object.
                return x.format('{MM}/{dd}');
            },
            "mouseover" : function(d, i) {
                var pos = $(this).offset();
                tt.text(d.x.format('{Month} {ord}') + ', No. of visits: ' + d.y)
                        .css({
                            top : topOffset + pos.top,
                            left : pos.left
                        }).show();
            },
            "mouseout" : function(x) {
                tt.hide();
            }
        };

        // Create a new xChart instance, passing the type
        // of chart a data set and the options object
        var chart = new xChart('line-dotted', data, '#chart', opts);

        // Function for loading data via AJAX and showing it on the chart
        function ajaxLoadChart(startDate, endDate) {
            // If no data is passed (the chart was cleared)
            if (!startDate || !endDate) {
                chart.setData({
                    "xScale" : "time",
                    "yScale" : "linear",
                    "main" : [ {
                        className : ".stats",
                        data : []
                    } ]
                });
                return;
            }
            // Otherwise, issue an AJAX request
            //URL of the servlet with POST request
            $.post('http://localhost:8080/test-webapp/xChartServlet', {
                start : startDate.format('{MM}/{dd}/{yyyy}'),
                end : endDate.format('{MM}/{dd}/{yyyy}')
            }, function(data) {
                if ((data.indexOf("No record found") > -1)
                        || (data.indexOf("Date must be selected.") > -1)) {
                    $('#msg').html('<span style="color:red;">' + data + '</span>');
                    $('#placeholder').hide();
                    chart.setData({
                        "xScale" : "time",
                        "yScale" : "linear",
                        "main" : [ {
                            className : ".stats",
                            data : []
                        } ]
                    });
                } else {
                    $('#msg').empty();
                    $('#placeholder').show();
                    var set = [];
                    $.each(data, function() {
                        //build array of chart data
                        set.push({
                            x : this.label,
                            y : parseInt(this.value, 10)
                        });
                    });
                    chart.setData({
                        "xScale" : "time",
                        "yScale" : "linear",
                        "main" : [ {
                            className : ".stats",
                            data : set
                        } ]
                    });
                }
            }, 'json');
        }
    });

</script>