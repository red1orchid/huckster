/* Copyright � 2015 Oracle and/or its affiliates. All rights reserved. */
package huckster.cabinet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    //setting cookie to expiry in 30 mins
    private static final int COOKIE_MAX_AGE = 60 * 60;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String rememberMe = req.getParameter("rememberMe");

        try {
            DbData db = new DbData();
            if (db.isUserExists(username, password)) {
                req.getSession().setAttribute("user", username);
                if (rememberMe != null) {
                    Cookie loginCookie = new Cookie("user", username);
                    loginCookie.setMaxAge(COOKIE_MAX_AGE);
                    resp.addCookie(loginCookie);
                }
                resp.sendRedirect("/");
            } else {
                req.setAttribute("message", "Неправильное имя пользователя или пароль");
                req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("/jsp/error.jsp");
        }
    }
}
