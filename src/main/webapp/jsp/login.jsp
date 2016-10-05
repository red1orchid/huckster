<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 03.05.2016
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="error.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <title>Личный кабинет HucksterBot</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="./css/bootstrap.min.css">
    <link rel="stylesheet" href="./css/bootstrap.css">
    <link rel="stylesheet" href="./css/signin.css">
    <script src="./js/bootstrap.min.js"></script>
</head>

<body>
<div class="container">
    <c:if test="${not empty message}">
        <div class="alert alert-danger">
                ${message}
        </div>
    </c:if>

    <form class="form-signin" method="post">
        <h2 class="form-signin-heading text-center">Hucksterbot</h2>
        <div class="form-group">
            <label for="inputEmail" class="sr-only">Имя пользователя</label></div>
        <div class="form-group">
            <input type="text" name="username" id="inputEmail" class="form-control" placeholder="Имя пользователя"
                   required autofocus>
            <label for="inputPassword" class="sr-only">Пароль</label></div>
        <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Пароль" required>
        <div class="form-group"><div class="checkbox">
            <label>
                <input type="checkbox" name="rememberMe" value="yes"> Запомнить меня
            </label>
        </div></div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Войти</button>
    </form>

</div>
</body>
</html>