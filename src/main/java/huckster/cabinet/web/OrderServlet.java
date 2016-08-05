package huckster.cabinet.web;

import huckster.cabinet.Util;
import huckster.cabinet.repository.OrdersDao;
import huckster.cabinet.repository.UserData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
@WebServlet("/orders")
public class OrderServlet extends UserServlet {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private OrdersDao dao = new OrdersDao();

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        req.setAttribute("startDate", userData.getStartDate().format(FORMATTER));
        req.setAttribute("endDate", userData.getEndDate().format(FORMATTER));
        req.setAttribute("statuses", getOrderStatuses());

        Util.timeStone("orders start forward");
        req.getRequestDispatcher("/jsp/orders.jsp").forward(req, resp);
    }

    @Override
    protected void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (req.getParameter("orderId") == null) {
            //Get list of orders from startDate to endDate
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

            userData.setStartDate(startDate);
            userData.setEndDate(endDate);
        } else {
            // Update order
            try {
                dao.updateOrder(Integer.parseInt(req.getParameter("orderId")), Integer.parseInt(req.getParameter("status")), req.getParameter("comment"));
            } catch (SQLException e) {
                Util.logError("Failed to update order №" + req.getParameter("orderId"), e, userData);
                req.setAttribute("error", "Сохранение невозможно в данный момент. Попробуйте еще раз позднее");
                //TODO: something!  initDataGet(req, resp);
            }
            //   System.out.println(req.getParameter("comment"));
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
