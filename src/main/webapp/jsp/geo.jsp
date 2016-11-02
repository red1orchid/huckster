<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 01.11.2016
  Time: 15:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="./Labelauty/jquery-labelauty.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.18.0/skin-win8-n/ui.fancytree.css"
          rel="stylesheet">
    <link href="./css/titatoggle-dist.css" rel="stylesheet">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.18.0/jquery.fancytree.js"></script>
    <script src="./Labelauty/jquery-labelauty.js"></script>

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
</head>
<body>
<div id="geo" class="tab-pane fade">
    <div class="row">
        <br>
        <div id="geoAlert"></div>
        <div class="col-sm-5 form-group">
            <br>
            <label>Регионы</label>
            <div id="fancyTree" name="fancyTree"></div>
        </div>
        <div class="col-sm-6">
            <div class="checkbox checkbox-slider--b-flat checkbox-slider-info pull-right">
                <label>
                    <input id="isAutoMode" type="checkbox"
                           <c:if test="${!isAutoMode}">checked="checked"</c:if>><span>Расширенный режим</span>
                </label></div>
            <br>
            <c:if test="${!isAutoMode}">
                <input type="hidden" id="selectedChannels" value="${rule.channels}">
                <div class="form-group">
                    <label for="chnl">Каналы</label>
                    <select id="chnl" class="selectpicker form-control"
                            multiple title="Все каналы"
                            data-live-search="true">
                    </select></div>
                <div class="form-group">
                    <label for="devices">Устройства</label>
                    <select id="devices" class="selectpicker form-control">
                        <c:forEach var="device" items="${devices}">
                            <option value="${device.key}"
                                    <c:if test="${rule.devices == device.key}">selected</c:if>>${device.value}</option>
                        </c:forEach>
                    </select></div>
                <div class="form-group">
                    <label for="days">Дни</label>
                    <div id="days" class="form-inline">
                        <input id="day0" class="button-checkbox" type="checkbox"
                               data-labelauty="ПН|ПН" <c:if test="${rule.daysArray[0]}">checked</c:if>/>
                        <input id="day1" class="button-checkbox" type="checkbox"
                               data-labelauty="ВТ|ВТ" <c:if test="${rule.daysArray[1]}">checked</c:if>/>
                        <input id="day2" class="button-checkbox" type="checkbox"
                               data-labelauty="СР|СР" <c:if test="${rule.daysArray[2]}">checked</c:if>/>
                        <input id="day3" class="button-checkbox" type="checkbox"
                               data-labelauty="ЧТ|ЧТ" <c:if test="${rule.daysArray[3]}">checked</c:if>/>
                        <input id="day4" class="button-checkbox" type="checkbox"
                               data-labelauty="ПТ|ПТ" <c:if test="${rule.daysArray[4]}">checked</c:if>/>
                        <input id="day5" class="button-checkbox" type="checkbox"
                               data-labelauty="СБ|СБ" <c:if test="${rule.daysArray[5]}">checked</c:if>/>
                        <input id="day6" class="button-checkbox" type="checkbox"
                               data-labelauty="ВС|ВС" <c:if test="${rule.daysArray[6]}">checked</c:if>/>
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
                                    <option value="${loop.index}"
                                            <c:if test="${rule.timeFrom == loop.index}">selected</c:if>>${loop.index < 10 ? '0'.concat(loop.index) : loop.index}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="hourTo">По</label>
                            <select id="hourTo" class="selectpicker"
                                    data-live-search="true"
                                    data-width="auto">
                                <c:forEach begin="0" end="24" varStatus="loop">
                                    <option value="${loop.index}"
                                            <c:if test="${rule.timeTo == loop.index}">selected</c:if>>${loop.index < 10 ? '0'.concat(loop.index) : loop.index}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
    <br>
    <button type="button" class="btn btn-default cancel">Отмена
    </button>
    <button id="saveRule" type="submit" class="btn btn-primary">Сохранить
    </button>
</div>
</body>

<script type="text/javascript">
    //tree
    $("#fancyTree").fancytree({
        checkbox: true,
        selectMode: 3,
        source: {
            cache: false,
            url: "geo",
            type: "GET",
            data: {
                type: "tree"
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

    //auto mode selector
    $('.toggle').bootstrapSwitch();

    if ($("#isAuto").val()) {
        //button select
        $(".button-checkbox").labelauty({
            class: "labelauty",
            label: true,
            separator: "|",
            same_width: true
        });

        //load channels
        $.ajax({
            url: "geo",
            type: "GET",
            data: {
                type: "channels"
            },
            success: function (data) {
                $('#chnl').empty();
                data.forEach(function (item, i, arr) {
                    $('#chnl').append('<option>' + item + '</option>');
                });
                $('#chnl').selectpicker('refresh');

                //set selected values
                if ($("#selectedChannels").val() != null) {
                    $('#chnl').selectpicker('val', $("#selectedChannels").val().split(":"));
                }
            }
        });
    }

    //Change auto mode
    $('#isAutoMode').change(function () {
        $.ajax({
            type: "POST",
            url: "settings",
            data: {
                type: "auto_mode",
                mode: !this.checked
            }
        }).done(function (msg) {
            location.reload();
        });
    });

    //Save changes
    $('#saveRule').on('click', function () {
        var selection = jQuery.map(
                jQuery('#fancyTree').fancytree('getRootNode').tree.getSelectedNodes(),
                function (node) {
                    return node.key;
                }
        );

        var days = [];
        for (var i = 0; i <= 6; i++) {
            if ($('#day' + i).is(":checked")) {
                days.push(i + 1);
            }
        }

        $.ajax({
            type: "POST",
            url: "geo",
            data: {
                tree: selection.join(":"),
                channels: ($('#chnl').selectpicker('val') || []).join(":"),
                devices: $('#devices').selectpicker('val'),
                days: days.join(":"),
                hourFrom: $('#hourFrom').selectpicker('val'),
                hourTo: $('#hourTo').selectpicker('val')
            }
        }).done(function (data) {
            if (!data.success) {
                alert('geoAlert', data.error, 'danger', 5000);
            } else {
                location.reload();
            }
        });
    });
</script>
</html>
