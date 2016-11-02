<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 01.11.2016
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<div id="step2" class="tab-pane fade">
    <br>
    <div id="vendorDiscountsAlert"></div>
    <a data-toggle="modal" href="#editVendorDiscount" type="button" class="btn btn-success">
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Добавить скидку
    </a>
    <table id="vendorDiscountsTbl" class="table table-hover table-bordered" cellspacing="0"
           width="100%">
    </table>
    <!-- Modal -->
    <div class="modal fade" id="editVendorDiscount" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" align="center">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Скидки для категорий товаров и вендоров</h4>
                </div>
                <div class="modal-body">
                    <div role="form">
                        <div class="form-group">
                            <label for="catSelect">Категория</label>
                            <select id="catSelect" class="selectpicker form-control"
                                    title="<Все категории>" data-live-search="true">
                            </select>
                            <label for="vendorSelect">Вендоры</label>
                            <select id="vendorSelect" class="selectpicker form-control"
                                    multiple title="<Все вендоры>"
                                    data-live-search="true">
                            </select>
                            <label for="priceFrom">Цена, от</label>
                            <input id="priceFrom" type="number" class="form-control">
                            <label for="priceTo">Цена, до</label>
                            <input id="priceTo" type="number" class="form-control">
                            <label for="discount1">Cкидка 1 уровня, %</label>
                            <input id="discount1" type="number" class="form-control">
                            <label for="discount2">Cкидка 2 уровня, %</label>
                            <input id="discount2" type="number" class="form-control">
                            <br>
                            <br>
                            <br>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                    <button id="deleteVendorDiscount" type="submit" class="btn btn-danger">Удалить
                    </button>
                    <button id="saveVendorDiscount" type="submit" class="btn btn-primary">Сохранить
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    var vendorDiscounts = $('#vendorDiscountsTbl').DataTable({
        colReorder: true,
        ordering: false,
        paging: false,
        searching: false,
        language: language,
        ajax: {
            url: "vendor_discounts",
            type: "GET",
            data: function (d) {
                d.type = "discounts"
            }
        },
        columns: [
            {
                data: 'id', render: function (data, type, full, meta) {
                return '<a data-id="' + full.id + '" data-category="' + full.categoryId + '" data-vendor="' + full.vendor + '" data-minprice="' + full.minPrice +
                        '" data-maxprice="' + full.maxPrice + '" data-discount1="' + full.discount1 + '" data-discount2="' + full.discount2 +
                        '" data-toggle="modal" href="#editVendorDiscount"><span class="glyphicon glyphicon-pencil"></span></a>';
            }
            },
            {data: 'category', title: 'категория', defaultContent: ''},
            {data: 'vendor', title: 'вендор', defaultContent: ''},
            {data: 'minPrice', title: 'цена, от', defaultContent: ''},
            {data: 'maxPrice', title: 'цена, до', defaultContent: ''},
            {data: 'discount1', title: 'скидка 1 шаг, %', defaultContent: ''},
            {data: 'discount2', title: 'скидка 2 шаг, %', defaultContent: ''}
        ]
    });

    function fillCategories(data) {
        $('#catSelect').empty().append('<option value=""><Все категории></option>');
        if (data != null) {
            for (var i = 0; i < data.length; i++) {
                $('#catSelect').append('<option value="' + data[i].key + '">' + data[i].value + '</option>');
            }
        }
        $('#catSelect').selectpicker('refresh');
    }

    function fillVendors(data) {
        $('#vendorSelect').empty();
        if (data != null) {
            for (var i = 0; i < data.length; i++) {
                $('#vendorSelect').append('<option>' + data[i] + '</option>');
            }
        }
        $('#vendorSelect').selectpicker('refresh');
    }

    var id;
    $('#editVendorDiscount').on('show.bs.modal', function (e) {
        id = $(e.relatedTarget).data('id');
        $('#priceFrom').val($(e.relatedTarget).data('minprice') != null ? $(e.relatedTarget).data('minprice') : 0);
        $('#priceTo').val($(e.relatedTarget).data('maxprice') != null ? $(e.relatedTarget).data('maxprice') : 1000000000);
        $('#discount1').val($(e.relatedTarget).data('discount1'));
        $('#discount2').val($(e.relatedTarget).data('discount2'));

        $.ajax({
            url: "vendor_discounts",
            type: "GET",
            data: {
                type: "vendors_categories",
                categoryId: $(e.relatedTarget).data('category')
            },
            success: function (data) {
                fillCategories(data.categories);
                $('#catSelect').selectpicker('val', $(e.relatedTarget).data('category'));
                fillVendors(data.vendors);
                if ($(e.relatedTarget).data('vendor') != null) {
                    $('#vendorSelect').selectpicker('val', $(e.relatedTarget).data('vendor').split(":"));
                }
            }
        });
    }).on('hidden.bs.modal', function () {
        $('#catSelect').empty().selectpicker('refresh');
        $('#vendorSelect').empty().selectpicker('refresh');
        $('#priceFrom').val('');
        $('#priceTo').val('');
        $('#discount1').val('');
        $('#discount2').val('');
    });

    $('#catSelect').on('changed.bs.select', function (e) {
        $.ajax({
            url: "vendor_discounts",
            type: "GET",
            data: {
                type: "vendors",
                categoryId: $('#catSelect').selectpicker('val')
            },
            success: function (data) {
                fillVendors(data);
            }
        });
        // fillVendors($('#catSelect').selectpicker('val'));
    });

    $('#saveVendorDiscount').on('click', function (e) {
        $.ajax({
            url: "vendor_discounts",
            type: "POST",
            data: {
                type: "save",
                id: id,
                category: $('#catSelect').selectpicker('val'),
                vendors: ($('#vendorSelect').selectpicker('val') || []).join(":"),
                minPrice: $('#priceFrom').val(),
                maxPrice: $('#priceTo').val(),
                discount1: $('#discount1').val(),
                discount2: $('#discount2').val()
            }
        }).done(function (data) {
            if (!data.success) {
                alert('vendorDiscountsAlert', data.error, 'danger', 5000);
            }
            $('#editVendorDiscount').modal('hide');
            vendorDiscounts.ajax.reload();
        });
    });

    $('#deleteVendorDiscount').on('click', function (e) {
        $.ajax({
            url: "vendor_discounts",
            type: "POST",
            data: {
                type: "delete",
                id: id
            }
        }).done(function (data) {
            if (!data.success) {
                alert('vendorDiscountsAlert', data.error, 'danger', 5000);
            }
            $('#editVendorDiscount').modal('hide');
            vendorDiscounts.ajax.reload();
        });
    });</script>
</html>
