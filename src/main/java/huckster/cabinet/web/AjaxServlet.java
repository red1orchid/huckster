package huckster.cabinet.web;

import huckster.cabinet.Util;
import huckster.cabinet.model.*;
import huckster.cabinet.repository.OrdersDao;
import huckster.cabinet.repository.StatisticDao;
import huckster.cabinet.repository.UserData;
import huckster.cabinet.repository.WidgetSettingsDao;
import org.apache.commons.math3.exception.NoDataException;

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
    private Util util = new Util();

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
                case "vendor_discounts":
                    data = getVendorDiscounts(req, userData);
                    break;
                case "vendor_categories":
                    data = getVendorCategories(req, userData);
                    break;
                case "vendors":
                    data = Util.toJson(getVendors(req, userData));
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
        List<OrderEntity> data = new ArrayList<>();
        try {
            data = ordersDao.getOrders(userData.getCompanyId(), Date.valueOf(userData.getStartDate()), Date.valueOf(userData.getEndDate()));
        } catch (SQLException e) {
            //TODO: some message?
            Util.logError("Failed to load orders", e, userData);
        }

/*        HashMap<String, List> map = new HashMap<>();
        String link = "<a data-id=\"%s\" data-status=\"%s\" data-comment=\"%s\" data-toggle=\"modal\" href=\"#editOrder\"><span class=\"glyphicon glyphicon-pencil\"></a>";
        for (List row : data) {
            //order id, status, comment
            row.add(0, String.format(link, row.get(0), row.get(14), row.get(13)));
            row.remove(15);
        }*/

        return util.toJsonWithDataWrap(data);
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

    private String getVendorDiscounts(HttpServletRequest req, UserData userData) {
        List<DiscountEntity> data = new ArrayList<>();
        String segmentId = req.getParameter("ruleId");
        if (!segmentId.isEmpty()) {
            try {
                data = widgetSettingsDao.getVendorsDiscounts(userData.getCompanyId(), Integer.parseInt(segmentId));
            } catch (SQLException e) {
                //TODO: some message?
                Util.logError("Failed to load vendor discounts", e, userData);
            }
        } else {
            //TODO: some message?
            Util.logError("Empty segment", userData);
        }

        return util.toJsonWithDataWrap(data);
    }

    private String getVendorCategories(HttpServletRequest req, UserData userData) {
        Map <String, Object> map = new HashMap<>();
        initCategories(userData);
        map.put("categories", userData.getCategoryContainer());

        map.put("vendors", getVendors(req, userData));
        return Util.toJson(map);
    }

    private List<String> getVendors(HttpServletRequest req, UserData userData) {
        List<String> vendors;
        String categoryId = req.getParameter("categoryId");
        if (categoryId != null) {
            initVendorsCat(userData);
            vendors = userData.getVendorCatContainer().get(Integer.parseInt(categoryId));
        } else {
            initVendors(userData);
            vendors = userData.getVendorContainer();
        }
        return vendors;
    }

    private void initCategories(UserData userData) {
        if (userData.getCategoryContainer().isEmpty()) {
            try {
                List<ListEntity<Integer, String>> categories = widgetSettingsDao.getCategories(userData.getCompanyId());
                if (!categories.isEmpty()) {
                    userData.setCategoryContainer(categories);
                } else {
                    Util.logError("No categories found", userData);
                }
            } catch (SQLException e) {
                Util.logError("Failed to load categories", e, userData);
            }
        }
    }

    private void initVendors(UserData userData) {
        if (userData.getVendorContainer().isEmpty()) {
            try {
                List<String> vendors = widgetSettingsDao.getVendors(userData.getCompanyId());
                if (!vendors.isEmpty()) {
                    userData.setVendorContainer(vendors);
                } else {
                    Util.logError("No vendors found", userData);
                }
            } catch (SQLException e) {
                Util.logError("Failed to load vendors", e, userData);
            }
        }
    }

    private void initVendorsCat(UserData userData) {
        if (userData.getVendorCatContainer().isEmpty()) {
            try {
                Map<Integer, List<String>> vendors = widgetSettingsDao.getVendorsByCategory(userData.getCompanyId());
                if (!vendors.isEmpty()) {
                    userData.setVendorCatContainer(vendors);
                } else {
                    Util.logError("No vendors found", userData);
                }
            } catch (SQLException e) {
                Util.logError("Failed to load vendors", e, userData);
            }
        }
    }

/*    private String getVendorDiscount(HttpServletRequest req, UserData userData) {
        System.out.println("getVendorDiscount !!");
        String id = req.getParameter("id");
        System.out.println(id);
        Map <String, Object> map = new HashMap<>();
        if (id != null) {
            try {
                DiscountEntity discount = widgetSettingsDao.getVendorsDiscount(Integer.parseInt(id)).orElseThrow(NoDataException::new);
                map.put("categories", getCategories(userData));
                map.put("category", discount.getCategory());
                map.put("vendors", getVendors(userData, discount.getCategory()));
                map.put("vendor", discount.getVendor());
                map.put("minPrice", discount.getMinPrice());
                map.put("maxPrice", discount.getMaxPrice());
                map.put("discount1", discount.getDiscount1());
                map.put("discount2", discount.getDiscount2());
            } catch (NoDataException | SQLException e) {
                //TODO: kill modal?
                Util.logError("Failed to load discount №" + id, e, userData);
            }
        } else {
            map.put("vendors", getVendors(userData, ""));
            map.put("categories", getCategories(userData));
        }

        return Util.toJson(map);
    }
    private List<ListEntity<Integer, String>> getCategories(UserData userData) {
        List<ListEntity<Integer, String>> categories = new ArrayList<>();
        try {
            categories = widgetSettingsDao.getCategories(userData.getCompanyId());
        } catch (SQLException e) {
            //TODO: kill modal?
            Util.logError("Failed to load categories", e, userData);
        }

        return categories;
    }

    private List<String> getVendors(UserData userData, String categoryId) {
        List<String> vendors = new ArrayList<>();
        try {
            if (categoryId != null && !categoryId.isEmpty()) {
                vendors = widgetSettingsDao.getVendors(userData.getCompanyId(), Integer.parseInt(categoryId));
            } else {
                vendors = widgetSettingsDao.getVendors(userData.getCompanyId());
            }
        } catch (SQLException e) {
            //TODO: kill modal?
            Util.logError("Failed to load vendors", e, userData);
        }

        return vendors;
    }*/
}