package huckster.cabinet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static huckster.cabinet.StaticElements.*;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
@WebServlet("/orders")
public class OrderServlet extends UserServlet {
    static long timeStone = System.currentTimeMillis();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (auth(req, resp)) {
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
                req.getRequestDispatcher("/jsp/orders.jsp").forward(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate startDate = LocalDate.parse(req.getParameter("startDate"), FORMATTER);
        LocalDate endDate = LocalDate.parse(req.getParameter("endDate"), FORMATTER);

        req.getSession().setAttribute("startDate", startDate);
        req.getSession().setAttribute("endDate", endDate);

        resp.sendRedirect("/orders");
    }
}
