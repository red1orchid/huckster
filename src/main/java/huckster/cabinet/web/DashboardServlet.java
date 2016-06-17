package huckster.cabinet.web;

import huckster.cabinet.Chart;
import huckster.cabinet.StatisticPanel;
import huckster.cabinet.repository.UserData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Perevalova Marina on 09.05.2016.
 */

@WebServlet("")
public class DashboardServlet extends UserServlet {
    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        userData.refreshData();
        req.setAttribute("period", userData.getPeriod());
        req.setAttribute("panels", getPanels(userData));
        req.setAttribute("charts", getCharts(userData));
        req.getRequestDispatcher("/jsp/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserData userData = (UserData) req.getSession().getAttribute("userData");
        userData.setPeriod(req.getParameter("period"));
        resp.sendRedirect("/");
    }

    private List<StatisticPanel> getPanels(UserData userData) throws SQLException {
        List<StatisticPanel> list = new ArrayList<>();
        list.add(new StatisticPanel(userData, StatisticPanel.Type.INCOME));
        list.add(new StatisticPanel(userData, StatisticPanel.Type.ORDERS));
        list.add(new StatisticPanel(userData, StatisticPanel.Type.CONVERSION));
        list.add(new StatisticPanel(userData, StatisticPanel.Type.COVERING));
        return list;
    }

    private List<Chart> getCharts(UserData userData) throws SQLException {
        List<Chart> list = new ArrayList<>();
        list.add(new Chart(userData, Chart.Type.INCOME));
        list.add(new Chart(userData, Chart.Type.ORDERS));
        list.add(new Chart(userData, Chart.Type.CONVERSION));
        return list;
    }
}
