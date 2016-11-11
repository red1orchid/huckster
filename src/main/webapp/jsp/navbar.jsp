<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 17.05.2016
  Time: 13:17
  To change this template use File | Settings | File Templates.
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href=".">${company.name}, ID ${company.id}</a>
        </div>
        <ul class="nav navbar-nav navbar-right">
            <li>
                <a href="#" data-toggle="dropdown"><span class="glyphicon glyphicon-user"></span><span
                        class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="#editPassword" data-toggle="modal"><span class="glyphicon glyphicon-wrench"></span>Изменить
                        пароль</a></li>
                </ul>
            </li>
            <c:if test="${isWidgetActive}">
                <li><a data-placement="bottom" title="Предпросмотр виджета" href="#"><span
                        class="glyphicon glyphicon-play green widget_preview"></span></a></li>
            </c:if>

            <li><a data-placement="bottom" title="Скачать инструкцию"
                   href="http://hucksterbot.ru/HucksterBot_Guide_2016.pdf" download><span
                    class="glyphicon glyphicon-question-sign"></span></a></li>
            <li><a href="logout"><span class="glyphicon glyphicon-log-out"></span> Выход</a></li>
        </ul>
    </div>
</nav>
<%--<div class="alert alert-success alert-dismissible fade in pull-right" role="alert" style="position: absolute; width: 40%; z-index: 3">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
        <span aria-hidden="true">&times;</span>
    </button>
    <strong>Holy guacamole!</strong> You should check in on some of those fields below.
</div>--%>
<div id="passSuccess"></div>
<%--Modal--%>
<div class="modal fade" id="editPassword" role="dialog">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Смена пароля</h4>
            </div>
            <div class="modal-body">
                <div id="passAlert"></div>
                <div class="form-group">
                    <input id="oldPassword" type="password" class="form-control"
                           placeholder="введите текущий пароль"></div>
                <div class="form-group">
                    <input id="newPassword" type="password" class="form-control"
                           placeholder="введите новый пароль"></div>
                <div class="form-group">
                    <input id="newPasswordConfirm" type="password" class="form-control"
                           placeholder="введите новый пароль еще раз">
                </div>
            </div>
            <div class="modal-footer">
                <button id="savePassword" type="submit" class="btn btn-primary">Сохранить
                </button>
            </div>
        </div>
    </div>
</div>
<%--Syncronizing...--%>
<div id="loadingBar" style="<c:if
        test="${!isSync}">display:none; </c:if>position: fixed; width: 200px; z-index: 10; bottom: 0px; right: 20px;">
    <p style="text-align: center">Синхронизация данных...</p>
    <div class="progress">
        <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45"
             aria-valuemin="0"
             aria-valuemax="100" style="width: 100%">
            <%--<span class="sr-only">45% Complete</span>--%>
        </div>
    </div>
</div>

<style type="text/css">
    .green {
        color: green;
    }
</style>
<script>
    function passAlert(id, message, type, time, style) {
        $('#' + id).html('<div class="alert alert-' + type + ' alert-dismissible fade in" role="alert" ' + (style != null ? ' style="' + style + '"' : null) +
                '><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
                message + '</div>');

        if (time != null) {
            $('#' + id).fadeTo(time, 500).slideUp(500, function () {
                $('#' + id).slideUp(500);
            });
        }
    }

    var webSocket = new WebSocket('ws://' + window.location.host + '/cabinet/sync');

    webSocket.onmessage = function (event) {
        if (event.data == 'show') {
            $('#loadingBar').show();
        } else if (event.data == 'hide') {
            $('#loadingBar').hide();
        }
    };

    $('.widget_preview').on('click', function (e) {
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

    $('#editPassword').on('hidden.bs.modal', function () {
        $('#passAlert').hide();
        $('#oldPassword').val('');
        $('#newPassword').val('');
        $('#newPasswordConfirm').val('');
    });

    $('#savePassword').on('click', function (e) {
        if ($('#oldPassword').val() == '' || $('#newPassword').val() == '' || $('#newPasswordConfirm').val() == '') {
            passAlert('passAlert', 'Пароль не может быть пустым', 'danger', 5000);
        } else if ($('#newPasswordConfirm').val() != $('#newPassword').val()) {
            passAlert('passAlert', 'Введенные пароли не совпадают', 'danger', 5000);
        } else {
            $.ajax({
                url: "settings",
                type: "POST",
                data: {
                    "type": "save_password",
                    "oldPassword": $('#oldPassword').val(),
                    "newPassword": $('#newPassword').val()
                }
            }).done(function (data) {
                $('#oldPassword').val('');
                $('#newPassword').val('');
                $('#newPasswordConfirm').val('');
                if (data.success) {
                    $('#editPassword').modal('hide');
                    passAlert('passSuccess', 'Пароль успешно изменен', 'success', 2500, 'position: absolute; right: 0; width: 40%; z-index: 3');
                } else {
                    passAlert('passAlert', data.error, 'danger');
                }
            })
        }
    });
</script>
