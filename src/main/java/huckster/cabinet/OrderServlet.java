package huckster.cabinet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

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

            try {
                req.setAttribute("company", userData.getCompanyName());
                req.setAttribute("menu", StaticElements.getMenu());
                req.setAttribute("orders", userData.getOrders());
                req.getRequestDispatcher("/jsp/orders.jsp").forward(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
