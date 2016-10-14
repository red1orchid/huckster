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
        System.out.println("restore: " + req.getParameter("restorePassword"));

        OperationStatus status;
        try {
            if (req.getParameter("restorePassword") != null) {
                if (true) {
                    status = new OperationStatus(true, "Инструкция по смене пароля отправлена на почту");
                } else {
                    status = new OperationStatus(false, "Не существует пользователя с указанным email");
                }
                sendResp(req, resp, status);
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

    private void sendResp(HttpServletRequest req, HttpServletResponse resp, OperationStatus status) throws ServletException, IOException  {
        req.setAttribute("status", status);
        req.getRequestDispatcher("jsp/login.jsp").forward(req, resp);
    }
}
