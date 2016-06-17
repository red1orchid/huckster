package huckster.cabinet.web;

import huckster.cabinet.repository.DbDao;
import huckster.cabinet.StaticElements;
import huckster.cabinet.repository.UserData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

import static huckster.cabinet.StaticElements.*;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
@WebServlet("/orders")
public class OrderServlet extends UserServlet {
    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        LocalDate startDate = (LocalDate) req.getSession().getAttribute("startDate");
        LocalDate endDate = (LocalDate) req.getSession().getAttribute("endDate");
        if (startDate == null) {
            startDate = DEFAULT_START_DATE;
        }
        if (endDate == null) {
            endDate = DEFAULT_END_DATE;
        }

        req.setAttribute("startDate", startDate.format(FORMATTER));
        req.setAttribute("endDate", endDate.format(FORMATTER));
        req.setAttribute("statuses", getOrderStatuses());

        StaticElements.timeStone("orders start forward");
        req.getRequestDispatcher("/jsp/orders.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (req.getParameter("orderId") != null) {
            DbDao dao = new DbDao();
            try {
                dao.updateOrder(Integer.parseInt(req.getParameter("orderId")), Integer.parseInt(req.getParameter("status")), req.getParameter("comment"));
            } catch (SQLException e) {
                e.printStackTrace();
                req.setAttribute("error", "Сохранение невозможно в данный момент. Попробуйте еще раз позднее");
                //TODO: something!  initDataGet(req, resp);
            }
            //   System.out.println(req.getParameter("comment"));
        } else {
            String startDateStr = req.getParameter("startDate");
            String endDateStr = req.getParameter("endDate");
            LocalDate startDate;
            LocalDate endDate;

            if (req.getParameter("startDate") == null || req.getParameter("startDate").isEmpty()) {
                startDate = LocalDate.of(2000, 1, 1);
            } else {
                startDate = LocalDate.parse(startDateStr, FORMATTER);
            }

            if (req.getParameter("endDate") == null || req.getParameter("endDate").isEmpty()) {
                endDate = LocalDate.now();
            } else {
                endDate = LocalDate.parse(endDateStr, FORMATTER);
            }

            req.getSession().setAttribute("startDate", startDate);
            req.getSession().setAttribute("endDate", endDate);
        }
        resp.sendRedirect("/orders");
    }

    private HashMap<Integer, String> getOrderStatuses() {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(0, "принят");
        map.put(1, "в работе");
        map.put(2, "обработан");
        map.put(3, "выкуплен");
        map.put(4, "отложен");
        map.put(5, "отменен");

        return map;
    }
}
