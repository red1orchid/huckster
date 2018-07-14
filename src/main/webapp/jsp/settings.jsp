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
    <link href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css" rel="stylesheet">
    <link href="./DataTables/datatables.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css"
          rel="stylesheet">

    <script src="./js/jquery-2.2.4.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
    <script src="./js/bootstrap.min.js"></script>
    <script src="./DataTables/datatables.min.js"></script>
    <script src="./js/bootstrap-select.min.js"></script>

    <style type="text/css">
        .password {
            padding-left: 1px;
        }
    </style>

    <script>
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
    </script>
</head>
<body>
<%--Top bar--%>
<%@ include file="navbar.jsp" %>

<div class="container-fluid">
    <%--Sidebar--%>
    <%@ include file="menu.jsp" %>
    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <ul class="nav nav-tabs" id="tabs">
            <li><a data-toggle="tab" href="#settings">Общие настройки</a></li>
            <li><a data-toggle="tab" href="#geo">География</a></li>
            <li><a data-toggle="tab" href="#step2">Скидки для категорий и вендоров</a></li>
            <li><a data-toggle="tab" href="#step3">Скидки на отдельные товары</a></li>
            <li><a data-toggle="tab" href="#pages">Страницы-исключения</a></li>
            <li><a data-toggle="tab" href="#widget">Оформление виджета</a></li>
            <c:if test="${!isScriptInstalled}">
                <li><a data-toggle="tab" href="#script">Ваш код установки</a></li>
            </c:if>
        </ul>
        <input type="hidden" id="isAuto" value="${isAutoMode}">
        <div class="tab-content">
            <%--General settings--%>
            <%@ include file="general_settings.jsp" %>
            <%--Geo--%>
            <%@ include file="geo.jsp" %>
            <%--Categories and vendors discounts--%>
            <%@ include file="vendor_discounts.jsp" %>
            <%--Items discounts--%>
            <%@ include file="item_discounts.jsp" %>
            <%--Blocked pages--%>
            <div id="pages" class="tab-pane fade">
                <br>
                <div id="pagesAlert"></div>
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
                                <div id="pagesForm" role="form">
                                    <div class="form-group">
                                        <label for="url">Адрес</label>
                                        <input id="url" type="text" class="form-control" value="${url.url}" required>
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
                                <button id="deletePage" type="submit" class="btn btn-default">Удалить
                                </button>
                                <button id="savePage" type="submit" class="btn btn-primary">Сохранить
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
                        })(window,document,'files.hucksterbot.com',{companyId:${companyId}});
                        &lt;/script&gt;
                    </code></p></div>
            </div>
            <%--Widget--%>
            <%@ include file="widget.jsp" %>
        </div>
    </div>
</div>
</body>

<script type="text/javascript">
    function alert(id, message, type, time) {
        $('#' + id).html('<div class="alert alert-' + type + ' alert-dismissible fade in" role="alert"> ' +
                ' <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
                message + '</div>');

        if (time != null) {
            $('#' + id).fadeTo(time, 500).slideUp(500, function () {
                $('#' + id).slideUp(500);
            });
        }
    }

    $(document).ready(function () {
        //select last tab or first tab by default
        $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
            localStorage.setItem('sActiveTab', $(e.target).attr('href'));
        });
        var activeTab = localStorage.getItem('sActiveTab');
        if (activeTab && $('a[href="' + activeTab + '"]').val() != undefined) {
            $('a[href="' + activeTab + '"]').tab('show');
        } else {
            $('.nav-tabs a:first').tab('show');
        }
    });

    $('.cancel').on('click', function () {
        location.reload();
    });

    //Pages
    $('#editPage').on('show.bs.modal', function (e) {
        id = $(e.relatedTarget).data('id');
        $('#url').val($(e.relatedTarget).data('url'));
        $('#isTrash').bootstrapSwitch('state', Boolean($(e.relatedTarget).data('trash')));
    });

    $('#savePage').on('click', function (e) {
        $.ajax({
            url: "blocked_pages",
            type: "POST",
            data: {
                type: "save",
                id: id,
                url: $('#url').val(),
                isTrash: $('#isTrash').bootstrapSwitch('state') ? 1 : 0
            }
        }).done(function (data) {
            if (!data.success) {
                $('#editPage').modal('hide');
                alert('pagesAlert', data.error, 'danger', 5000);
            } else {
                location.reload();
            }
        });
    });

    $('#deletePage').on('click', function (e) {
        $.ajax({
            url: "blocked_pages",
            type: "POST",
            data: {
                type: "delete",
                id: id
            }
        }).done(function (data) {
            if (!data.success) {
                $('#editPage').modal('hide');
                alert('pagesAlert', data.error, 'danger', 5000);
            } else {
                location.reload();
            }
        });
    });
</script>
</html>
