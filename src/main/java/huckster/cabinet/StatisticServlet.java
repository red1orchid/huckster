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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (auth(req, resp)) {
            UserData userData = (UserData) req.getSession().getAttribute("userData");

            req.setAttribute("menu", StaticElements.getMenu());
            try {
                req.setAttribute("company", userData.getCompanyName());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            req.getRequestDispatcher("/jsp/statistic.jsp").forward(req, resp);
        }
    }
}