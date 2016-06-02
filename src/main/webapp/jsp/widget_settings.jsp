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
    <link rel="stylesheet"
          href="https://cdn.datatables.net/u/bs/dt-1.10.12,cr-1.3.2,b-1.2.0/datatables.min.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.18.0/skin-win8-n/ui.fancytree.css"
          rel="stylesheet">


    <script src="../js/jquery-2.2.4.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
    <%--
        <script src="https://cdn.datatables.net/u/bs/dt-1.10.12,cr-1.3.2,b-1.2.0/datatables.min.js"></script>--%>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.18.0/jquery.fancytree.js"></script>

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
                </div>
                <%--Step 2--%>
                <div id="step2" class="tab-pane fade">
                        <div class="col-sm-6">
                            <h4>География</h4>
                            <div id="fancyTree" name="fancyTree"></div>
                            <button id="showSelection">click</button>
                        </div>
                </div>
                <%--Step 3--%>
                <div id="step3" class="tab-pane fade"></div>
            </div>
        </div>
    </div>
</div>
</body>

<script type="text/javascript">
    /*    var treeData = [
     {title: "item1 with key and tooltip", tooltip: "Look, a tool tip!"},
     {title: "item2: selected on init", selected: true},
     {
     title: "Folder", key: "id3",
     children: [
     {
     title: "Sub-item 3.1",
     children: [
     {title: "Sub-item 3.1.1", key: "id3.1.1"},
     {title: "Sub-item 3.1.2", key: "id3.1.2"}
     ]
     },
     {
     title: "Sub-item 3.2",
     children: [
     {title: "Sub-item 3.2.1", key: "id3.2.1"},
     {title: "Sub-item 3.2.2", key: "id3.2.2"}
     ]
     }
     ]
     },
     {
     title: "Document with some children (expanded on init)", key: "id4", expanded: true,
     children: [
     {
     title: "Sub-item 4.1 (active on init)", active: true,
     children: [
     {title: "Sub-item 4.1.1", key: "id4.1.1"},
     {title: "Sub-item 4.1.2", key: "id4.1.2"}
     ]
     },
     {
     title: "Sub-item 4.2 (selected on init)", selected: true,
     children: [
     {title: "Sub-item 4.2.1", key: "id4.2.1"},
     {title: "Sub-item 4.2.2", key: "id4.2.2"}
     ]
     },
     {title: "Sub-item 4.3 (hideCheckbox)", hideCheckbox: true},
     {title: "Sub-item 4.4 (unselectable)", unselectable: true}
     ]
     },
     {title: "Lazy folder", folder: true, lazy: true}
     ];*/

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

        $("#fancyTree").fancytree({
            checkbox: true,
            selectMode: 3,
            source: {
                url: "/datatable",
                cache: false,
                data: {
                    "type": "tree"
                }
            },
            icons: false,
            /*            lazyLoad: function(event, ctx) {
             ctx.result = {url: "ajax-sub2.json", debugDelay: 1000};
             },
             loadChildren: function(event, ctx) {
             ctx.node.fixSelection3AfterClick();
             },*/
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

    $('#tree').on("changed.jstree", function (e, data) {
        console.log(data.selected);
    });

    jQuery('#showSelection').on('click', function () {
        var selection = jQuery.map(
                jQuery('#fancyTree').fancytree('getRootNode').tree.getSelectedNodes(),
                function (node) {
                    return node.key;
                }
        );

        $.ajax({
            type: "POST",
            url: "/widget_settings",
            data: { tree: selection.join(":"),
                    test: "test"}
        }).done(function( msg ) {
            //do other processing
        });

        // selection will have an array of the keys of all selected nodes
        console.log(selection);
    });

</script>
</html>
