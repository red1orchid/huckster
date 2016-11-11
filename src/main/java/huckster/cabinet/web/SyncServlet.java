package huckster.cabinet.web;

import huckster.cabinet.repository.SyncDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by PerevalovaMA on 09.11.2016.
 */
@WebServlet("/sync")
public class SyncServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("do");
        if (action == null) {
            resp.sendError(500, "Action can't be empty");
            return;
        }

        if (action.equals("refreshAll")) {
            SyncDao.init();
        } else {
            if (!req.getRemoteAddr().equals("188.166.59.12")) {
                resp.sendError(403);
                return;
            }
            if (req.getParameter("companyId") == null) {
                resp.sendError(500, "Company id can't be empty");
                return;
            }

            int companyId = Integer.parseInt(req.getParameter("companyId"));
            if (action.equals("show")) {
                SyncDao.setSyncState(companyId, true);
                WebSocketServer.startSynchronization(companyId);
            }
            if (action.equals("hide")) {
                SyncDao.setSyncState(companyId, false);
                WebSocketServer.endSynchronization(companyId);
            }
        }
    }
}