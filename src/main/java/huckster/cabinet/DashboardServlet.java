package huckster.cabinet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Perevalova Marina on 09.05.2016.
 */
@WebServlet(
        name = "DashboardServlet",
        urlPatterns = {""}
)
public class DashboardServlet extends HttpServlet {
    private static UserData userData;
    static long timeStone = System.currentTimeMillis();

    @Override
    public void init() throws ServletException {
        timeStone("start of init");
        timeStone("new DBHelper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        timeStone("start of get");
        String user = getUser(req);
        if (user != null) {
            timeStone("start of get with login");

            try {
                if (userData == null) {
                    userData = new UserData(user);
                } else {
                    userData.refreshData();
                }

                req.setAttribute("company", userData.getCompanyName());
                req.setAttribute("period", userData.getPeriod());
                req.setAttribute("menu", getMenu());
                req.setAttribute("panels", getPanels());
                timeStone("get panels");
                req.setAttribute("charts", getCharts());
                timeStone("before redirect");
                req.getRequestDispatcher("/jsp/dashboard.jsp").forward(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("redirect to login");
            resp.sendRedirect("login");
        }
        timeStone("end of get");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        timeStone("post");
        userData.setPeriod(req.getParameter("period"));
       // setPeriod(req, req.getParameter("period"));
        resp.sendRedirect("/");
    }

    static void timeStone(String message) {
        long newTimeStone = System.currentTimeMillis();
        System.out.println(message + ": " + (newTimeStone - timeStone));
        timeStone = newTimeStone;
    }

    static void destoryUser() {
        System.out.println("destory user!!");
        userData = null;
    }

    private String getUser(HttpServletRequest req) {
        String user = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("user")) {
                    user = c.getValue();
                }
            }
        }

        return user;
    }

    private List<MenuItem> getMenu() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem("Работа с заказами", null, "glyphicon glyphicon-shopping-cart"));
        list.add(new MenuItem("Настройки виджета", null, "glyphicon glyphicon-plus-sign"));
        list.add(new MenuItem("Статистика и аналитика", null, "glyphicon glyphicon-stats"));
        list.add(new MenuItem("Общие настройки", null, "glyphicon glyphicon-cog"));

        return list;
    }

    private List<StatisticPanel> getPanels() throws SQLException {
        List<StatisticPanel> list = new ArrayList<>();
        list.add(new StatisticPanel(userData, StatisticPanel.Type.INCOME));
        list.add(new StatisticPanel(userData, StatisticPanel.Type.ORDERS));
        list.add(new StatisticPanel(userData, StatisticPanel.Type.CONVERSION));
        list.add(new StatisticPanel(userData, StatisticPanel.Type.COVERING));
        return list;
    }

    private List<Chart> getCharts() throws SQLException {
        List<Chart> list = new ArrayList<>();
        list.add(new Chart(userData, Chart.Type.INCOME));
        list.add(new Chart(userData, Chart.Type.ORDERS));
        list.add(new Chart(userData, Chart.Type.CONVERSION));
        return list;
    }
}
