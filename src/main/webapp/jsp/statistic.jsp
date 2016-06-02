<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 23.05.2016
  Time: 18:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Статистика и аналитика</title>
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/dashboard.css" rel="stylesheet">
    <link href="../css/datepicker.css" rel="stylesheet">
    <link href="../css/table.css" rel="stylesheet">
    <link rel="stylesheet"
          href="https://cdn.datatables.net/u/bs/dt-1.10.12,cr-1.3.2,b-1.2.0,jszip-2.5.0,b-flash-1.2.0,b-html5-1.2.0/datatables.min.css">

    <script src="../js/jquery-2.2.4.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="https://cdn.datatables.net/u/bs/dt-1.10.12,cr-1.3.2,b-1.2.0,jszip-2.5.0,b-flash-1.2.0,b-html5-1.2.0/datatables.min.js"></script>
    <script src="../js/bootstrap-datepicker.js"></script>
</head>
<body>
<%--Top bar--%>
<%@ include file="navbar.jsp" %>

<div class="container-fluid">
    <div class="row">
        <%--Sidebar--%>
        <%@ include file="menu.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ul class="nav nav-tabs" id="tabs">
                <li><a data-toggle="tab" href="#goods">Товары и рекомендации</a></li>
                <li><a data-toggle="tab" href="#traffic">Трафик и конверсия</a></li>
                <li><a data-toggle="tab" href="#yml">Информация по YML</a></li>
            </ul>

            <div class="tab-content">
                <%--Goods and recommendations--%>
                <div id="goods" class="tab-pane fade">
                    <form method="post">
                        <br>
                        <div class="btn-group">
                            <button type="submit" name="periodGoods" value="day"
                                    class="btn btn-primary <c:if test="${periodGoods == 'day'}">active</c:if>">Текущий
                                день
                            </button>
                            <button type="submit" name="periodGoods" value="week"
                                    class="btn btn-primary <c:if test="${periodGoods == 'week'}">active</c:if>">Текущая
                                неделя
                            </button>
                            <button type="submit" name="periodGoods" value="month"
                                    class="btn btn-primary <c:if test="${periodGoods == 'month'}">active</c:if>">Текущий
                                месяц
                            </button>
                        </div>
                        <c:choose>
                            <c:when test="${periodGoods == 'day'}">
                                <c:set var="goodHeader" value="день"/>
                            </c:when>
                            <c:when test="${periodGoods == 'week'}">
                                <c:set var="goodHeader" value="неделя"/>
                            </c:when>
                            <c:when test="${periodGoods == 'month'}">
                                <c:set var="goodHeader" value="месяц"/>
                            </c:when>
                        </c:choose>
                    </form>

                    <table id="goodsTbl" class="table table-hover table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>артикул</th>
                            <th>название</th>
                            <th>категория</th>
                            <th>вендор</th>
                            <th>просмотры, ${goodHeader}</th>
                            <th>показы виджета, ${goodHeader}</th>
                            <th>заказ с корзины, ${goodHeader}</th>
                            <th>заказ с 1-го виджета, ${goodHeader}</th>
                            <th>заказ с 2-го виджета, ${goodHeader}</th>
                            <th>заказ с 3-го виджета, ${goodHeader}</th>
                            <th>рекомендации</th>
                        </tr>
                        </thead>
                        <tfoot>
                        </tfoot>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                <%--Traffic and conversion --%>
                <div id="traffic" class="tab-pane fade">
                    <form method="post">
                        <br>
                        <div class="btn-group">
                            <button type="submit" name="periodTraffic" value="day"
                                    class="btn btn-primary <c:if test="${periodTraffic == 'day'}">active</c:if>">Текущий
                                день
                            </button>
                            <button type="submit" name="periodTraffic" value="week"
                                    class="btn btn-primary <c:if test="${periodTraffic == 'week'}">active</c:if>">
                                Текущая неделя
                            </button>
                            <button type="submit" name="periodTraffic" value="month"
                                    class="btn btn-primary <c:if test="${periodTraffic == 'month'}">active</c:if>">
                                Текущий месяц
                            </button>
                        </div>
                        <c:choose>
                            <c:when test="${periodTraffic == 'day'}">
                                <c:set var="trafficHeader" value="день"/>
                            </c:when>
                            <c:when test="${periodTraffic == 'week'}">
                                <c:set var="trafficHeader" value="неделя"/>
                            </c:when>
                            <c:when test="${periodTraffic == 'month'}">
                                <c:set var="trafficHeader" value="месяц"/>
                            </c:when>
                        </c:choose>
                    </form>

                    <table id="trafficTbl" class="table table-hover table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>правило</th>
                            <th>заказы, ${trafficHeader}</th>
                            <th>трафик, ${trafficHeader}</th>
                            <th>конверсия, ${trafficHeader}</th>
                            <th>скидка, ${trafficHeader}</th>
                        </tr>
                        </thead>
                        <tfoot>
                        </tfoot>
                        <tbody>
                        <c:forEach var="tRow" items="${traffic}">
                            <tr><c:forEach var="tCell" items="${tRow}">
                                <td>${tCell}</td>
                            </c:forEach>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div id="yml" class="tab-pane fade">
                    <br>
                    <div class="col-sm-4">
                        <table class="table table-hover table-bordered" cellspacing="0" width="100%">
                            <thead>
                            </thead>
                            <tfoot>
                            </tfoot>
                            <tbody>
                            <c:forEach var="yRow" items="${yml}">
                                <tr>
                                    <td>${yRow.key}</td>
                                    <td>${yRow.value}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var language = {
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
    };

    $(document).ready(function () {
        // for bootstrap 3 use 'shown.bs.tab', for bootstrap 2 use 'shown' in the next line
        $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
            localStorage.setItem('activeTab', $(e.target).attr('href'));
        });
        var activeTab = localStorage.getItem('activeTab');
        if (activeTab) {
            $('a[href="' + activeTab + '"]').tab('show');
        } else {
            $('.nav-tabs a:first').tab('show');
        }

        $('#goodsTbl').DataTable({
            colReorder: true,
            dom: 'lfrtipB',
            buttons: [
                'excel'
            ],
            "ordering": false,
            "iDisplayLength": 25,
            "language": language,
            "ajax": {
                "url": "/datatable",
                "data": {
                    "type": "goods"
                }
            }
        });

        $('#trafficTbl').DataTable({
            colReorder: true,
            ordering: false,
            paging: false,
            language: language/*,
             ajax: {
             url: "/datatable",
             data: {
             "type": "traffic"
             }
             }*/
        });
    });
</script>
</body>
</html>
