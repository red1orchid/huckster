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
            <c:if test="${isWidgetActive}">
                <li><a data-placement="bottom" title="Предпросмотр виджета"><span class="glyphicon glyphicon-play green widget_preview"></span></a></li>
            </c:if>
            <li>
                <a href="#" data-toggle="dropdown"><span class="glyphicon glyphicon-user"></span><span
                        class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="#editPassword" data-toggle="modal"><span class="glyphicon glyphicon-wrench"></span>Изменить пароль</a></li>
                </ul>
            </li>
            <li><a href="logout"><span class="glyphicon glyphicon-log-out"></span> Выход</a></li>
        </ul>
    </div>
</nav>
<%--Modal--%>
<div class="modal fade" id="editPassword">
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

<style type="text/css">
    .green {
        color: green;
    }
</style>
<script>
    bootstrap_alert = function () {
    };
    bootstrap_alert.error = function (message) {
        $('#passAlert').html('<div class="alert alert-danger alert-dismissible fade in" role="alert">' +
                '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                '<span aria-hidden="true">&times;</span></button>' + message + '</div>').show();
    };
    bootstrap_alert.success = function (message, timeOut) {
        $('#passAlert').html('<div class="alert alert-success alert-dismissible fade in" role="alert">' +
                '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                '<span aria-hidden="true">&times;</span></button>' + message + '</div>').fadeTo(timeOut, 500).slideUp(500, function () {
            $("#passAlert").slideUp(500);
        }).show();
    };

    $('.widget_preview').on('click', function (e) {
        console.log('PRR');
        $.ajax({
            url: "settings_data",
            type: "GET",
            async: false,
            data: {
                type: "widget_url"
            },
            success: function(data) {
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
            bootstrap_alert.error('Пароль не может быть пустым');
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
                        bootstrap_alert.error('Введен неверный пароль');
                    } else {
                        bootstrap_alert.error('В данный момент сохранение невозможно');
                    }
                }
                $('#oldPassword').val('');
                $('#newPassword').val('');
                $('#newPasswordConfirm').val('');
            })
        }
    });
</script>
