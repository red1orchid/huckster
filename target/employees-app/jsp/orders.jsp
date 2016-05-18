<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 17.05.2016
  Time: 12:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Работа с заказами</title>
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/dashboard.css" rel="stylesheet">
    <link href="../css/jquery.dataTables.min.css" rel="stylesheet">
    <link href="../css/table.css" rel="stylesheet">
    <%--
    <link href="https://cdn.datatables.net/1.10.11/css/dataTables.bootstrap.min.css" rel="stylesheet">--%>

    <script src="../js/jquery-1.9.1.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.11/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.11/js/dataTables.bootstrap.min.js"></script>
</head>
<body>
<%--Top bar--%>
<%@ include file="navbar.jsp" %>

<div class="container-fluid">
    <div class="row">
        <%--Sidebar--%>
        <%@ include file="sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <h5 class="page-header">Если вам необходимо выгружать заказы по API - напишите нам на
                support@hucksterbot.ru</h5>

            <div class="row placeholders">
                <table id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>заказ</th>
                        <th>правило</th>
                        <th>артикул</th>
                        <th>код вендора</th>
                        <th>модель</th>
                        <th>цена базовая</th>
                        <th>цена итоговая</th>
                        <th>скидка</th>
                        <th>телефон</th>
                        <th>город</th>
                        <th>создан</th>
                        <th>фраза</th>
                        <th>статус</th>
                        <th>коммент</th>
                    </tr>
                    </thead>
                    <tfoot>
                    </tfoot>
                    <tbody>
                    <c:forEach var="row" items="${orders}">
                        <tr>
                                <%--                        <tr>
                                                            <td>${order.getId()}</td>
                                                            <td>${order.getRuleId()}</td>
                                                            <td>${order.getArticul()}</td>
                                                            <td>${order.getVendorCode()}</td>
                                                            <td>${order.getModel()}</td>
                                                            <td>${order.getPriceBase()}</td>
                                                            <td>${order.getPriceResult()}</td>
                                                            <td>${order.getDiscount()}</td>
                                                            <td>${order.getPhone()}</td>
                                                            <td>${order.getCity()}</td>
                                                            <td>${order.getDate()}</td>
                                                            <td>${order.getPhrase()}</td>
                                                            <td>${order.getStatus()}</td>
                                                            <td>${order.getComment()}</td>
                                                        </tr>--%>
                            <c:forEach var="cell" items="${row}">
                                <td>${cell}</td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#example').DataTable(/*{
         "processing": true,
         "serverSide": true,
         "ordering": false,
         "language": {
         "lengthMenu": "Показать _MENU_ записей",
         "zeroRecords": "По Вашему запросу ничего не найдено",
         "search": "Поиск:",
         "info": "Показаны _START_-_END_ из _TOTAL_ записей",
         "infoEmpty": "Нет записей",
         "infoFiltered": "(всего _MAX_)"
         },
         "ajax": "/datatable"
         }*/);
    });
</script>
</body>
</html>
