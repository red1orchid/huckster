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
                case "save_page": {
                    String url = req.getParameter("url");
                    int isTrash = Integer.parseInt(req.getParameter("isTrash"));
                    if (isNumber(req.getParameter("id"))) {
                        try {
                            dao.updateBlockedUrl(userData.getCompanyId(), Integer.parseInt(req.getParameter("id")), url, isTrash);
                        } catch (SQLException e) {
                            //TODO: error?
                            Util.logError("Failed to update blocked url " + req.getParameter("id"), e, userData);
                        }
                    } else {
                        try {
                            dao.insertBlockedUrl(userData.getCompanyId(), url, isTrash);
                        } catch (SQLException e) {
                            //TODO: error?
                            Util.logError("Failed to insert blocked url", e, userData);
                        }
                    }
                }
                break;
                case "delete_page": {
                    if (isNumber(req.getParameter("id"))) {
                        try {
                            dao.deleteBlockedUrl(userData.getCompanyId(), Integer.parseInt(req.getParameter("id")));
                        } catch (SQLException e) {
                            //TODO: error?
                            Util.logError("Failed to delete blocked url " + req.getParameter("id"), e, userData);
                        }
                    } else {
                        //TODO: error?
                        Util.logError("Empty blocked url id", userData);
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

    private boolean isNumber(String src) {
        return (src != null && !src.isEmpty());
    }
}
