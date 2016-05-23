package huckster.cabinet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.IntSummaryStatistics;

import static huckster.cabinet.StaticElements.*;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
@WebServlet("/orders")
public class OrderServlet extends UserServlet {
    static long timeStone = System.currentTimeMillis();

    private void initContent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserData userData = (UserData) req.getSession().getAttribute("userData");
        LocalDate startDate = (LocalDate) req.getSession().getAttribute("startDate");
        LocalDate endDate = (LocalDate) req.getSession().getAttribute("endDate");
        if (startDate == null) {
            startDate = DEFAULT_START_DATE;
        }
        if (endDate == null) {
            endDate = DEFAULT_END_DATE;
        }

        try {
            req.setAttribute("company", userData.getCompanyName());
            req.setAttribute("menu", StaticElements.getMenu());
            req.setAttribute("startDate", startDate.format(FORMATTER));
            req.setAttribute("endDate", endDate.format(FORMATTER));
            req.setAttribute("statuses", getOrderStatuses());
            req.getRequestDispatcher("/jsp/orders.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (auth(req, resp)) {
            initContent(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("orderId") != null) {
            req.setCharacterEncoding("UTF-8");
            DbData db = new DbData();
            try {
                db.updateOrder(Integer.parseInt(req.getParameter("orderId")), Integer.parseInt(req.getParameter("status")), req.getParameter("comment"));
            } catch (SQLException e) {
                e.printStackTrace();
                req.setAttribute("error", "Сохранение невозможно в данный момент. Попробуйте еще раз позднее");
                initContent(req, resp);
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

            resp.sendRedirect("/orders");
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
}
