<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 23.05.2016
  Time: 18:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<>
<head>
    <title>Статистика и аналитика</title>
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/dashboard.css" rel="stylesheet">
    <link href="../css/datepicker.css" rel="stylesheet">
    <link href="../css/table.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.datatables.net/u/dt/dt-1.10.12,cr-1.3.2/datatables.min.css">

    <script src="../js/jquery-2.2.4.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="https://cdn.datatables.net/u/dt/dt-1.10.12,cr-1.3.2/datatables.min.js"></script>
    <script src="../js/bootstrap-datepicker.js"></script>
</head>
<body>
<%--Top bar--%>
<%@ include file="navbar.jsp" %>

<div class="container-fluid">
    <div class="row">
        <%--Sidebar--%>
        <%@ include file="sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ul class="nav nav-tabs">
                <li class="active"><a data-toggle="pill" href="#goods">Товары и рекомендации</a></li>
                <li><a data-toggle="pill" href="#traffic">Трафик и конверсия</a></li>
                <li><a data-toggle="pill" href="#yml">Информация по YML</a></li>
            </ul>

            <div class="tab-content">
                <div id="goods" class="tab-pane fade in active">
                    <%--Period selector--%>
                    <form method="post">
                        <br>
                        <h4>Показать данные за
                            <%@ include file="period.jsp" %>
                        </h4>
                        <c:choose>
                            <c:when test="${period == 'day'}">
                                <c:set var="periodHeader" value="день"/>
                            </c:when>
                            <c:when test="${period == 'week'}">
                                <c:set var="periodHeader" value="неделя"/>
                            </c:when>
                            <c:when test="${period == 'month'}">
                                <c:set var="periodHeader" value="месяц"/>
                            </c:when>
                        </c:choose>

                        <table id="goodsTbl" class="table table-hover table-bordered" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>артикул</th>
                                <th>название</th>
                                <th>категория</th>
                                <th>вендор</th>
                                <th>просмотры, ${periodHeader}</th>
                                <th>показы виджета, ${periodHeader}</th>
                                <th>заказ с корзины, ${periodHeader}</th>
                                <th>заказ с 1-го виджета, ${periodHeader}</th>
                                <th>заказ с 2-го виджета, ${periodHeader}</th>
                                <th>заказ с 3-го виджета, ${periodHeader}</th>
                                <th>рекомендации</th>
                            </tr>
                            </thead>
                            <tfoot>
                            </tfoot>
                            <tbody>
                            </tbody>
                        </table>
                    </form>
                </div>
                <div id="traffic" class="tab-pane fade">
                    <h3>Menu 1</h3>
                    <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
                        consequat.</p>
                </div>
                <div id="yml" class="tab-pane fade">
                    <h3>Menu 2</h3>
                    <p>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium,
                        totam rem aperiam.</p>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#goodsTbl').DataTable({
            colReorder: true,
            "ordering": false,
            "iDisplayLength": 25,
            "language": {
                "lengthMenu": "Показать _MENU_ записей",
                "zeroRecords": "По Вашему запросу ничего не найдено",
                "search": "Поиск:",
                "info": "Показаны _START_-_END_ из _TOTAL_ записей",
                "infoEmpty": "Нет записей",
                "infoFiltered": "(всего _MAX_)",
                "loadingRecords": "Загрузка...",
                "paginate": {
                    "first": "1",
                    "last": "_PAGES_",
                    "next": ">>",
                    "previous": "<<"
                }
            },
            "ajax": {
                "url": "/datatable",
                "data" : {
                    "type" : "goods"
                }
            }
        });
    });
</script>
</body>
</html>
