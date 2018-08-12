<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 01.11.2016
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.8/select2-bootstrap.css"
          rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.8.0/css/bootstrap-datepicker.css" rel="stylesheet">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.8.0/js/bootstrap-datepicker.js"></script>

    <style type="text/css">
        .datepicker {
            z-index: 9999 !important
        }
    </style>
</head>
<body>
<div id="step3" class="tab-pane fade">
    <br>
    <div id="itemDiscountsAlert"></div>
    <a data-toggle="modal" href="#editItemDiscount" type="button" class="btn btn-success">
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Добавить скидку
    </a>
    <table id="itemDiscountsTbl" class="table table-hover table-bordered" cellspacing="0"
           width="100%">
    </table>
    <!-- Modal -->
    <div class="modal fade" id="editItemDiscount" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" align="center">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Скидки на отдельные товары</h4>
                </div>
                <div class="modal-body">
                    <div role="form">
                        <div class="form-group">
                            <label for="itemSelect">Товар</label>
                            <select class="js-example-basic-single form-control" style="width: 100%"
                                    id="itemSelect"></select>
                            <label for="discountItem1">Cкидка 1 уровня, %</label>
                            <input id="discountItem1" type="number" class="form-control">
                            <label for="discountItem2">Cкидка 2 уровня, %</label>
                            <input id="discountItem2" type="number" class="form-control">
                        </div>
                        <label for="startdate">Период действия</label>
                        <div class="form-inline">
                            <div class="form-group">
                                <div class="input-group date">
                                    <input id="startdate" type="text" name="startDate"
                                           class="form-control" data-date-format="dd.mm.yyyy" autocomplete="off">
                                    <%--<span class="input-group-addon btn"><i class="glyphicon glyphicon-th"></i></span>--%>
                                </div>
                                <div class="input-group date">
                                    <input id="enddate" type="text" name="endDate"
                                           class="form-control"
                                           data-date-format="dd.mm.yyyy" autocomplete="off">
                                    <%-- <span class="input-group-addon btn"><i class="glyphicon glyphicon-th"></i></span>--%>
                                </div>
                            </div>
                        </div>
                        <br><br><br><br>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Отмена
                    </button>
                    <button id="deleteItemDiscount" type="submit" class="btn btn-danger">Удалить
                    </button>
                    <button id="saveItemDiscount" type="submit" class="btn btn-primary">Сохранить
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    var itemDiscounts = $('#itemDiscountsTbl').DataTable({
        colReorder: true,
        ordering: false,
        paging: false,
        searching: false,
        language: language,
        ajax: {
            url: "offer_discounts",
            type: "GET",
            data: function (d) {
                d.type = "discounts";
            }
        },
        columns: [
            {
                data: 'id', render: function (data, type, full, meta) {
                return '<a data-id="' + full.id + '" data-articul="' + full.articul + '" data-name="' + full.name +
                        '" data-discount1="' + full.discount1 + '" data-discount2="' + full.discount2 +
                        '" data-startdate="' + full.startDate + '" data-enddate="' + full.endDate +
                        '" data-toggle="modal" href="#editItemDiscount"><span class="glyphicon glyphicon-pencil"></span></a>';
            }
            },
            {data: 'articul', title: 'артикул', defaultContent: ''},
            {data: 'name', title: 'название', defaultContent: ''},
            {data: 'discount1', title: 'скидка 1 шаг, %', defaultContent: ''},
            {data: 'discount2', title: 'скидка 2 шаг, %', defaultContent: ''},
            {data: 'url', title: 'адрес', defaultContent: ''},
            {data: 'startDate', title: 'действует с', defaultContent: ''},
            {data: 'endDate', title: 'действует по', defaultContent: ''}
        ]
    });

    var id;

    var checkin = $('#startdate').datepicker().on('changeDate', function (ev) {
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

    $('#editItemDiscount').on('shown.bs.modal', function (e) {
        $('#discountItem1').val($(e.relatedTarget).data('discount1'));
        $('#discountItem2').val($(e.relatedTarget).data('discount2'));
        $('#enddate').datepicker('update', $(e.relatedTarget).data('enddate'));
        $('#startdate').datepicker('update', $(e.relatedTarget).data('startdate'));
        id = $(e.relatedTarget).data('id');

        if (id != null) {
            $("#itemSelect").append("<option value='" + $(e.relatedTarget).data('articul') + "'>" + $(e.relatedTarget).data('name') + "</option>");
            $('#itemSelect').val($(e.relatedTarget).data('articul'));
        }

        $('#itemSelect').select2({
            theme: "bootstrap",
            language: {
                noResults: function () {
                    return "Ничего не найдено";
                },
                searching: function () {
                    return "Поиск...";
                },
                inputTooShort: function () {
                    return "Начните вводить название товара";
                }
            },
            ajax: {
                url: "offer_discounts",
                type: "GET",
                dataType: 'json',
                delay: 250,
                data: function (params) {
                    return {
                        type: "offers",
                        search: params.term
                    };
                },
                processResults: function (data) {
                    // parse the results into the format expected by Select2.
                    // since we are using custom formatting functions we do not need to
                    // alter the remote JSON data
                    return {
                        results: data.map(function (obj) {
                            return {id: obj.key, text: obj.value};
                        })
                    };
                },
                cache: true
            },
            escapeMarkup: function (markup) {
                return markup;
            }, // let our custom formatter work
            minimumInputLength: 1
        });
    }).on('hidden.bs.modal', function () {
        $('#discountItem1').val('');
        $('#discountItem2').val('');
        $("#itemSelect").val('');
        $('#startdate').val('');
        $('#enddate').val('');
    });

    $('#saveItemDiscount').on('click', function (e) {
        $.ajax({
            url: "offer_discounts",
            type: "POST",
            data: {
                type: "save",
                id: id,
                offerId: $('#itemSelect').val(),
                discount1: $('#discountItem1').val(),
                discount2: $('#discountItem2').val(),
                startDate: $('#startdate').val(),
                endDate: $('#enddate').val()
            }
        }).done(function (data) {
            if (!data.success) {
                alert('itemDiscountsAlert', data.error, 'danger', 5000);
            }
            $('#editItemDiscount').modal('hide');
            itemDiscounts.ajax.reload();
        });
    });

    $('#deleteItemDiscount').on('click', function (e) {
        $.ajax({
            url: "offer_discounts",
            type: "POST",
            data: {
                type: "delete",
                id: id
            }
        }).done(function (data) {
            if (!data.success) {
                alert('itemDiscountsAlert', data.error, 'danger', 5000);
            }
            $('#editItemDiscount').modal('hide');
            itemDiscounts.ajax.reload();
        });
    });
</script>
</html>
