package huckster.cabinet.web;

import huckster.cabinet.Util;
import huckster.cabinet.repository.StatisticDao;
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
import java.util.TreeMap;

/**
 * Created by PerevalovaMA on 23.05.2016.
 */
@WebServlet("/statistic")
public class StatisticServlet extends UserServlet {
    private StatisticDao dao = new StatisticDao();

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        System.out.println(userData.getPeriodGoods());
        System.out.println(userData.getPeriodTraffic());
        req.setAttribute("periodGoods", userData.getPeriodGoods());
        req.setAttribute("periodTraffic", userData.getPeriodTraffic());
        req.setAttribute("traffic", getTraffic(userData));
        req.setAttribute("yml", getYml(userData));
        req.getRequestDispatcher("/jsp/statistic.jsp").forward(req, resp);
    }

    @Override
    protected void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        if (req.getParameter("periodGoods") != null) {
            userData.setPeriodGoods(req.getParameter("periodGoods"));
        }
        if (req.getParameter("periodTraffic") != null) {
            userData.setPeriodTraffic(req.getParameter("periodTraffic"));
        }
        resp.sendRedirect("/statistic");
    }

    private List<List> getTraffic(UserData userData) {
        try {
            return dao.getTraffic(userData.getCompanyId(), userData.getPeriodTraffic());
        } catch (SQLException e) {
            //TODO: some message?
            Util.logError("Failed to load traffic", userData);
            return new ArrayList<>();
        }
    }

    public Map<String, String> getYml(UserData userData) {
        try {
            return dao.getYml(userData.getCompanyId());
        } catch (SQLException e) {
            //TODO: some message?
            Util.logError("Failed to load yml", userData);
            return new TreeMap<>();
        }
    }
}