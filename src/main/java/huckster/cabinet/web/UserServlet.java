package huckster.cabinet.web;

import huckster.cabinet.Util;
import huckster.cabinet.repository.UserData;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
abstract class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (auth(req, resp)) {
            UserData userData = (UserData) req.getSession().getAttribute("userData");
            try {
                req.setAttribute("company", userData.getCompanyName());
                initDataGet(req, resp, userData);
            } catch (SQLException e) {
                e.printStackTrace();
                req.getRequestDispatcher("/jsp/error.jsp").forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (auth(req, resp)) {
            UserData userData = (UserData) req.getSession().getAttribute("userData");
            try {
                req.setAttribute("company", userData.getCompanyName());
                initDataPost(req, resp, userData);
            } catch (Exception e) {
                Util.logError("Error while loading a servlet " + this.getClass(), e, userData);
                req.getRequestDispatcher("/jsp/error.jsp").forward(req, resp);
            }
        }
    }

    abstract void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException;
    abstract void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException;

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

    private boolean auth(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
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
