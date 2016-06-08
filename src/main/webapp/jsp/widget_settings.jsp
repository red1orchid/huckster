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

    <script src="../js/jquery-2.2.4.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
    <script src="../DataTables/datatables.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.18.0/jquery.fancytree.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>

    <style type="text/css">
        /* Remove system outline for focused container */
        .ui-fancytree.fancytree-container:focus {
            outline: none;
        }

        .ui-fancytree.fancytree-container {
            border: none;
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
                    <!-- Modal -->
                    <div class="modal fade" id="editRule" tabindex="-1" role="dialog"
                         aria-labelledby="myModalLabel"
                         aria-hidden="true">
                        <div class="modal-dialog">
                            <form method="POST">
                                <div class="modal-content">
                                    <div class="modal-header" align="center">
                                        <button type="button" class="close" data-dismiss="modal"
                                                aria-hidden="true">&times;</button>
                                        <h4 class="modal-title" id="orderTitle">Сегменты пользователей</h4>
                                    </div>
                                    <div class="modal-body">
                                        <h4>Регионы</h4>
                                        <div id="fancyTree" name="fancyTree"></div>
                                        <select class="selectpicker" multiple>
                                        <c:forEach var="channel" items="${channels}">
                                            <option>${channel.value}</option>
                                        </c:forEach>
                                        </select>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">Отмена
                                        </button>
                                        <button id="saveRule" type="submit" class="btn btn-primary">Сохранить</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
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
                                <td><a data-id=${rRow[0]} data-toggle="modal" href="#editRule"><span
                                        class="glyphicon glyphicon-pencil"></span></a></td>
                                <c:forEach var="rCell" items="${rRow}">
                                    <td>${rCell}</td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <%--Step 2--%>
                <div id="step2" class="tab-pane fade">

                </div>
                <%--Step 3--%>
                <div id="step3" class="tab-pane fade"></div>
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

        $('#rulesTbl').DataTable({
            colReorder: true,
            ordering: false,
            paging: false,
            searching: false,
            language: language
        });

        $("#fancyTree").fancytree({
            checkbox: true,
            selectMode: 3,
            source: {
                url: "/datatable",
                cache: false,
                data: {
                    "type": "tree",
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
            },
            // The following options are only required, if we have more than one tree on one page:
//				initId: "treeData",
            cookieId: "fancytree-Cb3",
            idPrefix: "fancytree-Cb3-"
        });
    });

    jQuery('#saveRule').on('click', function () {
        var selection = jQuery.map(
                jQuery('#fancyTree').fancytree('getRootNode').tree.getSelectedNodes(),
                function (node) {
                    return node.key;
                }
        );

        $.ajax({
            type: "POST",
            url: "/widget_settings",
            data: {
                tree: selection.join(":")
            }
        }).done(function (msg) {
            //do other processing
        });
    });

    $('#editRule').on('show.bs.modal', function (e) {
        console.log($(e.relatedTarget).data('id'));

        // Optionally pass new `source`:
        var tree = $("#fancyTree").fancytree("getTree");
        tree.reload({
            url: "/datatable",
            cache: false,
            data: {
                "type": "tree",
                "id": $(e.relatedTarget).data('id')
            }
        }).done();
    });

</script>
</html>
