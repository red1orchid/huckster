<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 09.05.2016
  Time: 16:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="error.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Личный кабинет</title>
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/dashboard.css" rel="stylesheet">

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
</head>
<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">${company}</a>
        </div>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="logout"><span class="glyphicon glyphicon-log-out"></span> Выход</a></li>
        </ul>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <ul class="nav nav-sidebar">
                <c:forEach var="menuItem" items="${menu}">
                    <li><a href="${menuItem.getLink()}"><span class="${menuItem.getIcon()}"
                                                              aria-hidden="true"></span> ${menuItem.getName()}</a></li>
                </c:forEach>
            </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <form method="post">
                <h3 class="page-header">Показать данные за
                    <div class="btn-group">
                        <button type="submit" name="period" value="day"
                                class="btn btn-primary <c:if test="${period == 'day'}">active</c:if>">Текущий день
                        </button>
                        <button type="submit" name="period" value="week"
                                class="btn btn-primary <c:if test="${period == 'week'}">active</c:if>">Текущую неделю
                        </button>
                        <button type="submit" name="period" value="month"
                                class="btn btn-primary <c:if test="${period == 'month'}">active</c:if>">Текущий месяц
                        </button>
                    </div>
                    <%--                    <div class="btn-group" data-toggle="buttons">
                                            <label class="btn btn-primary">
                                                <input type="radio" name="options" value="option1"> Текущий день
                                            </label>
                                            <label class="btn btn-primary">
                                                <input type="radio" name="options" value="option2"> Текущую неделю
                                            </label>
                                            <label class="btn btn-primary active">
                                                <input type="radio" name="options" value="option3"> Текущий месяц
                                            </label>
                                            <input type="submit" value="submit">
                                        </div>--%>
                </h3>
            </form>

            <div class="row placeholders">
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
                            <a href="#">
                                <div class="panel-footer">
                                    <span class="pull-left">${panel.getFooter()}</span>
                                    <span class="pull-right"><span class="${panel.getIcon()}" aria-hidden="true"></span></span>
                                    <div class="clearfix"></div>
                                </div>
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>


<%--

<script src="../js/bootstrap.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
--%>

</body>
</html>