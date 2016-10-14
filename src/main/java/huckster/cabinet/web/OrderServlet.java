package huckster.cabinet.web;

import huckster.cabinet.OperationStatus;
import huckster.cabinet.Util;
import huckster.cabinet.model.OrderEntity;
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
public class OrderServlet extends UserServlet implements JsonOutput {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private OrdersDao dao = new OrdersDao();
    private Util util = new Util();

    private void init(HttpServletRequest req, UserData userData) {
        req.setAttribute("startDate", userData.getStartDate().format(FORMATTER));
        req.setAttribute("endDate", userData.getEndDate().format(FORMATTER));
        req.setAttribute("statuses", getOrderStatuses());
    }

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        init(req, userData);

        req.getRequestDispatcher("jsp/orders.jsp").forward(req, resp);
    }

    @Override
    protected void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        if ("ajax".equals(req.getParameter("request"))) {
            writeJson(resp, getOrders(userData));
        } else {
            if (req.getParameter("type") != null && req.getParameter("type").equals("save_order")) {
                OperationStatus status = new OperationStatus(false, "Сохранение невозможно в данный момент. Попробуйте еще раз позднее");
                // Update order
                try {
                    dao.updateOrder(Integer.parseInt(req.getParameter("id")), Integer.parseInt(req.getParameter("status")), req.getParameter("comment"));
                    status = new OperationStatus(true);
                } catch (SQLException e) {
                    Util.logError("Failed to update order №" + req.getParameter("id"), e, userData);
                    status = new OperationStatus(false, "Сохранение невозможно в данный момент. Попробуйте еще раз позднее");
                }
                writeJson(resp, Util.toJson(status));
            } else {
                //Get list of orders from startDate to endDate
                String startDateStr = req.getParameter("startDate");
                LocalDate startDate;
                if (startDateStr == null || startDateStr.isEmpty()) {
                    startDate = LocalDate.of(2000, 1, 1);
                } else {
                    startDate = LocalDate.parse(startDateStr, FORMATTER);
                }
                userData.setStartDate(startDate);

                String endDateStr = req.getParameter("endDate");
                LocalDate endDate;
                if (endDateStr == null || endDateStr.isEmpty()) {
                    endDate = LocalDate.now();
                } else {
                    endDate = LocalDate.parse(endDateStr, FORMATTER);
                }
                userData.setEndDate(endDate);

                init(req, userData);
                req.getRequestDispatcher("jsp/orders.jsp").forward(req, resp);
            }

            //resp.sendRedirect("orders");
        }
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

    private String getOrders(UserData userData) {
        List<OrderEntity> data = new ArrayList<>();
        try {
            data = dao.getOrders(userData.getCompanyId(), Date.valueOf(userData.getStartDate()), Date.valueOf(userData.getEndDate()));
        } catch (SQLException e) {
            //TODO: some message?
            Util.logError("Failed to load orders", e, userData);
        }

        return util.toJsonWithDataWrap(data);
    }
}
