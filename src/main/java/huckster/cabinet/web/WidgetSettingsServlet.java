package huckster.cabinet.web;

import huckster.cabinet.Util;
import huckster.cabinet.model.*;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by PerevalovaMA on 30.05.2016.
 */
@WebServlet("/widget_settings")
public class WidgetSettingsServlet extends UserServlet implements JsonOutput {
    private static final Logger LOG = LoggerFactory.getLogger(WidgetSettingsServlet.class);
    private WidgetSettingsDao dao = new WidgetSettingsDao();
    private Util util = new Util();

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        userData.clear();
        req.setAttribute("rules", getRules(userData));
        req.setAttribute("devices", dao.getDevices());
        req.setAttribute("segments", getSegments(userData));
        req.getRequestDispatcher("/jsp/widget_settings.jsp").forward(req, resp);
    }

    @Override
    protected void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        String type = req.getParameter("type");
        if (type != null) {
            if ("ajax".equals(req.getParameter("request"))) {
                writeJson(resp, getAjaxData(type, req, userData));
            } else {
                LOG.debug("widget settings post");
                switch (type) {
                    case "save_vendor_discount": {
                        Integer categoryId = stringToInt(req.getParameter("category"));
                        String vendor = req.getParameter("vendors");
                        Integer step1 = stringToInt(req.getParameter("discount1"));
                        Integer step2 = stringToInt(req.getParameter("discount2"));
                        Integer minPrice = stringToInt(req.getParameter("minPrice"));
                        Integer maxPrice = stringToInt(req.getParameter("maxPrice"));
                        if (!isNumber(req.getParameter("ruleId"))) {
                            Util.logError("empty segment for vendor discount " + req.getParameter("id"), userData);
                            //TODO: error?
                        } else {
                            if (isNumber(req.getParameter("id"))) {
                                try {
                                    dao.updateVendorsDiscount(Integer.parseInt(req.getParameter("id")), userData.getCompanyId(), Integer.parseInt(req.getParameter("ruleId")),
                                            categoryId, vendor, step1, step2, minPrice, maxPrice);
                                } catch (SQLException e) {
                                    //TODO: error?
                                    Util.logError("Failed to update vendor discount " + req.getParameter("id"), e, userData);
                                }
                            } else {
                                try {
                                    dao.insertVendorsDiscount(userData.getCompanyId(), Integer.parseInt(req.getParameter("ruleId")),
                                            categoryId, vendor, step1, step2, minPrice, maxPrice);
                                } catch (SQLException e) {
                                    //TODO: error?
                                    Util.logError("Failed to insert new vendor discount", e, userData);
                                }
                            }
                        }
                    }
                    break;
                    case "delete_vendor_discount": {
                        if (isNumber(req.getParameter("id"))) {
                            try {
                                dao.deleteVendorsDiscount(Integer.parseInt(req.getParameter("id")), userData.getCompanyId());
                            } catch (SQLException e) {
                                //TODO: error?
                                Util.logError("Failed to delete vendor discount " + req.getParameter("id"), e, userData);
                            }
                        } else {
                            //TODO: error?
                            Util.logError("Empty vendor discount id", userData);
                        }
                    }
                    break;
                    case "save_rule": {
                        String cities = req.getParameter("tree");
                        String channels = req.getParameter("channels");
                        String sources = req.getParameter("sources");
                        String devices = req.getParameter("devices");
                        String days = req.getParameter("days");
                        String startHour = req.getParameter("hourFrom");
                        String endHour = req.getParameter("hourTo");
                        if (isNumber(req.getParameter("id"))) {
                            try {
                                dao.updateRule(Integer.parseInt(req.getParameter("id")), userData.getCompanyId(), cities, channels, sources, devices, days, startHour, endHour);
                            } catch (SQLException e) {
                                //TODO: error?
                                Util.logError("Failed to update rule " + req.getParameter("id"), e, userData);
                            }
                        } else {
                            try {
                                dao.insertRule(userData.getCompanyId(), cities, channels, sources, devices, days, startHour, endHour);
                            } catch (SQLException e) {
                                //TODO: error?
                                Util.logError("Failed to insert new rule", e, userData);
                            }
                        }
                    }
                    break;
                    case "delete_rule": {
                        if (isNumber(req.getParameter("id"))) {
                            try {
                                dao.deleteRule(Integer.parseInt(req.getParameter("id")), userData.getCompanyId());
                            } catch (SQLException e) {
                                //TODO: error?
                                Util.logError("Failed to delete rule " + req.getParameter("id"), e, userData);
                            }
                        } else {
                            //TODO: error?
                            Util.logError("Empty rule id", userData);
                        }
                    }
                    break;
                    case "save_offer_discount": {
                        int ruleId = Integer.parseInt(req.getParameter("ruleId"));
                        int offerId = Integer.parseInt(req.getParameter("offerId"));
                        Integer step1 = stringToInt(req.getParameter("discount1"));
                        Integer step2 = stringToInt(req.getParameter("discount2"));
                        if (isNumber(req.getParameter("id"))) {
                            try {
                                dao.updateOfferDiscount(Integer.parseInt(req.getParameter("id")), userData.getCompanyId(), ruleId, offerId, step1, step2);
                            } catch (SQLException e) {
                                //TODO: error?
                                Util.logError("Failed to update offer discount " + req.getParameter("id"), e, userData);
                            }
                        } else {
                            try {
                                dao.insertOfferDiscount(userData.getCompanyId(), ruleId, offerId, step1, step2);
                            } catch (SQLException e) {
                                //TODO: error?
                                Util.logError("Failed to insert offer discount", e, userData);
                            }
                        }
                    }
                    break;
                    case "delete_offer_discount": {
                        if (isNumber(req.getParameter("id"))) {
                            try {
                                dao.deleteOfferDiscount(Integer.parseInt(req.getParameter("id")), userData.getCompanyId());
                            } catch (SQLException e) {
                                //TODO: error?
                                Util.logError("Failed to delete rule " + req.getParameter("id"), e, userData);
                            }
                        } else {
                            //TODO: error?
                            Util.logError("Empty rule id", userData);
                        }
                    }
                    break;
                }
            }
        }
    }

    private Integer stringToInt(String src) {
        if (src != null && !src.isEmpty())
            return Integer.parseInt(src);
        else
            return null;
    }

    private boolean isNumber(String src) {
        return (src != null && !src.isEmpty());
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

    private String getAjaxData(String type, HttpServletRequest req, UserData userData) {
        String data = "";
        switch (type) {
            case "rules":
                data = getRules(userData);
                break;
            case "settings_tree":
                data = getTree(req, userData);
                break;
            case "settings_lists":
                data = getSettings(userData);
                break;
            case "vendors_discounts":
                data = getVendorDiscounts(req, userData);
                break;
            case "vendors_categories":
                data = getVendorCategories(req, userData);
                break;
            case "vendors":
                data = Util.toJson(getVendors(req, userData));
                break;
            case "offers_discounts":
                data = getOfferDiscounts(req, userData);
                break;
            case "offers":
                data = getOffers(req, userData);
                break;
        }
        return data;
    }

    // Step 1
    private String getRules(UserData userData) {
        List<RuleEntity> data = new ArrayList<>();
        try {
            data = dao.getRules(userData.getCompanyId());
        } catch (SQLException e) {
            //TODO: some message?
            Util.logError("Failed to load rules", e, userData);
        }

        return util.toJsonWithDataWrap(data);
    }

    private String getSettings(UserData userData) {
        HashMap<String, Object> map = new HashMap<>();
        List<String> channels = new ArrayList<>();
        List<String> sources = new ArrayList<>();

        try {
            sources = dao.getSources(userData.getCompanyId());
        } catch (SQLException e) {
            //TODO: kill modal?
            Util.logError("Failed to load sources", e, userData);
        }
        try {
            channels = dao.getChannels(userData.getCompanyId());
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
                selectedTreeEntities = dao.getSelectedTree(Integer.parseInt(ruleId));
            } else {
                selectedTreeEntities = dao.getSelectedTree();
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

    // Step 2
    private String getVendorDiscounts(HttpServletRequest req, UserData userData) {
        List<DiscountEntity> data = new ArrayList<>();
        String segmentId = req.getParameter("ruleId");
        if (!segmentId.isEmpty()) {
            try {
                data = dao.getVendorsDiscounts(userData.getCompanyId(), Integer.parseInt(segmentId));
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
        Map<String, Object> map = new HashMap<>();
        initCategories(userData);
        map.put("categories", userData.getCategoryContainer());

        map.put("vendors", getVendors(req, userData));
        return Util.toJson(map);
    }

    private List<String> getVendors(HttpServletRequest req, UserData userData) {
        List<String> vendors;
        Integer categoryId = stringToInt(req.getParameter("categoryId"));
        if (categoryId != null) {
            initVendorsCat(userData);
            vendors = userData.getVendorCatContainer().get(categoryId);
        } else {
            initVendors(userData);
            vendors = userData.getVendorContainer();
        }
        return vendors;
    }

    private void initCategories(UserData userData) {
        if (userData.getCategoryContainer().isEmpty()) {
            try {
                List<ListEntity<Integer, String>> categories = dao.getCategories(userData.getCompanyId());
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
                List<String> vendors = dao.getVendors(userData.getCompanyId());
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
                Map<Integer, List<String>> vendors = dao.getVendorsByCategory(userData.getCompanyId());
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

    // Step 3
    private String getOfferDiscounts(HttpServletRequest req, UserData userData) {
        List<DiscountEntity> data = new ArrayList<>();
        String segmentId = req.getParameter("ruleId");
        if (!segmentId.isEmpty()) {
            try {
                data = dao.getOfferDiscounts(userData.getCompanyId(), Integer.parseInt(segmentId));
            } catch (SQLException e) {
                //TODO: some message?
                Util.logError("Failed to load item discounts", e, userData);
            }
        } else {
            //TODO: some message?
            Util.logError("Empty segment", userData);
        }

        return util.toJsonWithDataWrap(data);
    }

    private String getOffers(HttpServletRequest req, UserData userData) {
        List<ListEntity> offers = new ArrayList<>();
        String search = req.getParameter("search");
        if (search != null) {
            initOffers(userData);
            for (ListEntity i : userData.getOfferContainer()) {
                if (((String) i.getValue()).toLowerCase().contains(search.toLowerCase())) {
                    offers.add(i);
                }
            }
        }

        return Util.toJson(offers);
    }

    private void initOffers(UserData userData) {
        if (userData.getOfferContainer().isEmpty()) {
            try {
                List<ListEntity> offers = dao.getOffers(userData.getCompanyId());
                if (!offers.isEmpty()) {
                    userData.setOfferContainer(offers);
                } else {
                    Util.logError("No offers found", userData);
                }
            } catch (SQLException e) {
                Util.logError("Failed to load offers", e, userData);
            }
        }
    }
}
