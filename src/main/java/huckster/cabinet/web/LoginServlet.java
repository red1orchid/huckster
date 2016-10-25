/* Copyright � 2015 Oracle and/or its affiliates. All rights reserved. */
package huckster.cabinet.web;

import huckster.cabinet.OperationStatus;
import huckster.cabinet.repository.CompanyInfoDao;
import huckster.cabinet.repository.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);
    private CompanyInfoDao dao = new CompanyInfoDao();
    //setting cookie to expiry in 300 mins
    private static final int COOKIE_MAX_AGE = 600 * 60;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String rememberMe = req.getParameter("rememberMe");

        try {
            if (req.getParameter("restorePassword") != null) {
                sendResp(req, resp, restorePassword(req.getParameter("email")));
            } else {
                if (dao.isUserExists(username, password)) {
                    req.getSession().setAttribute("userData", new UserData(username));
                    if (rememberMe != null) {
                        Cookie loginCookie = new Cookie("user", username);
                        loginCookie.setMaxAge(COOKIE_MAX_AGE);
                        resp.addCookie(loginCookie);
                    }
                    resp.sendRedirect(".");
                } else {
                    sendResp(req, resp, new OperationStatus(false, "Неправильное имя пользователя или пароль"));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to login with username " + username, e);
            resp.sendRedirect("jsp/error.jsp");
        }
    }

    private OperationStatus restorePassword(String email) throws SQLException {
        OperationStatus status;
        if (!dao.isEmailExists(email)) {
            status = new OperationStatus(false, "Пользователя с указанным email не существует");
        } else {
            if (dao.restorePassword(email)) {
                status = new OperationStatus(true);
            } else {
                LOG.error("Failed to restore password for user " + email);
                status = new OperationStatus(false, "Операция временно невозможна. Повторите попытку позднее");
            }
        }

        return status;
    }

    private void sendResp(HttpServletRequest req, HttpServletResponse resp, OperationStatus status) throws ServletException, IOException  {
        req.setAttribute("status", status);
        req.getRequestDispatcher("jsp/login.jsp").forward(req, resp);
    }
}
