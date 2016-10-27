package huckster.cabinet.web;

import huckster.cabinet.DataException;
import huckster.cabinet.OperationStatus;
import huckster.cabinet.Util;
import huckster.cabinet.model.CompanySettingsEntity;
import huckster.cabinet.repository.CompanyInfoDao;
import huckster.cabinet.repository.SettingsDao;
import huckster.cabinet.repository.UserData;
import huckster.cabinet.repository.WidgetSettingsDao;

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
public class SettingsServlet extends UserServlet implements JsonOutput {
    private CompanyInfoDao companyDao = new CompanyInfoDao();
    private SettingsDao dao = new SettingsDao();
    private WidgetSettingsDao widgetDao = new WidgetSettingsDao();
    private GeoServlet geo = new GeoServlet();

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        String type = req.getParameter("type");
        if (type == null) {
            userData.clear();

            boolean isAutoMode = isAutoMode(userData);
            req.setAttribute("companyId", userData.getCompanyId());
            req.setAttribute("settings", getCompanySettings(userData));
            req.setAttribute("urls", dao.getBlockedUrls(userData.getCompanyId()));
            req.setAttribute("isAutoMode", isAutoMode);
            req.setAttribute("isScriptInstalled", isScriptInstalled(userData));
            req.setAttribute("rule", geo.getGeo(userData));
            if (!isAutoMode) {
                req.setAttribute("devices", widgetDao.getDevices());
            }

            req.getRequestDispatcher("/jsp/settings.jsp").forward(req, resp);
        } else if (type.equals("widget_url")) {
            resp.getWriter().print(widgetDao.getUrl(userData.getCompanyId()).orElse(null));
        }
    }

    @Override
    void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        String type = req.getParameter("type");
        if (type != null) {
            switch (type) {
                case "save_settings":
                    writeObject(resp, getOperationStatus(saveSettings(req, userData), "Ошибка сохранения. Проверьте правильность вводимых данных"));
                break;
                case "auto_mode": {
                    if (req.getParameter("mode") != null) {
                        try {
                            dao.setAutoMode(userData.getCompanyId(), Boolean.parseBoolean(req.getParameter("mode")));
                        } catch (SQLException e) {
                            Util.logError("Failed to set auto mode", userData);
                        }
                    }
                }
                break;
                case "save_password": {
                    writeObject(resp, savePassword(req, userData));
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
            throw new DataException("Failed to load general settings");
        }
    }

    private boolean isAutoMode(UserData userData) {
        try {
            return dao.isAutoMode(userData.getCompanyId());
        } catch (SQLException e) {
            Util.logError("Failed to select mode", e, userData);
            return false;
        }
    }

    private boolean isScriptInstalled(UserData userData) {
        try {
            return dao.isScriptInstalled(userData.getCompanyId());
        } catch (SQLException e) {
            Util.logError("Failed to select info about script installation", e, userData);
            return false;
        }
    }

    private boolean saveSettings(HttpServletRequest req, UserData userData) {
        String isEnabled = req.getParameter("isEnabled");
        try {
            if (isEnabled == null) {
                throw new NotFoundException("Empty field isEnabled");
            } else {
                int isActive = Boolean.parseBoolean(isEnabled) ? 1 : 0;
                dao.updateCompanySettings(userData.getCompanyId(), req.getParameter("yml"), req.getParameter("orderEmails"), req.getParameter("contactEmails"),
                        req.getParameter("yandexKey"), isActive);
                return true;
            }
        } catch (SQLException | NotFoundException e) {
            Util.logError("Failed to update settings for company ", e, userData);
        }
        return false;
    }

    private OperationStatus savePassword(HttpServletRequest req, UserData userData) {
        OperationStatus status = null;
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");
        if (oldPassword != null) {
            try {
                if (companyDao.isPasswordCorrect(userData.getCompanyId(), oldPassword)) {
                    companyDao.setPassword(userData.getCompanyId(), newPassword);
                    status = new OperationStatus(true);
                } else {
                    status = new OperationStatus(false, "Введен неверный пароль");
                }
            } catch (SQLException e) {
                Util.logError("Failed to save password", e, userData);
            }
        } else {
            Util.logError("Empty password", userData);
        }

        if (status == null) {
            status = new OperationStatus(false, "Ошибка сохранения. Проверьте правильность вводимых данных");
        }
        return status;
    }
}
