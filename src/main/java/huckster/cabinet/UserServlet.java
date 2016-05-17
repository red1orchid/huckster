package huckster.cabinet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
abstract class UserServlet extends HttpServlet {
    private String getUser(HttpServletRequest req) {
        String user = (String) req.getSession().getAttribute("user");
        if (user == null) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("user")) {
                        user = c.getValue();
                    }
                }
            }
        }

        return user;
    }

    boolean auth(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String user = getUser(req);
        HttpSession session = req.getSession();
        if (user != null) {
            if (session.getAttribute("userData") == null) {
                UserData userData = new UserData(user);
                session.setAttribute("userData", userData);
            }
            return true;
        } else {
            resp.sendRedirect("login");
            return false;
        }
    }
}
