<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 24.05.2016
  Time: 17:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="btn-group">
    <button type="submit" name="period" value="day"
            class="btn btn-primary <c:if test="${period == 'day'}">active</c:if>">Текущий день
    </button>
    <button type="submit" name="period" value="week"
            class="btn btn-primary <c:if test="${period == 'week'}">active</c:if>">Текущую неделю
    </button>
    <button type="submit" name="period" value="month"
            class="btn btn-primary <c:if test="${period == 'month'}">active</c:if>">Текущий месяц
    </button>
</div>