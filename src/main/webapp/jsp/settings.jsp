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

    <script src="./js/jquery-2.2.4.min.js"></script>
    <script src="./js/bootstrap.min.js"></script>
    <script src="./js/bootstrap-switch.js"></script>

    <style type="text/css">
        .password {
            padding-left: 1px;
        }
    </style>
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
                <li><a data-toggle="tab" href="#settings">Общие настройки</a></li>
                <li><a data-toggle="tab" href="#pages">Страницы-исключения</a></li>
                <li><a data-toggle="tab" href="#password">Смена пароля доступа</a></li>
                <li><a data-toggle="tab" href="#setup">Ваш код установки</a></li>
            </ul>

            <div class="tab-content">
                <div id="settings" class="tab-pane fade">
                    <br>
                    <div class="alert alert-warning alert-dismissible fade in" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <strong>Внимание!</strong> Изменение общих настроек следует выполнять с большой осторожностью.
                        Неверно указанные ссылки или значения могут критически повлиять на работу сервиса на Вашем сайте
                    </div>
                    <div role="form">
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
                <div id="password" class="tab-pane fade">
                    <div role="form">
                        <br>
                        Для смены пароля доступа в личный кабинет введите текущий пароль, новый пароль и подтверждение
                        нового пароля
                        <br>
                        <div id="passAlert"></div>
                        <div class="form-group password col-xs-4">
                            <br>
                            <input id="oldPassword" type="password" class="form-control"
                                   placeholder="введите текущий пароль">
                            <br>
                            <input id="newPassword" type="password" class="form-control"
                                   placeholder="введите новый пароль">
                            <br>
                            <input id="newPasswordConfirm" type="password" class="form-control"
                                   placeholder="введите новый пароль еще раз">
                            <br>
                            <button id="savePassword" type="submit" class="btn btn-primary center-block">Сохранить
                            </button>
                        </div>
                    </div>
                </div>
                <div id="setup" class="tab-pane fade">
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
                    </code></p><br>
                    <p>P.S. Без установленного скрипта функционирование сервиса невозможно.</p>
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

    //Password
    $('#passAlert').hide();
    bootstrap_alert = function () {
    }
    bootstrap_alert.error = function (message, timeOut) {
        $('#passAlert').html('<div class="alert alert-danger alert-dismissible fade in" role="alert">' +
                '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                '<span aria-hidden="true">&times;</span></button>' + message + '</div>').fadeTo(timeOut, 500).slideUp(500, function () {
            $("#passAlert").slideUp(500);
        }).show();
    };
    bootstrap_alert.success = function (message, timeOut) {
        $('#passAlert').html('<div class="alert alert-success alert-dismissible fade in" role="alert">' +
                '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                '<span aria-hidden="true">&times;</span></button>' + message + '</div>').fadeTo(timeOut, 500).slideUp(500, function () {
            $("#passAlert").slideUp(500);
        }).show();
    };


    $('#savePassword').on('click', function (e) {
        if ($('#oldPassword').val() == '' || $('#newPassword').val() == '' || $('#newPasswordConfirm').val() == '') {
            bootstrap_alert.error('Пароль не может быть пустым', 5000);
        } else if ($('#newPasswordConfirm').val() != $('#newPassword').val()) {
            bootstrap_alert.error('Введенные пароли не совпадают', 5000);
        } else {
            $.ajax({
                url: "settings",
                type: "POST",
                data: {
                    "request": "ajax",
                    "type": "save_password",
                    "oldPassword": $('#oldPassword').val(),
                    "newPassword": $('#newPassword').val()
                }
            }).done(function (data) {
                console.log(data);
                if (data.success) {
                    bootstrap_alert.success('Пароль успешно сохранен', 2000);
                } else {
                    if (data.error == "wrong_password") {
                        bootstrap_alert.error('Введен неверный пароль', 5000);
                    } else {
                        bootstrap_alert.error('В данный момент сохранение невозможно', 5000);
                    }
                }
                $('#oldPassword').val('');
                $('#newPassword').val('');
                $('#newPasswordConfirm').val('');
            })
        }
    });
</script>
</html>
