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
 * Created by Marina on 09.05.2016.
 */
@WebServlet(
        name = "DashboardServlet",
        urlPatterns = {""}
)
public class DashboardServlet extends HttpServlet {
    private static final int COOKIE_MAX_AGE = 30 * 60;
    private static final String DEFAULT_PERIOD = "week";
    //   private static int companyId;
    private static DbHelper db;

    @Override
    public void init( ) throws ServletException {
        db = new DbHelper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getUser(req);
        if (user != null) {
            try {
                int companyId = db.getCompanyId(user);
                String period = getPeriod(req, resp);

                req.setAttribute("company", db.getCompanyName(companyId));
                req.setAttribute("menu", getMenu());
                req.setAttribute("panels", getPanels(period, companyId));
                req.setAttribute("period", period);
                req.setAttribute("chartData", getChart(companyId, period));
              //  req.setAttribute("chartData", getJson());
                req.getRequestDispatcher("/jsp/dashboard.jsp").forward(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            resp.sendRedirect("login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post");
        setPeriod(req, resp, req.getParameter("period"));
        resp.sendRedirect("/");
    }

    private String getPeriod(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("period")) {
                return c.getValue();
            }
        }
        //no existing cookie
        setPeriod(req, resp, DEFAULT_PERIOD);
        return DEFAULT_PERIOD;
    }

    private void setPeriod(HttpServletRequest req, HttpServletResponse resp, String period) {
        Cookie[] cookies = req.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("period")) {
                c.setValue(period);
                resp.addCookie(c);
                return;
            }
        }

        //no existing cookie
        Cookie periodCookie = new Cookie("period", period);
        periodCookie.setMaxAge(COOKIE_MAX_AGE);
        resp.addCookie(periodCookie);
    }

    private String getUser(HttpServletRequest req) {
        String user = null;
        Cookie[] cookies = req.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("user")) {
                user = c.getValue();
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

    private List<StatisticPanel> getPanels(String period, int companyId) {
        List<StatisticPanel> list = new ArrayList<>();
        list.add(new StatisticPanel(db, StatisticPanel.Type.INCOME, period, companyId));
        list.add(new StatisticPanel(db, StatisticPanel.Type.ORDERS, period, companyId));
        list.add(new StatisticPanel(db, StatisticPanel.Type.CONVERSION, period, companyId));
        list.add(new StatisticPanel(db, StatisticPanel.Type.COVERING, period, companyId));
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

    private String getChart(int companyId, String period) throws SQLException {
        ChartData chartData = db.getChartData(companyId, period, 1);
        Gson json = new Gson();
        System.out.println(json.toJson(chartData));
        return json.toJson(chartData);
    }

}
