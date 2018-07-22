<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 01.11.2016
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="./css/bootstrap-switch.css" rel="stylesheet">

    <script src="./js/bootstrap-switch.js"></script>
</head>
<body>
<div id="settings" class="tab-pane fade">
    <br>
    <div id="settingsAlert"></div>
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
                <label for="yandexKey">Ключ Яндекс.Метрики</label>
                <input id="yandexKey" type="number" class="form-control" value="${settings.yandexKey}">
                <br>
                <label for="workMode">Виджет</label><br>
                <button id="widgetPreview" type="submit"
                        class="btn btn-success <c:if test="${!isWidgetActive}">disabled</c:if>"><span
                        class="glyphicon glyphicon-play"></span>Предпросмотр
                </button>
                <form id="workMode" class="input-group pull-right">
                    <div class="btn-group" data-toggle="buttons">
                        <label class="btn btn-default <c:if test="${settings.workMode == 'on'}">active</c:if>">
                            <input type="radio" class="workMode"  value="on"> Включен
                        </label>
                        <label class="btn btn-default <c:if test="${settings.workMode == 'off'}">active</c:if>">
                            <input type="radio" class="workMode" value="off"> Выключен
                        </label>
                        <label class="btn btn-default <c:if test="${settings.workMode == 'button'}">active</c:if>">
                            <input type="radio" class="workMode" value="button"> Кнопка
                        </label>
                    </div>
                </form>
                <br><br>
                <button id="saveSettings" type="submit" class="btn btn-primary center-block">Сохранить
                </button>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    $('#widgetPreview').on('click', function (e) {
        $.ajax({
            url: "settings",
            type: "GET",
            async: false,
            data: {
                type: "widget_url"
            },
            success: function (data) {
                window.open(data, '_blank');
            }
        });
    });

    /*   $('#workMode .btn').on('click', function(event) {
        console.log($(this).find('input').val());
        var val = $(this).find('input').val();
        $('#output').html(val);
    });
    $(".workMode").click(function(){
        $(".workMode").removeClass("active");
        $(this).addClass("active");
    });*/

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
                workMode: $('.workMode:checked').val()
            }
        }).done(function (data) {
            if (!data.success) {
                alert('settingsAlert', data.error, 'danger', 5000);
            } else {
                location.reload();
            }
        });
    });</script>
</html>
