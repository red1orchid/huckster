/* Copyright � 2015 Oracle and/or its affiliates. All rights reserved. */
package huckster.cabinet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "LoginServlet",
        urlPatterns = {"/login"}
)
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if(checkLogin(req.getParameter("username"), req.getParameter("password"))){
            Cookie loginCookie = new Cookie("user", req.getParameter("username"));
            //setting cookie to expiry in 30 mins
            loginCookie.setMaxAge(30*60);
            resp.addCookie(loginCookie);
            resp.sendRedirect("/");
        }else{
            req.setAttribute("message", "Неправильное имя пользователя или пароль");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
        }
    }

    private boolean checkLogin(String username, String password) {
        if (username.equals("1admin") && password.equals("nikita"))
            return true;
        else
            return false;
    }

}
