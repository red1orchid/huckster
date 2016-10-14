<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 17.05.2016
  Time: 12:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Работа с заказами</title>
    <link href="./DataTables/datatables.min.css" rel="stylesheet">
    <link href="./css/table.css" rel="stylesheet">
    <link href="./css/bootstrap.min.css" rel="stylesheet">
    <link href="./css/dashboard.css" rel="stylesheet">
    <link href="./css/datepicker.css" rel="stylesheet">

    <script src="./js/jquery-2.2.4.min.js"></script>
    <script src="./DataTables/datatables.min.js"></script>
    <script src="./js/bootstrap.min.js"></script>
    <script src="./js/bootstrap-datepicker.js"></script>
</head>
<body>

<fmt:requestEncoding value="UTF-8"/>

<%--Top bar--%>
<%@ include file="navbar.jsp" %>

<div class="container-fluid">
    <div class="row">
        <%--Sidebar--%>
        <%@ include file="menu.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h5 class="page-header">Если вам необходимо выгружать заказы по API - напишите нам на
                <a href="mailto:support@hucksterbot.ru">support@hucksterbot.ru</a>.</h5>
            ${sessionScope.activeTab}
            <c:if test="${sessionScope.activeTab == 'yml'}">in active</c:if>

            <div class="row">
                <div id="alerts"></div>
                <form method="post">
                    <div class="col-sm-2 form-group">
                        <input id="startdate" type="text" name="startDate" value="${startDate}" class="span2"
                               data-date-format="dd.mm.yyyy">
                    </div>
                    <div class="col-sm-2 form-group">
                        <input id="enddate" type="text" name="endDate" value="${endDate}" class="span2"
                               data-date-format="dd.mm.yyyy">
                    </div>
                    <div class="col-sm-2 form-group">
                        <button type="submit" class="btn btn-primary btn-sm">OK</button>
                    </div>
                </form>
            </div>

            <table id="orders" class="table table-hover table-bordered" cellspacing="0" width="100%">
            </table>

            <!-- Modal -->
            <form class="modal fade" id="editOrder" tabindex="-1" role="dialog"
                  aria-labelledby="myModalLabel"
                  aria-hidden="true">
                <div class="modal-dialog modal-sm">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal"
                                    aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="orderTitle"></h4>
                        </div>
                        <div class="modal-body">
                            <div role="form">
                                <div class="form-group">
                                    <label for="status">Статус:</label>
                                    <select id="status" class="form-control">
                                        <c:forEach var="entry" items="${statuses}">
                                            <option value="<c:out value="${entry.key}"/>"><c:out
                                                    value="${entry.value}"/></option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="comment">Комментарий:</label>
                                    <textarea id="comment" class="form-control" rows="5" id="comment"></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                            <button type="submit" id="saveOrder" class="btn btn-primary">Сохранить</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script type="text/javascript">
    var ordersTable = $('#orders').DataTable({
        colReorder: true,
        iDisplayLength: 25,
        order: [[1, 'desc']],
        columnDefs: [
            {orderable: false, targets: 0}
        ],
        language: {
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
        ajax: {
            url: "orders",
            type: "POST",
            data: {
                "request": "ajax"
            }
        },
        columns: [
            {
                data: null, render: function (data, type, full, meta) {
                return '<a data-id="' + full.id + '" data-status="' + full.status + '" data-comment="' + full.comment + '" data-toggle="modal" href="#editOrder"><span class="glyphicon glyphicon-pencil"></span></a>';
            }
            },
            {data: 'id', title: 'заказ', defaultContent: ''},
            {data: 'articul', title: 'артикул', defaultContent: ''},
            {data: 'vendorCode', title: 'код вендора', defaultContent: ''},
            {data: 'model', title: 'модель', defaultContent: ''},
            {data: 'basePrice', title: 'цена базовая', defaultContent: ''},
            {data: 'resultPrice', title: 'цена итоговая', defaultContent: ''},
            {data: 'discount', title: 'скидка', defaultContent: ''},
            {data: 'phone', title: 'телефон', defaultContent: ''},
            {data: 'city', title: 'город', defaultContent: ''},
            {data: 'creationDate', title: 'создан', defaultContent: ''},
            {data: 'phrase', title: 'фраза', defaultContent: ''},
            {data: 'statusTitle', title: 'статус', defaultContent: ''},
            {data: 'comment', title: 'комментарий', defaultContent: ''}
        ]
    });

    $(document).ready(function () {
        var checkin = $('#startdate').datepicker().on('changeDate', function (ev) {
            if (ev.date.valueOf() > checkout.date.valueOf()) {
                var startDate = new Date(ev.date);
                checkout.setValue(startDate);
            } else {
                checkout.setValue(checkout.date);
            }
            checkin.hide();
            $('#enddate')[0].focus();
        }).data('datepicker');
        var checkout = $('#enddate').datepicker({
            onRender: function (date) {
                return date.valueOf() <= checkin.date.valueOf() ? 'disabled' : '';
            }
        }).on('changeDate', function (ev) {
            checkout.hide();
        }).data('datepicker');
    });

    function alert(message, type, time) {
        $('#alerts').html('<div class="alert alert-' + type + ' alert-dismissible fade in" role="alert"> ' +
                ' <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
                message + '</div>');

        if (time != null) {
            $('#alerts').fadeTo(time, 500).slideUp(500, function () {
                $("#alerts").slideUp(500);
            });
        }
    }

    var id;

    $('#editOrder').on('show.bs.modal', function (e) {
        id = $(e.relatedTarget).data('id');
        var status = $(e.relatedTarget).data('status');
        var comment = $(e.relatedTarget).data('comment');
        document.getElementById('orderTitle').innerHTML = "Заказ №".concat(id);
        document.getElementById('status').selectedIndex = status;
        document.getElementById('comment').value = comment;
    });

    $('#editOrder').on('submit', function (e) {
        $.ajax({
            url: "orders",
            type: "POST",
            data: {
                type: "save_order",
                id: id,
                status: $('#status').val(),
                comment: $('#comment').val()
            },
            success: function (data) {
                if (!data.success) {
                    $('#editOrder').modal('hide');
                    alert(data.error, 'danger', 5000);
                } else {
                    location.reload();
                }
            }
        });

        e.preventDefault();
    });

    /*{
     "processing": true,
     "serverSide": true,
     "ordering": false,
     ,
     "ajax": "/datatable"
     }*/
</script>
</body>
</html>
