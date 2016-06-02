package huckster.cabinet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static huckster.cabinet.StaticElements.DEFAULT_END_DATE;
import static huckster.cabinet.StaticElements.DEFAULT_START_DATE;
import static huckster.cabinet.StaticElements.FORMATTER;

/**
 * Created by PerevalovaMA on 23.05.2016.
 */
@WebServlet("/statistic")
public class StatisticServlet extends UserServlet {
    private static final String DEFAULT_PERIOD = "month";

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        initPeriods(req);
        req.setAttribute("periodGoods", req.getSession().getAttribute("periodGoods"));
        req.setAttribute("periodTraffic", req.getSession().getAttribute("periodTraffic"));
        req.setAttribute("traffic", getTraffic(req));
        req.setAttribute("yml", getYml(req));
        req.getRequestDispatcher("/jsp/statistic.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
/*        UserData userData = (UserData) req.getSession().getAttribute("userData");
        userData.setPeriod(req.getParameter("period"));*/
        req.getSession().setAttribute("periodGoods", req.getParameter("periodGoods"));
        req.getSession().setAttribute("periodTraffic", req.getParameter("periodTraffic"));
        resp.sendRedirect("/statistic");
    }

    private void initPeriods(HttpServletRequest req) {
        if (req.getSession().getAttribute("periodGoods") == null) {
            req.getSession().setAttribute("periodGoods", DEFAULT_PERIOD);
        }

        if (req.getSession().getAttribute("periodTraffic") == null) {
            req.getSession().setAttribute("periodTraffic", DEFAULT_PERIOD);
        }
    }

    private Map<String, String> getYml(HttpServletRequest req) throws SQLException {
        UserData userData = (UserData) req.getSession().getAttribute("userData");
        return userData.getYml();
    }

    private List<List> getTraffic(HttpServletRequest req) throws SQLException {
        UserData userData = (UserData) req.getSession().getAttribute("userData");
        return userData.getTraffic((String) req.getSession().getAttribute("periodTraffic"));
    }
}