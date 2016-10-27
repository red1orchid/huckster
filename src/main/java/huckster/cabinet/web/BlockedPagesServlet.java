package huckster.cabinet.web;

import huckster.cabinet.OperationStatus;
import huckster.cabinet.Util;
import huckster.cabinet.repository.SettingsDao;
import huckster.cabinet.repository.UserData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by PerevalovaMA on 26.10.2016.
 */
@WebServlet("/blocked_pages")
/*TODO: ajax only*/
public class BlockedPagesServlet extends UserServlet implements JsonOutput {
    private SettingsDao dao = new SettingsDao();

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
    }

    @Override
    void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        String type = req.getParameter("type");
        if (type != null) {
            OperationStatus status;
            switch (type) {
                case "save": {
                    writeObject(resp, getOperationStatus(saveUrl(req, userData), "Ошибка сохранения. Проверьте правильность вводимых данных"));
                }
                break;
                case "delete": {
                    //TODO: text?!
                    writeObject(resp, getOperationStatus(deleteUrl(req, userData), "Ошибка удаления"));
                }
                break;
            }
        }
    }

    private boolean saveUrl(HttpServletRequest req, UserData userData) {
        String url = req.getParameter("url");
        Integer id = stringToInt(req.getParameter("id"));
        int isTrash = Integer.parseInt(req.getParameter("isTrash"));
        boolean success = false;

        if (id != null) {
            try {
                dao.updateBlockedUrl(userData.getCompanyId(), id, url, isTrash);
                success = true;
            } catch (SQLException e) {
                Util.logError("Failed to update blocked url " + req.getParameter("id"), e, userData);
            }
        } else {
            try {
                dao.insertBlockedUrl(userData.getCompanyId(), url, isTrash);
                success = true;
            } catch (SQLException e) {
                Util.logError("Failed to insert blocked url", e, userData);
            }
        }
        return success;
    }

    private boolean deleteUrl(HttpServletRequest req, UserData userData) {
        Integer id = stringToInt(req.getParameter("id"));

        if (id != null) {
            try {
                dao.deleteBlockedUrl(userData.getCompanyId(), id);
                return true;
            } catch (SQLException e) {
                Util.logError("Failed to delete blocked url " + req.getParameter("id"), e, userData);
            }
        } else {
            Util.logError("Empty blocked url id", userData);
        }
        return false;
    }
}
