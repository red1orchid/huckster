<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 30.05.2016
  Time: 15:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Настройки виджета</title>
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/dashboard.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css"/>
    <link href="../css/table.css" rel="stylesheet">
    <link href="../DataTables/datatables.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.18.0/skin-win8-n/ui.fancytree.css"
          rel="stylesheet">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css">
    <link href="../Labelauty/jquery-labelauty.css" rel="stylesheet">

    <script src="../js/jquery-2.2.4.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
    <script src="../DataTables/datatables.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.18.0/jquery.fancytree.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>
    <script src="../Labelauty/jquery-labelauty.js"></script>

    <style type="text/css">
        /* Remove system outline for focused container */
        .ui-fancytree.fancytree-container:focus {
            outline: none;
        }

        .ui-fancytree.fancytree-container {
            border: none;
        }

        #editRuleStyle {
            width: 1100px;
            max-width: 95%; /* respsonsive width */
        }
    </style>
<body>
<%--Top bar--%>
<%@ include file="navbar.jsp" %>

<div class="container-fluid">
    <div class="row">
        <%--Sidebar--%>
        <%@ include file="menu.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ul class="nav nav-tabs" id="tabs">
                <li><a data-toggle="tab" href="#step1">шаг 1: Сегменты</a></li>
                <li><a data-toggle="tab" href="#step2">шаг 2: Скидки для категорий и вендоров</a></li>
                <li><a data-toggle="tab" href="#step3">шаг 3: Скидки на отдельные товары</a></li>
            </ul>

            <div class="tab-content">
                <%--Step 1--%>
                <div id="step1" class="tab-pane fade">
                    <br>
                    <%--<h4>Правила</h4>--%>
                    <a data-toggle="modal" href="#editRule" type="button" class="btn btn-success">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Новое правило
                    </a>
                    <table id="rulesTbl" class="table table-hover table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th></th>
                            <th>id</th>
                            <th>каналы</th>
                            <th>источники</th>
                            <th>устройства</th>
                            <th>режим запуска</th>
                        </tr>
                        </thead>
                        <tfoot>
                        </tfoot>
                        <tbody>
                        <c:forEach var="rRow" items="${rules}">
                            <tr>
                                <td><a data-id=${rRow.id} data-channels=${rRow.channels}
                                       data-sources=${rRow.sources} data-device=${rRow.devices}
                                       data-days=${rRow.days} data-time-from=${rRow.timeFrom}
                                       data-time-to=${rRow.timeTo} data-toggle="modal"
                                       href="#editRule"><span
                                        class="glyphicon glyphicon-pencil"></span></a></td>
                                <td>${rRow.id}</td>
                                <td>${rRow.channels}</td>
                                <td>${rRow.sources}</td>
                                <td>${devices[rRow.devices].toLowerCase().replace('все устройства', 'все')}</td>
                                <td>${rRow.days}, ${rRow.timeFrom}-${rRow.timeTo}чч</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                    <!-- Modal -->
                    <div id="editRule" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                         aria-hidden="true">
                        <div id="editRuleStyle" class="modal-dialog modal-lg">
                            <div class="modal-content">
                                <div class="modal-header" align="center">
                                    <button type="button" class="close" data-dismiss="modal"
                                            aria-hidden="true">&times;</button>
                                    <h4 class="modal-title" id="orderTitle">Сегменты пользователей</h4>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-sm-6">
                                            <label for="fancyTree">Регионы</label>
                                            <div id="fancyTree" name="fancyTree"></div>
                                        </div>
                                        <%--
                                                                                <div class="col-sm-1"></div>--%>
                                        <div class="col-sm-6">
                                            <div role="form">
                                                <div class="form-group">
                                                    <label for="chnl">Каналы</label>
                                                    <select id="chnl" class="selectpicker form-control"
                                                            multiple title="Все каналы"
                                                            data-live-search="true">
                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label for="src">Источники</label>
                                                    <select id="src" class="selectpicker form-control" multiple
                                                            title="Все источники"
                                                            data-live-search="true">
                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label for="devices">Устройства</label>
                                                    <select id="devices" class="selectpicker form-control">
                                                        <c:forEach var="device" items="${devices}">
                                                            <option value="${device.key}">${device.value}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label for="days">Дни</label>
                                                    <div id="days" class="form-inline">
                                                        <input id="mon" class="button-checkbox" type="checkbox"
                                                               data-labelauty="ПН|ПН" checked/>
                                                        <input id="tue" class="button-checkbox" type="checkbox"
                                                               data-labelauty="ВТ|ВТ" checked/>
                                                        <input id="wed" class="button-checkbox" type="checkbox"
                                                               data-labelauty="СР|СР" checked/>
                                                        <input id="thu" class="button-checkbox" type="checkbox"
                                                               data-labelauty="ЧТ|ЧТ" checked/>
                                                        <input id="fri" class="button-checkbox" type="checkbox"
                                                               data-labelauty="ПТ|ПТ" checked/>
                                                        <input id="sat" class="button-checkbox" type="checkbox"
                                                               data-labelauty="СБ|СБ" checked/>
                                                        <input id="sun" class="button-checkbox" type="checkbox"
                                                               data-labelauty="ВС|ВС" checked/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="hours">Часы</label>
                                                    <div id="hours" class="form-inline">
                                                        <div class="form-group">
                                                            <label for="hourFrom">C</label>
                                                            <select id="hourFrom" class="selectpicker"
                                                                    data-live-search="true" data-width="auto">
                                                                <c:forEach begin="0" end="24" varStatus="loop">
                                                                    <option>${loop.index < 10 ? '0'.concat(loop.index) : loop.index}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="hourTo">По</label>
                                                            <select id="hourTo" class="selectpicker"
                                                                    data-live-search="true"
                                                                    data-width="auto">
                                                                <c:forEach begin="0" end="24" varStatus="loop">
                                                                    <option>${loop.index < 10 ? '0'.concat(loop.index) : loop.index}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <%--
                                                                                            </div>--%>

                                            <%--
                                                <input class="text-nicelabel"
                                                       data-nicelabel='{"checked_text": "ПН", "unchecked_text": "ПН"}'
                                                       type="checkbox">
                                                <input class="text-nicelabel"
                                                       data-nicelabel='{"checked_text": "ВТ", "unchecked_text": "ВТ"}'
                                                       type="checkbox">--%>

                                            </form>
                                        </div>
                                    </div>
                                </div>

                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Отмена
                                    </button>
                                    <button id="saveRule" type="submit" class="btn btn-primary">Сохранить
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%--Step 2--%>
                <div id="step2" class="tab-pane fade">
                    <br>
                    Выберите сегмент, для которого хотите установить скидки на группы товаров
                    <br><br>
                    <select id="segmentSelect" class="selectpicker form-control">
                        <c:forEach var="segment" items="${segments}">
                            <option value="${segment.key}">${segment.value}</option>
                        </c:forEach>
                    </select>
                    <br><br>
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
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Отмена
                                    </button>
                                    <button id="deleteVendorDiscount" type="submit" class="btn btn-danger">Удалить
                                    </button>
                                    <button id="saveVendorDiscount" type="submit" class="btn btn-primary">Сохранить
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%--Step 3--%>
                <div id="step3" class="tab-pane fade">
                    <br>
                    Выберите сегмент, в котором вы хотите управлять индивидуальными скидками на товары (такие скидки
                    приоритетнее скидок для категорий и вендоров)
                    <br><br>
                    <select id="segmentItemSelect" class="selectpicker form-control">
                        <c:forEach var="segment" items="${segments}">
                            <option value="${segment.key}">${segment.value}</option>
                        </c:forEach>
                    </select>
                    <br><br>
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
                                            <label for="vendorItemSelect">Вендор</label>
                                            <select id="vendorItemSelect" class="selectpicker form-control"
                                                    title="<Все вендоры>" data-live-search="true">
                                            </select>
                                            <label for="itemSelect">Товар</label>
                                            <select id="itemSelect" class="selectpicker form-control"
                                                    title="...Выберите товар..."
                                                    data-live-search="true">
                                            </select>
                                            <label for="discountItem1">Cкидка 1 уровня, %</label>
                                            <input id="discountItem1" type="number" class="form-control">
                                            <label for="discountItem2">Cкидка 2 уровня, %</label>
                                            <input id="discountItem2" type="number" class="form-control">
                                            <br>
                                            <br>
                                            <br>
                                        </div>
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
            </div>
        </div>
    </div>
</div>
</body>

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

    //Step 2
    var vendorDiscounts = $('#vendorDiscountsTbl').DataTable({
        colReorder: true,
        ordering: false,
        paging: false,
        searching: false,
        language: language,
        ajax: {
            url: "/ajax",
            type: "POST",
            data: function (d) {
                d.type = "vendor_discounts";
                d.ruleId = $('#segmentSelect').selectpicker('val')
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

    //Step 3
    var itemDiscounts = $('#itemDiscountsTbl').DataTable({
        colReorder: true,
        ordering: false,
        paging: false,
        searching: false,
        language: language,
        ajax: {
            url: "/ajax",
            type: "POST",
            data: function (d) {
                d.type = "offer_discounts";
                d.ruleId = $('#segmentItemSelect').selectpicker('val')
            }
        },
        columns: [
            {
                data: 'id', render: function (data, type, full, meta) {
                return '<a data-id="' + full.id + '" data-vendor="' + full.vendor + '" data-articul="' + full.articul +
                        '" data-discount1="' + full.discount1 + '" data-discount2="' + full.discount2 +
                        '" data-toggle="modal" href="#editItemDiscount"><span class="glyphicon glyphicon-pencil"></span></a>';
            }
            },
            {data: 'articul', title: 'артикул', defaultContent: ''},
            {data: 'vendor', title: 'вендор', defaultContent: ''},
            {data: 'name', title: 'название', defaultContent: ''},
            {data: 'discount1', title: 'скидка 1 шаг, %', defaultContent: ''},
            {data: 'discount2', title: 'скидка 2 шаг, %', defaultContent: ''},
            {data: 'url', title: 'адрес', defaultContent: ''}
        ]
    });

    $(document).ready(function () {
        //Tab selection
        // for bootstrap 3 use 'shown.bs.tab', for bootstrap 2 use 'shown' in the next line
        $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
            localStorage.setItem('wActiveTab', $(e.target).attr('href'));
        });
        var activeTab = localStorage.getItem('wActiveTab');
        if (activeTab) {
            console.log(activeTab);
            $('a[href="' + activeTab + '"]').tab('show');
        } else {
            $('.nav-tabs a:first').tab('show');
        }

        //Step 1
        $('#rulesTbl').DataTable({
            colReorder: true,
            ordering: false,
            paging: false,
            searching: false,
            language: language
        });

        //button select
        $(".button-checkbox").labelauty({
            class: "labelauty",
            label: true,
            separator: "|",
            same_width: true
        });

        //tree
        $("#fancyTree").fancytree({
            checkbox: true,
            selectMode: 3,
            source: {
                cache: false,
                url: "/ajax",
                type: "POST",
                data: {
                    "type": "settings_tree"
                }
            },
            icon: false,
            select: function (event, data) {
                // Get a list of all selected nodes, and convert to a key array:
                var selKeys = $.map(data.tree.getSelectedNodes(), function (node) {
                    return node.key;
                });
                $("#echoSelection3").text(selKeys.join(", "));

                // Get a list of all selected TOP nodes
                var selRootNodes = data.tree.getSelectedNodes(true);
                // ... and convert to a key array:
                var selRootKeys = $.map(selRootNodes, function (node) {
                    return node.key;
                });
                $("#echoSelectionRootKeys3").text(selRootKeys.join(", "));
                $("#echoSelectionRoots3").text(selRootNodes.join(", "));
            },
            dblclick: function (event, data) {
                data.node.toggleSelected();
            },
            keydown: function (event, data) {
                if (event.which === 32) {
                    data.node.toggleSelected();
                    return false;
                }
            }
        });
    });

    //Step 1
    var id;
    $('#editRule').on('show.bs.modal', function (e) {
        id = $(e.relatedTarget).data('id');
        // Reload tree with new source
        var tree = $("#fancyTree").fancytree("getTree");
        tree.reload({
            cache: false,
            url: "/ajax",
            type: "POST",
            data: {
                "type": "settings_tree",
                "id": $(e.relatedTarget).data('id')
            }
        }).done();

        //load select lists
        $.ajax({
            url: "/ajax",
            type: "POST",
            data: {
                "type": "settings_lists"
            },
            success: function (data) {
                $('#chnl').empty();
                $('#src').empty();

                data.channels.forEach(function (item, i, arr) {
                    $('#chnl').append('<option>' + item + '</option>');
                });
                for (var i in data.sources) {
                    $('#src').append('<option value="' + i + '">' + data.sources[i] + '</option>');
                }

                $('#chnl').selectpicker('refresh');
                $('#src').selectpicker('refresh');

                //set selected values
                $('#chnl').selectpicker('val', $(e.relatedTarget).data('channels').split(":"));
                $('#src').selectpicker('val', $(e.relatedTarget).data('sources').split(":"));
                $('#devices').selectpicker('val', $(e.relatedTarget).data('device'));
                $('#hourFrom').selectpicker('val', $(e.relatedTarget).data('time-from'));
                $('#hourTo').selectpicker('val', $(e.relatedTarget).data('time-to'));
                var days = $(e.relatedTarget).data('days');
                for (i = 1; i <= 7; i++) {
                    if (days.indexOf(i.toString()) < 0) {
                        $('#mon').prop('checked', false);
                    }
                }
            }
        });
    });

    $('#saveRule').on('click', function () {
        var selection = jQuery.map(
                jQuery('#fancyTree').fancytree('getRootNode').tree.getSelectedNodes(),
                function (node) {
                    return node.key;
                }
        );

        var days;

        function addDay(id, day) {
            if ($(id).is(":checked")) {
                if (days == null)
                    days = day;
                else
                    days = days + ':' + day;
            }
        }

        addDay('#mon', 1);
        addDay('#tue', 2);
        addDay('#wed', 3);
        addDay('#thu', 4);
        addDay('#fri', 5);
        addDay('#sat', 6);
        addDay('#sun', 7);

        $.ajax({
            type: "POST",
            url: "/widget_settings",
            data: {
                rule: id,
                tree: selection.join(":"),
                channels: ($('#chnl').selectpicker('val') || []).join(":"),
                sources: ($('#src').selectpicker('val') || []).join(":"),
                devices: $('#devices').selectpicker('val'),
                days: days,
                hourFrom: $('#hourFrom').selectpicker('val'),
                hourTo: $('#hourTo').selectpicker('val')
            }
        }).done(function (msg) {
            //do other processing
        });
    });

    //Step 2
    $('#segmentSelect').on('changed.bs.select', function (e) {
        vendorDiscounts.ajax.reload();
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

    $('#editVendorDiscount').on('show.bs.modal', function (e) {
        $('#priceFrom').val($(e.relatedTarget).data('minprice'));
        $('#priceTo').val($(e.relatedTarget).data('maxprice'));
        $('#discount1').val($(e.relatedTarget).data('discount1'));
        $('#discount2').val($(e.relatedTarget).data('discount2'));

        var categoryId = $(e.relatedTarget).data('category');
        if (categoryId == null) {
            categoryId = undefined;
        }
        $.ajax({
            url: "/ajax",
            type: "POST",
            data: {
                "type": "step2_all",
                "categoryId": categoryId
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
            url: "/ajax",
            type: "POST",
            data: {
                "type": "step2_vendors",
                "categoryId": $('#catSelect').selectpicker('val')
            },
            success: function (data) {
                fillVendors(data);
            }
        });
        // fillVendors($('#catSelect').selectpicker('val'));
    });

    //Step 3
    $('#segmentItemSelect').on('changed.bs.select', function (e) {
        itemDiscounts.ajax.reload();
    });

    $('#editItemDiscount').on('show.bs.modal', function (e) {
        $('#discountItem1').val($(e.relatedTarget).data('discount1'));
        $('#discountItem2').val($(e.relatedTarget).data('discount2'));

        $.ajax({
            url: "/ajax",
            type: "POST",
            data: {
                "type": "step3_vendors"
            },
            success: function (data) {
                $('#vendorItemSelect').empty();
                if (data != null) {
                    for (var i = 0; i < data.length; i++) {
                        $('#vendorItemSelect').append('<option>' + data[i] + '</option>');
                    }
                }
                $('#vendorItemSelect').selectpicker('refresh');
              //  $('#vendorItemSelect').val($(e.relatedTarget).data('vendor'));
                $('#itemSelect').val($(e.relatedTarget).data('id'));
            }
        });
    }).on('hidden.bs.modal', function () {
        $('#vendorItemSelect').empty().selectpicker('refresh');
        $('#itemSelect').empty().selectpicker('refresh');
        $('#discountItem1').val('');
        $('#discountItem2').val('');
    });

    $('#vendorItemSelect').on('changed.bs.select', function (e) {
        $.ajax({
            url: "/ajax",
            type: "POST",
            data: {
                "type": "step3_offers",
                "vendor": $('#vendorItemSelect').selectpicker('val')
            },
            success: function (data) {
                $('#itemSelect').empty();
                if (data != null) {
                    for (var i = 0; i < data.length; i++) {
                        $('#itemSelect').append('<option value="' + data[i].key + '">' + data[i].value + '</option>');
                    }
                }
                $('#itemSelect').selectpicker('refresh');
            }
        });
        // fillVendors($('#catSelect').selectpicker('val'));
    });


</script>
</html>
