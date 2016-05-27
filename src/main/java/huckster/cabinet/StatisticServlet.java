package huckster.cabinet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

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
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        initPeriods(req);
        req.setAttribute("periodGoods", req.getSession().getAttribute("periodGoods"));
        req.setAttribute("periodTraffic", req.getSession().getAttribute("periodTraffic"));
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
}