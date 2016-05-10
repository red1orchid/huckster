<%@ page import="com.google.gson.Gson" %><%--
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
            src='../js/charts/script2.js'></script>
    <script type='text/javascript'
            src='../js/daterangepicker.js'></script>
</head>
<body>
<div style="margin: 10px 0 0 10px;">
<%
        Gson gson = new Gson();
        gson.toJson(request.getAttribute("data"));
    <div id="placeholder">
        <figure id="chart"></figure>
    </div>
</div>
</body>
</html>