<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 03.05.2016
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="error.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Личный кабинет HucksterBot</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="./css/bootstrap.min.css">
    <link rel="stylesheet" href="./css/bootstrap.css">
    <link rel="stylesheet" href="./css/signin.css">

    <script src="./js/jquery-2.2.4.min.js"></script>
    <script src="./js/bootstrap.min.js"></script>
</head>

<body>
<div class="container">
    <c:choose>
        <c:when test="${status.success}">
            <div class="alert alert-success">
                Инструкция по смене пароля отправлена на почту
            </div>
        </c:when>
        <c:when test="${not empty status.error}">
            <div class="alert alert-danger">
                    ${status.error}
            </div>
        </c:when>
    </c:choose>

    <form class="form-signin" method="post">
        <h2 class="form-signin-heading text-center">Hucksterbot</h2>
        <div class="form-group">
            <label for="inputEmail" class="sr-only">Имя пользователя</label></div>
        <div class="form-group">
            <input type="text" name="username" id="inputEmail" class="form-control" placeholder="Имя пользователя"
                   required autofocus>
            <label for="inputPassword" class="sr-only">Пароль</label></div>
        <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Пароль" required>
        <div class="form-group">
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="rememberMe" value="yes"> Запомнить меня
                </label>
            </div>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Войти</button>
        <br>
        <div class="form-group text-right"><a href="#" data-toggle="modal" data-target="#restorePassword">Забыли
            пароль?</a></div>
    </form>

    <!-- Modal -->
    <form method="post" class="modal fade" id="restorePassword" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header" align="center">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Восстановление пароля</h4>
                </div>
                <div class="modal-body">
                    <div role="form">
                        <div class="form-group">
                            <label for="email">Введите свой email:</label>
                            <input id="email" type="text" class="form-control">
                        </div>
                        <div class="form-group">
                            <button type="submit" name="restorePassword" value="true" id="saveOrder" class="btn btn-primary pull-right">OK</button>
                            <br>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>

</div>
</body>
</html>