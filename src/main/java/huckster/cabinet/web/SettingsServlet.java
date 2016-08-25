package huckster.cabinet.web;

import huckster.cabinet.Util;
import huckster.cabinet.model.CompanySettingsEntity;
import huckster.cabinet.repository.SettingsDao;
import huckster.cabinet.repository.UserData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Perevalova Marina on 07.08.2016.
 */
@WebServlet("/settings")
public class SettingsServlet extends UserServlet {
    private SettingsDao dao = new SettingsDao();

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        req.setAttribute("companyId", userData.getCompanyId());
        req.setAttribute("settings", getCompanySettings(userData));
        req.setAttribute("urls", dao.getBlockedUrls(userData.getCompanyId()));
        req.getRequestDispatcher("/jsp/settings.jsp").forward(req, resp);
    }

    @Override
    void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        String type = req.getParameter("type");
        if (type != null) {
            switch (type) {
                case "save_settings": {
                    String isEnabled = req.getParameter("isEnabled");
                    try {
                        if (isEnabled == null) {
                            throw new NotFoundException("Empty field isEnabled");
                        } else {
                            int isActive = Boolean.parseBoolean(isEnabled) ? 1 : 0;
                            dao.updateCompanySettings(userData.getCompanyId(), req.getParameter("yml"), req.getParameter("orderEmails"), req.getParameter("contactEmails"),
                                    req.getParameter("yandexKey"), isActive);
                        }
                    } catch (SQLException | NotFoundException e) {
                        Util.logError("Failed to update settings for company ", e, userData);
                        //TODO: error?
                    }
                }
                break;
            }
        }
    }

    private CompanySettingsEntity getCompanySettings(UserData userData) {
        try {
            return dao.getCompanySettings(userData.getCompanyId()).orElseThrow(NotFoundException::new);
        } catch (SQLException | NotFoundException e) {
            Util.logError("Failed to load settings for company ", e, userData);
            //TODO: fatal? 500? message?
            throw new RuntimeException("");
        }
    }
}
