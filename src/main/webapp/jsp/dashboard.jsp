<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 09.05.2016
  Time: 16:56
  To change this template use File | Settings | File Templates.

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Личный кабинет</title>
    <link href="./css/bootstrap.min.css" rel="stylesheet">
    <link href="./css/dashboard.css" rel="stylesheet">
    <link href="./css/charts/chart.css" rel="stylesheet">
    <link href="./css/charts/xcharts.min.css" rel="stylesheet">

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
    <script src="./js/bootstrap.min.js"></script>
    <script src='./js/jquery-1.9.1.min.js'></script>
    <script src='./js/jquery-migrate-1.2.1.js'></script>
    <script src='./js/jquery-ui-1.10.3-custom.min.js'></script>
    <script src='./js/charts/d3.min.js'></script>
    <script src='./js/charts/sugar.min.js'></script>
    <script src='./js/charts/xcharts.min.js'></script>
    <script src='./js/charts/script.js'></script>
</head>
<body>
<%--Top bar--%>
<%@ include file="navbar.jsp" %>

<div class="container-fluid">
    <div class="row">
        <%--Sidebar--%>
        <%@ include file="menu.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <%--Period selector--%>
            <form method="post">
                <h3 class="page-header">Показать данные за
                    <div class="btn-group">
                        <button type="submit" name="period" value="day"
                                class="btn btn-default <c:if test="${period == 'day'}">active</c:if>">Текущий день
                        </button>
                        <button type="submit" name="period" value="week"
                                class="btn btn-default <c:if test="${period == 'week'}">active</c:if>">Текущую неделю
                        </button>
                        <button type="submit" name="period" value="month"
                                class="btn btn-default <c:if test="${period == 'month'}">active</c:if>">Текущий месяц
                        </button>
                    </div>
                </h3>
            </form>

            <div class="row placeholders">
                <%--Statistic panels--%>
                <c:forEach var="panel" items="${panels}">
                    <div class="col-xs-6 col-sm-3 placeholder">
                        <div class="panel ${panel.getPanelClass()}">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <h1><span class="${panel.getIcon()}" aria-hidden="true"></span></h1>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <h1>${panel.getContent()}</h1>
                                        <div>${panel.getLabel()}</div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-footer">
                                <span class="pull-left">${panel.getFooter()}</span>
                                <span class="pull-right"><span class="${panel.getIcon()}"
                                                               aria-hidden="true"></span></span>
                                <div class="clearfix"></div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <%--Charts--%>
                <c:forEach var="chart" items="${charts}">
                    <div class="col-xs-12 col-sm-12 placeholder">
                        <div class="panel panel-default">
                            <div class="panel-heading text-left"><span class="${chart.getIcon()}"
                                                                       aria-hidden="true"></span> ${chart.getTitle()}
                            </div>
                            <div class="panel-body">
                                <figure id="${chart.getId()}"></figure>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <script type="text/javascript">
                <c:forEach var="chart" items="${charts}">
                var ${chart.getVar()} = ${chart.getData()};
                </c:forEach>
            </script>
        </div>
    </div>
</div>

</body>
</html>