package huckster.cabinet.web;

import huckster.cabinet.Util;
import huckster.cabinet.model.RuleEntity;
import huckster.cabinet.repository.UserData;
import huckster.cabinet.repository.WidgetSettingsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PerevalovaMA on 30.05.2016.
 */
@WebServlet("/widget_settings")
public class WidgetSettingsServlet extends UserServlet {
    private static final Logger LOG = LoggerFactory.getLogger(WidgetSettingsServlet.class);
    WidgetSettingsDao dao = new WidgetSettingsDao();

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        req.setAttribute("rules", getRules(userData));
        req.setAttribute("devices", dao.getDevices());
        req.setAttribute("segments", getSegments(userData));
/*        req.setAttribute("channels", userData.getChannels());
        Util.timeStone("stat: get channels");
        req.setAttribute("sources", userData.getSources());
        Util.timeStone("stat: get sources");
        req.setAttribute("channel", "cpa:referral");
        Util.timeStone("stat: get data");*/
        req.getRequestDispatcher("/jsp/widget_settings.jsp").forward(req, resp);
    }

    @Override
    protected void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        LOG.debug("post");
        System.out.println("DODODO");
        System.out.println(req.getParameter("rule"));
        System.out.println(req.getParameter("tree"));
        System.out.println(req.getParameter("channels"));
        System.out.println(req.getParameter("sources"));
        System.out.println(req.getParameter("devices"));
        System.out.println(req.getParameter("days"));
        System.out.println(req.getParameter("hourFrom"));
        System.out.println(req.getParameter("hourTo"));
        Integer ruleId = null;
        if (req.getParameter("rule") != null) {
            ruleId = Integer.parseInt(req.getParameter("rule"));
        }

        try {
            dao.updateRules(userData.getCompanyId(), ruleId, req.getParameter("tree"), req.getParameter("channels"), req.getParameter("sources")
                    , req.getParameter("devices"), req.getParameter("days"), req.getParameter("hourFrom"), req.getParameter("hourTo"));
        } catch (SQLException e) {
            //TODO: some message?
            Util.logError("Failed to update rule " + ruleId, e, userData);
        }
    }

    private List<RuleEntity> getRules(UserData userData) {
        try {
            return dao.getRules(userData.getCompanyId());
        } catch (SQLException e) {
            //TODO: some message?
            Util.logError("Failed to load rules", e, userData);
            return new ArrayList<>();
        }
    }

    private Map<Integer, String> getSegments(UserData userData) throws SQLException {
        try {
            return dao.getSegments(userData.getCompanyId());
        } catch (SQLException e) {
            //TODO: fatal?
            Util.logError("Failed to load segments", e, userData);
            return new LinkedHashMap<>();
        }
    }
}
