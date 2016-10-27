package huckster.cabinet.web;

import huckster.cabinet.DataException;
import huckster.cabinet.OperationStatus;
import huckster.cabinet.Util;
import huckster.cabinet.model.JsonTreeNode;
import huckster.cabinet.model.RuleEntity;
import huckster.cabinet.model.TreeEntity;
import huckster.cabinet.repository.UserData;
import huckster.cabinet.repository.WidgetSettingsDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Created by PerevalovaMA on 26.10.2016.
 */
@WebServlet("/geo")
/*TODO: ajax only*/
public class GeoServlet extends UserServlet implements JsonOutput {
    private WidgetSettingsDao dao = new WidgetSettingsDao();

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        String type = req.getParameter("type");
        if (type != null) {
            switch (type) {
                case "channels":
                    writeObject(resp, getChannels(userData));
                    break;
                case "tree":
                    writeObject(resp, getTree(userData));
                    break;
            }
        }
    }

    @Override
    protected void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        writeObject(resp, getOperationStatus(saveGeo(req, userData), "Ошибка сохранения. Повторите попытку позже или обратитесь в поддержку"));
    }

    private List<JsonTreeNode> getTree(UserData userData) {
        try {
            List<TreeEntity> selectedTreeEntities = dao.getTree(userData.getCompanyId());
            Map<Integer, JsonTreeNode> map = selectedTreeEntities.stream()
                    .collect(Collectors.toMap(TreeEntity::getId, v -> new JsonTreeNode(v.getId(), v.getTitle(), v.getParentId(), v.isSelected(), null)));

            List<JsonTreeNode> list = new ArrayList<>();
            for (Map.Entry<Integer, JsonTreeNode> entry : map.entrySet()) {
                JsonTreeNode node = entry.getValue();
                if (node.getParent() == 0) {
                    list.add(node);
                } else {
                    map.get(node.getParent()).addChild(node);
                }
            }

            return list;
        } catch (SQLException e) {
            Util.logError("Failed to load tree", e, userData);
            //fatal
            throw new DataException("Failed to load geo tree");
        }
    }

    private List<String> getChannels(UserData userData) {
        List<String> channels = new ArrayList<String>();
        try {
            channels = dao.getChannels(userData.getCompanyId());
        } catch (SQLException e) {
            Util.logError("Failed to load channels", e, userData);
        }

        return channels;
    }

    RuleEntity getGeo(UserData userData) {
        try {
            return dao.getRule(userData.getCompanyId()).get();
        } catch (SQLException | NoSuchElementException e) {
            Util.logError("Failed to select geo rule", e, userData);
            throw new DataException("Failed to load geo rule");
        }
    }

    private boolean saveGeo(HttpServletRequest req, UserData userData) {
        try {
            RuleEntity rule = new RuleEntity(req.getParameter("channels"), Integer.parseInt(req.getParameter("devices")), req.getParameter("days"),
                    Integer.parseInt(req.getParameter("hourFrom")), Integer.parseInt(req.getParameter("hourTo")), req.getParameter("tree"));
            dao.updateRule(userData.getCompanyId(), rule);
            return true;
        } catch (NumberFormatException | SQLException e) {
            Util.logError("Failed to update geo", e, userData);
            return false;
        }
    }
}
