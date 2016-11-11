<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 12.05.2016
  Time: 0:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html>
<head>
    <title>Ошибка сервера</title>
    <link rel="stylesheet" href="./css/error.css">
    <link rel="stylesheet" href="./css/bootstrap.css">

    <script src="./js/jquery-2.2.4.min.js"></script>
    <script src="./js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="error-template">
                <h1>500</h1>
                <h2>Ошибка сервера</h2>
                <div class="error-details">
                    Сервер временно недоступен. Попробуйте обновить страницу или зайти позже.
                </div>
                <div class="error-actions">
                    <a href="." class="btn btn-primary btn-lg"><span
                            class="glyphicon glyphicon-home"></span>
                        Домашняя страница</a><a href="mailto:support@hucksterbot.ru" class="btn btn-default btn-lg"><span
                        class="glyphicon glyphicon-envelope"></span> Напишите нам </a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>