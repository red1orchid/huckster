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
    <link href="../css/table.css" rel="stylesheet">
    <link href="../css/datepicker.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.datatables.net/u/dt/dt-1.10.12,cr-1.3.2/datatables.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css">

    <script src="../js/jquery-2.2.4.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="https://cdn.datatables.net/u/dt/dt-1.10.12,cr-1.3.2/datatables.min.js"></script>
    <script src="../js/bootstrap-datepicker.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>
    <!-- (Optional) Latest compiled and minified JavaScript translation files -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/i18n/defaults-*.min.js"></script>

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
                <!-- Modal -->
                <div class="modal fade" id="editOrder" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-sm">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title" id="orderTitle"></h4>
                            </div>
                            <div class="modal-body">
                                    <select class="selectpicker">
                                        <option>Mustard</option>
                                        <option>Ketchup</option>
                                        <option>Relish</option>
                                    </select>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                                <button type="button" class="btn btn-primary">Сохранить</button>
                            </div>
                        </div>
                    </div>
                </div>

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

                <table id="example" class="table table-hover table-bordered" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th></th>
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
                    <%--                    <tbody>
                                        <c:forEach var="row" items="${orders}">
                                            <tr>
                                                    &lt;%&ndash;                        <tr>
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
                                                                            </tr>&ndash;%&gt;
                                                <c:forEach var="cell" items="${row}">
                                                    <td>${cell}</td>
                                                </c:forEach>
                                            </tr>
                                        </c:forEach>--%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#example').DataTable({
            colReorder: true,
            "iDisplayLength": 25,
            "order": [[1, 'desc']],
            "columnDefs": [
                {"orderable": false, "targets": 0}
            ],
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
                "dataSrc": "data"
            }
        });

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

        $('#editOrder').on('show.bs.modal', function(e) {
            var bookId = $(e.relatedTarget).data('id');
            document.getElementById('orderTitle').innerHTML = "Заказ №".concat(bookId);
        });

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
