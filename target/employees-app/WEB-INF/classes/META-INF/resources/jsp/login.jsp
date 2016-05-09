<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 03.05.2016
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <link rel="stylesheet" href="../css/bootstrap.css">
    <link rel="stylesheet" href="../css/signin.css">
    <script src="../js/bootstrap.min.js"></script>
</head>

<body>
<div class="container">
    <c:if test="${not empty message}">
        <div class="alert alert-danger">
                ${message}
        </div>
    </c:if>

    <form class="form-signin" method="post">
        <h2 class="form-signin-heading">Hucksterbot</h2>
        <label for="inputEmail" class="sr-only">Имя пользователя</label>
        <input type="text" name="username" id="inputEmail" class="form-control" placeholder="Имя пользователя" required autofocus>
        <label for="inputPassword" class="sr-only">Пароль</label>
        <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Пароль" required>
        <div class="checkbox">
            <label>
                <input type="checkbox" value="remember-me"> Запомнить меня
            </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Войти</button>
    </form>

</div>
</body>
</html>