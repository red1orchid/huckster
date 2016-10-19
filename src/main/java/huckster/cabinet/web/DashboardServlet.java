package huckster.cabinet.web;

import huckster.cabinet.Chart;
import huckster.cabinet.StatisticPanel;
import huckster.cabinet.Util;
import huckster.cabinet.model.ChartData;
import huckster.cabinet.model.TwoLineChartEntity;
import huckster.cabinet.repository.DashbordDao;
import huckster.cabinet.repository.UserData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Perevalova Marina on 09.05.2016.
 */

@WebServlet("")
public class DashboardServlet extends UserServlet {
    private DashbordDao dao = new DashbordDao();

    public DashboardServlet() throws SQLException {
    }

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        userData.clear();
        req.setAttribute("period", userData.getPeriod());
        req.setAttribute("panels", getPanels(userData));
        req.setAttribute("charts", getCharts(userData));
        req.getRequestDispatcher("jsp/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        userData.setPeriod(req.getParameter("period"));
        resp.sendRedirect(".");
    }

    private List<StatisticPanel> getPanels(UserData userData) throws SQLException {
        List<StatisticPanel> list = new ArrayList<>();
        list.add(new StatisticPanel(userData, StatisticPanel.Type.INCOME, getRate(userData, StatisticPanel.Type.INCOME), getPercent(userData, StatisticPanel.Type.INCOME)));
        list.add(new StatisticPanel(userData, StatisticPanel.Type.ORDERS, getRate(userData, StatisticPanel.Type.ORDERS), getPercent(userData, StatisticPanel.Type.ORDERS)));
        list.add(new StatisticPanel(userData, StatisticPanel.Type.CONVERSION, getRate(userData, StatisticPanel.Type.CONVERSION), getPercent(userData, StatisticPanel.Type.CONVERSION)));
        list.add(new StatisticPanel(userData, StatisticPanel.Type.COVERING, getRate(userData, StatisticPanel.Type.COVERING), getPercent(userData, StatisticPanel.Type.COVERING)));
        return list;
    }

    private List<Chart> getCharts(UserData userData) throws SQLException {
        List<Chart> list = new ArrayList<>();
        list.add(new Chart(userData, Chart.Type.INCOME, getChartData(userData, Chart.Type.INCOME)));
        list.add(new Chart(userData, Chart.Type.ORDERS, getChartData(userData, Chart.Type.ORDERS)));
        list.add(new Chart(userData, Chart.Type.CONVERSION, getChartData(userData, Chart.Type.CONVERSION)));
        return list;
    }

    private String getRate(UserData userData, StatisticPanel.Type panelType) {
        initRateContainer(userData);
        return userData.getRateContainer().getOrDefault(panelType.getReportId(), "0");
    }

    private String getPercent(UserData userData, StatisticPanel.Type panelType) {
        initPercentsContainer(userData);
        return userData.getPercentContainer().getOrDefault(panelType.getReportId(), "0.0");
    }

    public ChartData getChartData(UserData userData, Chart.Type type) throws SQLException {
        initChartContainer(userData);
        return userData.getChartContainer().get(type.getReportId());
    }

    private void initRateContainer(UserData userData) {
        if (userData.getRateContainer().isEmpty()) {
            try {
                Map<Integer, String> container = dao.getStatisticRates(userData.getCompanyId(), userData.getPeriod());
                if (!container.isEmpty()) {
                    userData.setRateContainer(container);
                } else {
                    Util.logError("No rates found", userData);
                }
            } catch (SQLException e) {
                Util.logError("Failed to load rates", e, userData);
            }
        }
    }

    private void initPercentsContainer(UserData userData) {
        if (userData.getPercentContainer().isEmpty()) {
            try {
                Map<Integer, String> container = dao.getStatisticPercents(userData.getCompanyId(), userData.getPeriod());
                if (!container.isEmpty()) {
                    userData.setPercentContainer(container);
                } else {
                    Util.logError("No percents found", userData);
                }
            } catch (SQLException e) {
                Util.logError("Failed to load percents", e, userData);
            }
        }
    }

    private void initChartContainer(UserData userData) {
        if (userData.getChartContainer().isEmpty()) {
            try {
                List<TwoLineChartEntity> chartRawData = dao.getChartData(userData.getCompanyId(), userData.getPeriod());
                Map<Integer, ChartData> container = ChartData.makeData(chartRawData, ".current", ".last");
                container.values().stream()
                        .forEach(v -> v.setProperties("time", "linear", 0));

                if (!container.isEmpty()) {
                    userData.setChartContainer(container);
                } else {
                    Util.logError("No graphics data found", userData);
                }
            } catch (SQLException e) {
                Util.logError("Failed to load  graphics data", e, userData);
            }
        }
    }
}
