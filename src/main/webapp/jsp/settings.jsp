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
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/dashboard.css" rel="stylesheet">

    <script src="../js/jquery-2.2.4.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
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
                    <div role="form">
                        <div class="form-group col-xs-6">
                            <br>
                            <label for="yml">Ссылка на выгрузку с товарами (YML)</label>
                            <input id="yml" type="text" class="form-control" value="${settings.yml}">
                            <br>
                            <label for="orderEmails">Email для рассылки заказов (возможно несколько адресов через точку с запятой)</label>
                            <input id="orderEmails" type="text" class="form-control" value="${settings.orderEmails}">
                            <br>
                            <label for="contactEmails">Email для контактов и уведомлений (возможно несколько адресов через точку с запятой)</label>
                            <input id="contactEmails" type="text" class="form-control" value="${settings.contactEmails}">
                            <br>
                            <label for="yandexKey">Ключ Яндекс.Метрика</label>
                            <input id="yandexKey" type="text" class="form-control" value="${settings.yandexKey}">
                            <br>
                            <label for="isEnabled">Виджет, состояние</label>
                            <select id="isEnabled" class="selectpicker form-control">
                                <option value="1">Включен</option>
                                <option value="0">Выключен</option>
                            </select>
                            <br>
                            <button id="saveSettings" type="submit" class="btn btn-primary">Сохранить
                            </button>
                        </div>
                    </div>
                </div>
                <div id="pages" class="tab-pane fade"></div>
                <div id="password" class="tab-pane fade"></div>
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
    });
</script>
</html>
