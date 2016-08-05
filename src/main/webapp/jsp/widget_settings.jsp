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

        .modal-dialog {
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
                    <div class="modal fade" id="editRule" tabindex="-1" role="dialog"
                         aria-labelledby="myModalLabel"
                         aria-hidden="true">
                        <div class="modal-dialog modal-lg">
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
                    <select id="vendors" class="selectpicker form-control">
                        <c:forEach var="vendor" items="${vendors}">
                            <option value="${vendor.key}">${vendor.value}</option>
                        </c:forEach>
                    </select>
                    <br><br>
                    <a data-toggle="modal" href="#editRule" type="button" class="btn btn-success">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Добавить скидку
                    </a>
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
        $(".button-checkbox").labelauty({
            class: "labelauty",
            label: true,
            separator: "|",
            same_width: true
        });

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

    jQuery('#saveRule').on('click', function () {
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
</script>
</html>
