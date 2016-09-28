<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 07.08.2016
  Time: 22:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Общие настройки</title>
    <link href="./css/bootstrap.min.css" rel="stylesheet">
    <link href="./css/dashboard.css" rel="stylesheet">
    <link href="./css/table.css" rel="stylesheet">
    <link href="./css/bootstrap-switch.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css" rel="stylesheet">
    <link href="./DataTables/datatables.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.18.0/skin-win8-n/ui.fancytree.css"
          rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css"
          rel="stylesheet">
    <link href="./Labelauty/jquery-labelauty.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.8/select2-bootstrap.css"
          rel="stylesheet"/>
    <link href="./css/titatoggle-dist.css" rel="stylesheet">

    <script src="./js/jquery-2.2.4.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
    <script src="./js/bootstrap.min.js"></script>
    <script src="./js/bootstrap-switch.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
    <script src="./DataTables/datatables.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.18.0/jquery.fancytree.js"></script>
    <script src="./js/bootstrap-select.min.js"></script>
    <script src="./Labelauty/jquery-labelauty.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>

    <style type="text/css">
        .password {
            padding-left: 1px;
        }

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
</head>
<body>
<%--Top bar--%>
<%@ include file="navbar.jsp" %>

<div class="container-fluid">
    <%--Sidebar--%>
    <%@ include file="menu.jsp" %>
    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <div class="checkbox checkbox-slider--b-flat checkbox-slider-info pull-right">
            <label>
                <input id="isAutoMode" type="checkbox"
                       <c:if test="${isAutoMode}">checked="checked"</c:if>><span>Авторежим</span>
            </label>
        </div>
        <ul class="nav nav-tabs" id="tabs">
            <li><a data-toggle="tab" href="#settings">Общие настройки</a></li>
            <c:if test="${!isAutoMode}">
                <li><a data-toggle="tab" href="#step1">География</a></li>
            </c:if>
            <li><a data-toggle="tab" href="#step2">Скидки для категорий и вендоров</a></li>
            <li><a data-toggle="tab" href="#step3">Скидки на отдельные товары</a></li>
            <li><a data-toggle="tab" href="#pages">Страницы-исключения</a></li>
            <c:if test="${!isScriptInstalled}">
                <li><a data-toggle="tab" href="#script">Ваш код установки</a></li>
            </c:if>
        </ul>
        <div class="tab-content">
            <div id="settings" class="tab-pane fade">
                <br>
                <c:if test="${!isScriptInstalled}">
                    <div class="alert alert-danger alert-dismissible fade in" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <strong>Установите JS-скрипт!</strong> Без установленного скрипта функционирование сервиса
                        невозможно. Информация в разделе "Ваш код установки".
                    </div>
                </c:if>
                <div class="alert alert-warning alert-dismissible fade in" role="alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <strong>Внимание!</strong> Изменение общих настроек следует выполнять с большой осторожностью.
                    Неверно указанные ссылки или значения могут критически повлиять на работу сервиса на Вашем сайте
                </div>
                <div role="form">
                    <div class="row">
                        <div class="form-group col-xs-6">
                            <label for="yml">Ссылка на выгрузку с товарами (YML)</label>
                            <input id="yml" type="text" class="form-control" value="${settings.yml}">
                            <br>
                            <label for="orderEmails">Email для рассылки заказов (возможно несколько адресов через точку
                                с запятой)</label>
                            <input id="orderEmails" type="text" class="form-control" value="${settings.orderEmails}">
                            <br>
                            <label for="contactEmails">Email для контактов и уведомлений (возможно несколько адресов
                                через точку с запятой)</label>
                            <input id="contactEmails" type="text" class="form-control"
                                   value="${settings.contactEmails}">
                            <br>
                            <label for="yandexKey">Ключ Яндекс.Метрика</label>
                            <input id="yandexKey" type="text" class="form-control" value="${settings.yandexKey}">
                            <br>
                            <label for="isEnabled">Виджет, состояние</label><br>
                            <input id="isEnabled" type="checkbox" class="toggle form-control" data-on-text="Включен"
                                   data-off-text="Выключен" <c:if test="${settings.isActive == 1}">checked</c:if>>
                            <%--                            <select id="isEnabled" class="selectpicker form-control">
                                                            <option value="1" <c:if test="${settings.isActive == 1}">selected="selected"</c:if>>
                                                                Включен
                                                            </option>
                                                            <option value="0" <c:if test="${settings.isActive == 0}">selected="selected"</c:if>>
                                                                Выключен
                                                            </option>
                                                        </select>--%>
                            <br><br>
                            <button id="saveSettings" type="submit" class="btn btn-primary center-block">Сохранить
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div id="pages" class="tab-pane fade">
                <br>
                Добавьте (или отредактируйте) страницы сайта, при заходе на которые показ виджета клиенту будет
                блокироваться на несколько дней (например, адрес корзины)
                <br><br>
                <a data-toggle="modal" href="#editPage" type="button" class="btn btn-success">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Добавить адрес
                </a>
                <table class="table table-hover table-bordered" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th></th>
                        <th>id</th>
                        <th>url</th>
                        <th>корзина</th>
                        <th>добавлена</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="url" items="${urls}">
                        <tr>
                            <td><a data-id="${url.id}" data-url="${url.url}" data-trash="${url.isTrash}"
                                   data-toggle="modal" href="#editPage"><span
                                    class="glyphicon glyphicon-pencil"></span></a></td>
                            <td>${url.id}</td>
                            <td>${url.url}</td>
                            <td><c:choose>
                                <c:when test="${url.isTrash == 1}">да</c:when>
                                <c:when test="${url.isTrash == 0}">нет</c:when>
                            </c:choose></td>
                            <td>${url.createTime}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <%--Modal--%>
                <div class="modal fade" id="editPage" role="dialog">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header" align="center">
                                <button type="button" class="close" data-dismiss="modal"
                                        aria-hidden="true">&times;</button>
                                <h4 class="modal-title">Редактирование страницы</h4>
                            </div>
                            <div class="modal-body">
                                <div role="form">
                                    <div class="form-group">
                                        <label for="url">Адрес</label>
                                        <input id="url" type="text" class="form-control" value="${url.url}">
                                        <br>
                                        <label for="isTrash">Это корзина</label>
                                        <br>
                                        <input id="isTrash" type="checkbox" class="toggle form-control"
                                               data-on-text="Да"
                                               data-off-text="Нет">
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Отмена
                                </button>
                                <button id="deletePage" type="submit" class="btn btn-danger">Удалить
                                </button>
                                <button id="savePage" type="submit" class="btn btn-primary">Сохранить
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%--Step 1--%>
            <div id="step1" class="tab-pane fade">
                <br>
                <%--<h4>Правила</h4>--%>
                <a data-toggle="modal" href="#editRule" type="button" class="btn btn-success">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Новое правило
                </a>
                <table id="rulesTbl" class="table table-hover table-bordered" cellspacing="0" width="100%">
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
                                                                <option value="${loop.index}">${loop.index < 10 ? '0'.concat(loop.index) : loop.index}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="hourTo">По</label>
                                                        <select id="hourTo" class="selectpicker"
                                                                data-live-search="true"
                                                                data-width="auto">
                                                            <c:forEach begin="0" end="24" varStatus="loop">
                                                                <option value="${loop.index}">${loop.index < 10 ? '0'.concat(loop.index) : loop.index}</option>
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
                                <button id="deleteRule" type="submit" class="btn btn-danger">Удалить
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
                                        <label for="itemSelect">Товар</label>
                                        <select class="js-example-basic-single form-control" style="width: 100%"
                                                id="itemSelect"></select>
                                        <label for="discountItem1">Cкидка 1 уровня, %</label>
                                        <input id="discountItem1" type="number" class="form-control">
                                        <label for="discountItem2">Cкидка 2 уровня, %</label>
                                        <input id="discountItem2" type="number" class="form-control">
                                        <br><br><br><br>
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
            <%--Script--%>
            <div id="script" class="tab-pane fade">
                <div class="col-sm-9">
                    <br>
                    <br>
                    <p>Если наш JS-скрипт не установлен, вставьте указанный ниже код на все страницы вашего сайта перед
                        закрывающим тегом &lt;/body&gt;:</p>
                    <br>
                    <p><code>
                        &lt;script id="huckster"&gt;
                        (function(w,d,s,c) {
                        var
                        h=document.getElementById('huckster'),r=d.createElement('script');w.Huckster={};w.Huckster.config=c||{};w.Huckster.config.server=s;r.setAttribute('src',
                        '//'+Huckster.config.server+'/app/huckster.min.js');h.parentNode.insertBefore(r,h.nextSibling);
                        })(window,document,'files.hucksterbot.com',{companyId:${companyId});
                        &lt;/script&gt;
                    </code></p></div>
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

    $(document).ready(function () {
        //select last tab or first tab by default
        // for bootstrap 3 use 'shown.bs.tab', for bootstrap 2 use 'shown' in the next line
        $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
            localStorage.setItem('sActiveTab', $(e.target).attr('href'));
        });
        var activeTab = localStorage.getItem('sActiveTab');
        if (activeTab) {
            console.log(activeTab);
            $('a[href="' + activeTab + '"]').tab('show');
        } else {
            $('.nav-tabs a:first').tab('show');
        }

        $('.toggle').bootstrapSwitch();
    });

    //Settings
    $('#saveSettings').on('click', function (e) {
        $.ajax({
            url: "settings",
            type: "POST",
            data: {
                type: "save_settings",
                yml: $('#yml').val(),
                orderEmails: $('#orderEmails').val(),
                contactEmails: $('#contactEmails').val(),
                yandexKey: $('#yandexKey').val(),
                isEnabled: $('#isEnabled').bootstrapSwitch('state')
            }
        }).done(function (msg) {
            location.reload();
        });
    });

    //Pages
    var id;
    $('#editPage').on('show.bs.modal', function (e) {
        id = $(e.relatedTarget).data('id');
        $('#url').val($(e.relatedTarget).data('url'));
        $('#isTrash').bootstrapSwitch('state', Boolean($(e.relatedTarget).data('trash')));
    });

    $('#savePage').on('click', function (e) {
        $.ajax({
            url: "settings",
            type: "POST",
            data: {
                type: "save_page",
                id: id,
                url: $('#url').val(),
                isTrash: $('#isTrash').bootstrapSwitch('state') ? 1 : 0
            }
        }).done(function (msg) {
            location.reload();
        });
    });

    $('#deletePage').on('click', function (e) {
        $.ajax({
            url: "settings",
            type: "POST",
            data: {
                type: "delete_page",
                id: id
            }
        }).done(function (msg) {
            location.reload();
        });
    });

    //Step 1
    var week = {1: '#mon', 2: '#tue', 3: '#wed', 4: '#thu', 5: '#fri', 6: '#sat', 7: '#sun'};
    var rules = $('#rulesTbl').DataTable({
        colReorder: true,
        ordering: false,
        paging: false,
        searching: false,
        language: language,
        ajax: {
            url: "widget_settings",
            type: "POST",
            data: function (d) {
                d.request = "ajax";
                d.type = "rules";
            }
        },
        columns: [
            {
                data: null, render: function (data, type, full, meta) {
                return '<a data-id="' + full.id + '" data-channels="' + full.channels + '" data-sources="' + full.sources + '" data-device="' + full.devices +
                        '" data-days="' + full.days + '" data-time-from="' + full.timeFrom + '" data-time-to="' + full.timeTo +
                        '" data-toggle="modal" href="#editRule"><span class="glyphicon glyphicon-pencil"></span></a>';
            }
            },
            {data: 'id', title: 'id', defaultContent: ''},
            {data: 'channels', title: 'каналы', defaultContent: ''},
            {data: 'sources', title: 'источники', defaultContent: ''},
            {
                data: 'strDevices', render: function (data) {
                return data.toLowerCase().replace('все устройства', 'все')
            }, title: 'устройства', defaultContent: ''
            },
            {
                data: null, render: function (data, type, full, meta) {
                return full.days + ', ' + full.timeFrom + '-' + full.timeTo + 'чч'
            }, title: 'режим запуска', defaultContent: ''
            }
        ]
    });

    //Step 2
    var vendorDiscounts = $('#vendorDiscountsTbl').DataTable({
        colReorder: true,
        ordering: false,
        paging: false,
        searching: false,
        language: language,
        ajax: {
            url: "widget_settings",
            type: "POST",
            data: function (d) {
                d.request = "ajax";
                d.type = "vendors_discounts";
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
            url: "widget_settings",
            type: "POST",
            data: function (d) {
                d.request = "ajax";
                d.type = "offers_discounts";
                d.ruleId = $('#segmentItemSelect').selectpicker('val')
            }
        },
        columns: [
            {
                data: 'id', render: function (data, type, full, meta) {
                return '<a data-id="' + full.id + '" data-articul="' + full.articul +
                        '" data-discount1="' + full.discount1 + '" data-discount2="' + full.discount2 +
                        '" data-toggle="modal" href="#editItemDiscount"><span class="glyphicon glyphicon-pencil"></span></a>';
            }
            },
            {data: 'articul', title: 'артикул', defaultContent: ''},
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
        if (activeTab && $('a[href="' + activeTab + '"]').val() != undefined) {
            $('a[href="' + activeTab + '"]').tab('show');
        } else {
            console.log('here');
            $('.nav-tabs a:first').tab('show');
        }

        $("#isAutoMode").change(function () {
            $.ajax({
                type: "POST",
                url: "settings",
                data: {
                    type: "auto_mode",
                    mode: this.checked
                }
            }).done(function (msg) {
                location.reload();
            });
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
                url: "widget_settings",
                type: "POST",
                data: {
                    request: "ajax",
                    type: "settings_tree"
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

    var id;

    //Step 1
    $('#editRule').on('show.bs.modal', function (e) {
        id = $(e.relatedTarget).data('id');
        // Reload tree with new source
        var tree = $("#fancyTree").fancytree("getTree");
        tree.reload({
            cache: false,
            url: "widget_settings",
            type: "POST",
            data: {
                request: "ajax",
                type: "settings_tree",
                id: $(e.relatedTarget).data('id')
            }
        }).done();

        //load select lists
        $.ajax({
            url: "widget_settings",
            type: "POST",
            data: {
                request: "ajax",
                type: "settings_lists"
            },
            success: function (data) {
                $('#chnl').empty();
                $('#src').empty();

                data.channels.forEach(function (item, i, arr) {
                    $('#chnl').append('<option>' + item + '</option>');
                });
                data.sources.forEach(function (item, i, arr) {
                    $('#src').append('<option>' + item + '</option>');
                });

                $('#chnl').selectpicker('refresh');
                $('#src').selectpicker('refresh');

                //set selected values
                if ($(e.relatedTarget).data('channels') != null) {
                    $('#chnl').selectpicker('val', $(e.relatedTarget).data('channels').split(":"));
                }
                if ($(e.relatedTarget).data('sources') != null) {
                    $('#src').selectpicker('val', $(e.relatedTarget).data('sources').split(":"))
                }
                $('#devices').selectpicker('val', $(e.relatedTarget).data('device'));
                $('#hourFrom').selectpicker('val', $(e.relatedTarget).data('time-from'));
                $('#hourTo').selectpicker('val', $(e.relatedTarget).data('time-to'));
                if ($(e.relatedTarget).data('days') != null) {
                    for (var day in week) {
                        if ($(e.relatedTarget).data('days').indexOf(day) < 0) {
                            $(week[day]).prop('checked', false);
                        }
                    }
                }
            }
        });
    }).on('hidden.bs.modal', function () {
        $('#chnl').empty().selectpicker('refresh');
        $('#src').empty().selectpicker('refresh');
        $('#devices').selectpicker('val', 0);
        $('#hourFrom').selectpicker('val', 0);
        $('#hourTo').selectpicker('val', 24);
        $('#mon').prop('checked', true);
        $('#tue').prop('checked', true);
        $('#wed').prop('checked', true);
        $('#thu').prop('checked', true);
        $('#fri').prop('checked', true);
        $('#sat').prop('checked', true);
        $('#sun').prop('checked', true);
    });

    $('#saveRule').on('click', function () {
        var selection = jQuery.map(
                jQuery('#fancyTree').fancytree('getRootNode').tree.getSelectedNodes(),
                function (node) {
                    return node.key;
                }
        );

        var days;
        for (var day in week) {
            if ($(week[day]).is(":checked")) {
                if (days == null)
                    days = day;
                else
                    days = days + ':' + day;
            }
        }

        $.ajax({
            type: "POST",
            url: "widget_settings",
            data: {
                type: "save_rule",
                id: id,
                tree: selection.join(":"),
                channels: ($('#chnl').selectpicker('val') || []).join(":"),
                sources: ($('#src').selectpicker('val') || []).join(":"),
                devices: $('#devices').selectpicker('val'),
                days: days,
                hourFrom: $('#hourFrom').selectpicker('val'),
                hourTo: $('#hourTo').selectpicker('val')
            }
        }).done(function (msg) {
            $('#editRule').modal('hide');
            rules.ajax.reload();
        });
    });

    $('#deleteRule').on('click', function (e) {
        $.ajax({
            url: "widget_settings",
            type: "POST",
            data: {
                type: "delete_rule",
                id: id
            }
        }).done(function (msg) {
            $('#editRule').modal('hide');
            rules.ajax.reload();
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
        id = $(e.relatedTarget).data('id');
        $('#priceFrom').val($(e.relatedTarget).data('minprice'));
        $('#priceTo').val($(e.relatedTarget).data('maxprice'));
        $('#discount1').val($(e.relatedTarget).data('discount1'));
        $('#discount2').val($(e.relatedTarget).data('discount2'));

        $.ajax({
            url: "widget_settings",
            type: "POST",
            data: {
                "request": "ajax",
                "type": "vendors_categories",
                "categoryId": $(e.relatedTarget).data('category')
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
            url: "widget_settings",
            type: "POST",
            data: {
                "request": "ajax",
                "type": "vendors",
                "categoryId": $('#catSelect').selectpicker('val')
            },
            success: function (data) {
                fillVendors(data);
            }
        });
        // fillVendors($('#catSelect').selectpicker('val'));
    });

    $('#saveVendorDiscount').on('click', function (e) {
        $.ajax({
            url: "widget_settings",
            type: "POST",
            data: {
                type: "save_vendor_discount",
                id: id,
                ruleId: $('#segmentSelect').selectpicker('val'),
                category: $('#catSelect').selectpicker('val'),
                vendors: ($('#vendorSelect').selectpicker('val') || []).join(":"),
                minPrice: $('#priceFrom').val(),
                maxPrice: $('#priceTo').val(),
                discount1: $('#discount1').val(),
                discount2: $('#discount2').val()
            }
        }).done(function (msg) {
            $('#editVendorDiscount').modal('hide');
            vendorDiscounts.ajax.reload();
        });
    });

    $('#deleteVendorDiscount').on('click', function (e) {
        $.ajax({
            url: "widget_settings",
            type: "POST",
            data: {
                type: "delete_vendor_discount",
                id: id
            }
        }).done(function (msg) {
            $('#editVendorDiscount').modal('hide');
            vendorDiscounts.ajax.reload();
        });
    });

    //Step 3
    $('#segmentItemSelect').on('changed.bs.select', function (e) {
        itemDiscounts.ajax.reload();
    });

    $('#editItemDiscount').on('show.bs.modal', function (e) {
        $('#discountItem1').val($(e.relatedTarget).data('discount1'));
        $('#discountItem2').val($(e.relatedTarget).data('discount2'));
        id = $(e.relatedTarget).data('id');

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
                url: "widget_settings",
                type: "POST",
                dataType: 'json',
                delay: 250,
                data: function (params) {
                    return {
                        request: "ajax",
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
        $('#itemSelect').val($(e.relatedTarget).data('id'));
    }).on('hidden.bs.modal', function () {
        $('#discountItem1').val('');
        $('#discountItem2').val('');
    });

    $('#saveItemDiscount').on('click', function (e) {
        $.ajax({
            url: "widget_settings",
            type: "POST",
            data: {
                type: "save_offer_discount",
                id: id,
                offerId: $('#itemSelect').val(),
                ruleId: $('#segmentSelect').selectpicker('val'),
                discount1: $('#discountItem1').val(),
                discount2: $('#discountItem2').val()
            }
        }).done(function (msg) {
            $('#editItemDiscount').modal('hide');
            itemDiscounts.ajax.reload();
        });
    });

    $('#deleteItemDiscount').on('click', function (e) {
        $.ajax({
            url: "widget_settings",
            type: "POST",
            data: {
                type: "delete_offer_discount",
                id: id
            }
        }).done(function (msg) {
            $('#editItemDiscount').modal('hide');
            itemDiscounts.ajax.reload();
        });
    });
</script>
</html>
