<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 17.05.2016
  Time: 13:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-sm-3 col-md-2 sidebar">
    <ul class="nav nav-sidebar">
        <li><a href="/orders"><span class="glyphicon glyphicon-shopping-cart" aria-hidden="true"></span> Работа с заказами</a></li>
        <li><a href="/widget_settings"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> Настройки виджета</a></li>
        <li><a href="/statistic"><span class="glyphicon glyphicon-stats" aria-hidden="true"></span> Статистика и аналитика</a></li>
        <li><a href="/settings"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Общие настройки</a></li>
        <%--        <c:forEach var="menuItem" items="${menu}">
                    <li><a href="${menuItem.getLink()}"><span class="${menuItem.getIcon()}"
                                                              aria-hidden="true"></span> ${menuItem.getName()}</a></li>
                </c:forEach>--%>
    </ul>
</div>