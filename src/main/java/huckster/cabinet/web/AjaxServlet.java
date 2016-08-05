package huckster.cabinet.web;

import huckster.cabinet.Util;
import huckster.cabinet.model.JsonTreeNode;
import huckster.cabinet.model.SelectedTreeEntity;
import huckster.cabinet.repository.OrdersDao;
import huckster.cabinet.repository.StatisticDao;
import huckster.cabinet.repository.UserData;
import huckster.cabinet.repository.WidgetSettingsDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by PerevalovaMA on 18.05.2016.
 */
@WebServlet("/ajax")
public class AjaxServlet extends HttpServlet {
    private OrdersDao ordersDao = new OrdersDao();
    private StatisticDao statisticDao = new StatisticDao();
    private WidgetSettingsDao widgetSettingsDao = new WidgetSettingsDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        UserData userData = (UserData) req.getSession().getAttribute("userData");
        String data = "";
        String type = req.getParameter("type");

        if (type != null) {
            switch (req.getParameter("type")) {
                case "orders":
                    data = getOrders(userData);
                    break;
                case "goods":
                    data = getGoods(userData);
                    break;
                case "settings_tree":
                    data = getTree(req, userData);
                    break;
                case "settings_lists":
                    data = getSettings(userData);
                    break;
                case "vendor_rules":
                    data = getVendorRules(req, userData);
                    break;
            }

            resp.setContentType("application/json; charset=utf-8");
            try {
                resp.getWriter().print(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getOrders(UserData userData) {
        List<List> data = new ArrayList<>();
        try {
            data = ordersDao.getOrders(userData.getCompanyId(), Date.valueOf(userData.getStartDate()), Date.valueOf(userData.getEndDate()));
        } catch (SQLException e) {
            //TODO: some message?
            Util.logError("Failed to load orders", e, userData);
        }

        HashMap<String, List> map = new HashMap<>();
        String link = "<a data-id=\"%s\" data-status=\"%s\" data-comment=\"%s\" data-toggle=\"modal\" href=\"#editOrder\"><span class=\"glyphicon glyphicon-pencil\"></a>";
        for (List row : data) {
            //order id, status, comment
            row.add(0, String.format(link, row.get(0), row.get(14), row.get(13)));
            row.remove(15);
        }

        map.put("data", data);
        return Util.toJson(map);
    }

    private String getGoods(UserData userData) {
        List<List> data = new ArrayList<>();
        try {
            data = statisticDao.getGoods(userData.getCompanyId(), userData.getPeriodGoods());
        } catch (SQLException e) {
            //TODO: some message?
            Util.logError("Failed to load goods", e, userData);
        }

        HashMap<String, List> map = new HashMap<>();
        map.put("data", data);
        return Util.toJson(map);
    }

    private String getSettings(UserData userData) {
        HashMap<String, Object> map = new HashMap<>();
        List<String> channels = new ArrayList<>();
        Map<String, String> sources = new LinkedHashMap<>();

        try {
            sources = widgetSettingsDao.getSources(userData.getCompanyId());
        } catch (SQLException e) {
            //TODO: kill modal?
            Util.logError("Failed to load sources", e, userData);
        }
        try {
            channels = widgetSettingsDao.getChannels(userData.getCompanyId());
        } catch (SQLException e) {
            //TODO: kill modal?
            Util.logError("Failed to load channels", e, userData);
        }
        map.put("sources", sources);
        map.put("channels", channels);
        return Util.toJson(map);
    }

    private String getTree(HttpServletRequest req, UserData userData) {
        Map<Integer, JsonTreeNode> map = new TreeMap<>();
        String ruleId = req.getParameter("id");
        try {
            List<SelectedTreeEntity> selectedTreeEntities;
            if (ruleId != null) {
                selectedTreeEntities = widgetSettingsDao.getSelectedTree(Integer.parseInt(ruleId));
            } else {
                selectedTreeEntities = widgetSettingsDao.getSelectedTree();
            }
            map = selectedTreeEntities.stream()
                    .collect(Collectors.toMap(SelectedTreeEntity::getId, v -> new JsonTreeNode(v.getId(), v.getTitle(), v.getParentId(), v.isSelected(), null)));
        } catch (SQLException e) {
            //TODO: kill modal?
            Util.logError("Failed to load tree for rule " + ruleId, e, userData);
        }
        List list = new ArrayList<>();

        for (Map.Entry<Integer, JsonTreeNode> entry : map.entrySet()) {
            JsonTreeNode node = entry.getValue();
            if (node.getParent() == 0) {
                list.add(node);
            } else {
                //  System.out.println(node.getParent());
                map.get(node.getParent()).addChild(node);
            }
        }

        return Util.toJson(list);
    }

    private String getVendorRules(HttpServletRequest req, UserData userData) {
        List<List> data = new ArrayList<>();
        String vendorId = req.getParameter("vendorId");
        if (vendorId != null) {
            try {
                data = widgetSettingsDao.getVendorsRules(userData.getCompanyId(), Integer.parseInt(vendorId));
            } catch (SQLException e) {
                //TODO: some message?
                Util.logError("Failed to load vendor rules", e, userData);
            }
        } else {
            //TODO: some message?
            Util.logError("Empty vendor category", userData);
        }

        HashMap<String, List> map = new HashMap<>();
        map.put("data", data);

        System.out.println(Util.toJson(map));
        return Util.toJson(map);
    }
}