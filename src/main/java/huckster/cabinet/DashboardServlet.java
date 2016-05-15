package huckster.cabinet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    private static final int COOKIE_MAX_AGE = 60 * 60;
    private static final String DEFAULT_PERIOD = "week";
    //   private static int companyId;
    private static DbHelper db;
    static long timeStone = System.currentTimeMillis();

    @Override
    public void init() throws ServletException {
        timeStone("start of init");
        db = new DbHelper();
        timeStone("new DBHelper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        timeStone("start of get");
        String user = getUser(req);
        if (user != null) {
            timeStone("start of get with login");
            try {
                int companyId = getCompanyId(req, user);
                String period = getPeriod(req);
                db.refreshData();

                req.setAttribute("company", getCompanyName(req, companyId));
                req.setAttribute("menu", getMenu());
                req.setAttribute("panels", getPanels(companyId, period));
                timeStone("get panels");
                req.setAttribute("period", period);
                req.setAttribute("charts", getCharts(companyId, period));
              //  req.setAttribute("chartData", getJson());
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
        setPeriod(req, req.getParameter("period"));
        resp.sendRedirect("/");
    }

    static void timeStone(String message) {
        long newTimeStone = System.currentTimeMillis();
        System.out.println(message + ": " + (newTimeStone - timeStone));
        timeStone = newTimeStone;
    }

    private Integer getCompanyId(HttpServletRequest req, String user) throws SQLException {
        Integer companyId = (Integer) req.getSession().getAttribute("companyId");
        if (companyId == null) {
            companyId = db.getCompanyId(user);
            req.getSession().setAttribute("companyId", companyId);
        }
        return companyId;
    }

    private String getCompanyName(HttpServletRequest req, Integer companyId) throws SQLException {
        String companyName = (String) req.getSession().getAttribute("companyName");
        if (companyName == null) {
            companyName = db.getCompanyName(companyId);
            req.getSession().setAttribute("companyName", companyName);
        }
        return companyName;
    }

    private String getPeriod(HttpServletRequest req) {
        String period = (String) req.getSession().getAttribute("period");
        if (period == null) {
            period = DEFAULT_PERIOD;
            req.getSession().setAttribute("period", period);
        }
        return period;
    }

    private void setPeriod(HttpServletRequest req, String period) {
        System.out.println("period " + period);
        req.getSession().setAttribute("period", period);
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

    private List<StatisticPanel> getPanels(int companyId, String period) {
        List<StatisticPanel> list = new ArrayList<>();
        list.add(new StatisticPanel(db, StatisticPanel.Type.INCOME, companyId, period));
        list.add(new StatisticPanel(db, StatisticPanel.Type.ORDERS, companyId, period));
        list.add(new StatisticPanel(db, StatisticPanel.Type.CONVERSION, companyId, period));
        list.add(new StatisticPanel(db, StatisticPanel.Type.COVERING, companyId, period));
        return list;
    }

    private String getJson() {
/*        String json = "{\n" +
                "  \"xScale\": \"time\",\n" +
                "  \"yScale\": \"linear\",\n" +
                "  \"main\": [\n" +
                "    {\n" +
                "      \"className\": \".stat\",\n" +
                "      \"data\": [\n" +
                "        {\n" +
                "          \"x\": \"2012-11-05\",\n" +
                "          \"y\": 6\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-06\",\n" +
                "          \"y\": 6\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-07\",\n" +
                "          \"y\": 8\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-08\",\n" +
                "          \"y\": 3\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-09\",\n" +
                "          \"y\": 4\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-10\",\n" +
                "          \"y\": 9\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-11\",\n" +
                "          \"y\": 6\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";*/
        String json = "{\n" +
                "  \"xScale\": \"time\",\n" +
                "  \"yScale\": \"linear\",\n" +
                "  \"main\": [\n" +
                "    {\n" +
                "      \"className\": \".pizza\",\n" +
                "      \"data\": [\n" +
                "        {\n" +
                "          \"x\": \"2012-11-05 23:59\",\n" +
                "          \"y\": 12\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-06 23:59\",\n" +
                "          \"y\": 8\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"className\": \".tacos\",\n" +
                "      \"data\": [\n" +
                "        {\n" +
                "          \"x\": \"2012-11-05 23:59\",\n" +
                "          \"y\": 8\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-06 23:59\",\n" +
                "          \"y\": 11\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return json;
    }

    private List<Chart> getCharts(int companyId, String period) {
        List<Chart> list = new ArrayList<>();
        list.add(new Chart(db, Chart.Type.INCOME, companyId, period));
        list.add(new Chart(db, Chart.Type.ORDERS, companyId, period));
        list.add(new Chart(db, Chart.Type.CONVERSION, companyId, period));
        return list;
    }
}
