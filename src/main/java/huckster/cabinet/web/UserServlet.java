package huckster.cabinet.web;

import huckster.cabinet.Util;
import huckster.cabinet.model.CompanyEntity;
import huckster.cabinet.repository.CompanyInfoDao;
import huckster.cabinet.repository.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
abstract class UserServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(UserServlet.class);
    CompanyInfoDao dao = new CompanyInfoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (auth(req, resp)) {
            UserData userData = (UserData) req.getSession().getAttribute("userData");
            try {
                req.setAttribute("company", userData.getCompanyInfo());
                req.setAttribute("isWidgetActive", dao.isWidgetActive(userData.getCompanyId()));
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
                req.setAttribute("company", userData.getCompanyInfo());
                initDataPost(req, resp, userData);
            } catch (Exception e) {
                Util.logError("Response while loading a servlet " + this.getClass(), e, userData);
                req.getRequestDispatcher("/jsp/error.jsp").forward(req, resp);
            }
        }
    }

    abstract void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException;
    abstract void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException;

    private UserData getUser(HttpServletRequest req) throws ServletException {
        HttpSession session = req.getSession();
        UserData userData = (UserData) session.getAttribute("userData");
        if (userData == null) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("user")) {
                        userData = new UserData(c.getValue());
                        session.setAttribute("userData", userData);
                    }
                }
            }
        }

        return userData;
    }

    private boolean auth(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        UserData userData = getUser(req);
        if (userData != null) {
            return true;
        } else {
            boolean isAuthByToken = false;
            String token = req.getParameter("token");
            if (token != null) {
                try {
                    Optional <CompanyEntity> companyInfo = dao.getCompanyInfoByToken(token);
                    if (companyInfo.isPresent()) {
                        userData = new UserData(companyInfo.get());
                        req.getSession().setAttribute("userData", userData);
                        isAuthByToken = true;
                        dao.deleteToken(userData.getCompanyId());
                    }
                } catch (SQLException e) {
                    LOG.error("Failed to get company information by token " + token, e);
                }
            }
            if (isAuthByToken) {
                resp.sendRedirect("settings#settings");
                return false;
            } else {
                resp.sendRedirect("login");
                return false;
            }
        }
    }
}
