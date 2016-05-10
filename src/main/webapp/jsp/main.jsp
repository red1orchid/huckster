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
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/dashboard.css" rel="stylesheet">
    <link href="../css/xcharts.min.css" rel="stylesheet">
    <link href="../css/style.css" rel="stylesheet">
    <link href="../css/daterangepicker.css" rel="stylesheet">

    <script src="../js/bootstrap.min.js"></script>
    <script src="../js/jquery.min.js"></script>

    <script src="//cdnjs.cloudflare.com/ajax/libs/d3/2.10.0/d3.v2.js"></script>
    <script src="../js/xcharts.min.js"></script>

    <!-- The daterange picker bootstrap plugin -->
    <script src="../js/sugar.min.js"></script>
    <script src="../js/daterangepicker.js"></script>

    <!-- Our main script file -->
    <script src="../js/script.js"></script>
</head>
<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">Садовые машины</a>
        </div>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span> Выход</a></li>
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
            <h3 class="page-header">Показать данные за
                <div class="btn-group" data-toggle="buttons">
                    <label class="btn btn-primary">
                        <input type="radio" name="options" id="option1"> Текущий день
                    </label>
                    <label class="btn btn-primary">
                        <input type="radio" name="options" id="option2"> Текущую неделю
                    </label>
                    <label class="btn btn-primary active">
                        <input type="radio" name="options" id="option3"> Текущий месяц
                    </label>
                </div>
            </h3>

            <div class="row placeholders">
                <c:forEach var="panel" items="${panels}">
                    <div class="col-xs-6 col-sm-3 placeholder">
                        <div class="panel ${panel.getPanelType()}">
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

            <h2>Section title</h2>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Header</th>
                        <th>Header</th>
                        <th>Header</th>
                        <th>Header</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>1,001</td>
                        <td>Lorem</td>
                        <td>ipsum</td>
                        <td>dolor</td>
                        <td>sit</td>
                    </tr>
                    <tr>
                        <td>1,002</td>
                        <td>amet</td>
                        <td>consectetur</td>
                        <td>adipiscing</td>
                        <td>elit</td>
                    </tr>
                    <tr>
                        <td>1,003</td>
                        <td>Integer</td>
                        <td>nec</td>
                        <td>odio</td>
                        <td>Praesent</td>
                    </tr>
                    <tr>
                        <td>1,003</td>
                        <td>libero</td>
                        <td>Sed</td>
                        <td>cursus</td>
                        <td>ante</td>
                    </tr>
                    <tr>
                        <td>1,004</td>
                        <td>dapibus</td>
                        <td>diam</td>
                        <td>Sed</td>
                        <td>nisi</td>
                    </tr>
                    <tr>
                        <td>1,005</td>
                        <td>Nulla</td>
                        <td>quis</td>
                        <td>sem</td>
                        <td>at</td>
                    </tr>
                    <tr>
                        <td>1,006</td>
                        <td>nibh</td>
                        <td>elementum</td>
                        <td>imperdiet</td>
                        <td>Duis</td>
                    </tr>
                    <tr>
                        <td>1,007</td>
                        <td>sagittis</td>
                        <td>ipsum</td>
                        <td>Praesent</td>
                        <td>mauris</td>
                    </tr>
                    <tr>
                        <td>1,008</td>
                        <td>Fusce</td>
                        <td>nec</td>
                        <td>tellus</td>
                        <td>sed</td>
                    </tr>
                    <tr>
                        <td>1,009</td>
                        <td>augue</td>
                        <td>semper</td>
                        <td>porta</td>
                        <td>Mauris</td>
                    </tr>
                    <tr>
                        <td>1,010</td>
                        <td>massa</td>
                        <td>Vestibulum</td>
                        <td>lacinia</td>
                        <td>arcu</td>
                    </tr>
                    <tr>
                        <td>1,011</td>
                        <td>eget</td>
                        <td>nulla</td>
                        <td>Class</td>
                        <td>aptent</td>
                    </tr>
                    <tr>
                        <td>1,012</td>
                        <td>taciti</td>
                        <td>sociosqu</td>
                        <td>ad</td>
                        <td>litora</td>
                    </tr>
                    <tr>
                        <td>1,013</td>
                        <td>torquent</td>
                        <td>per</td>
                        <td>conubia</td>
                        <td>nostra</td>
                    </tr>
                    <tr>
                        <td>1,014</td>
                        <td>per</td>
                        <td>inceptos</td>
                        <td>himenaeos</td>
                        <td>Curabitur</td>
                    </tr>
                    <tr>
                        <td>1,015</td>
                        <td>sodales</td>
                        <td>ligula</td>
                        <td>in</td>
                        <td>libero</td>
                    </tr>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>

<script src="../js/bootstrap.min.js"></script>
<script src="../js/jquery.min.js"></script>

<script src="//cdnjs.cloudflare.com/ajax/libs/d3/2.10.0/d3.v2.js"></script>
<script src="../js/xcharts.min.js"></script>

<!-- The daterange picker bootstrap plugin -->
<script src="../js/sugar.min.js"></script>
<script src="../js/daterangepicker.js"></script>

<!-- Our main script file -->
<script src="../js/script.js"></script>

</body>
</html>