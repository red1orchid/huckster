package huckster.cabinet.web;

import huckster.cabinet.Util;
import huckster.cabinet.model.DiscountEntity;
import huckster.cabinet.model.ListEntity;
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
import java.util.*;

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
                    case "step2_save_discount": {
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
                    case "step2_delete_discount": {
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
                        System.out.println("delete rule:" + req.getParameter("id"));
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
            case "step1_rules":
                data = getRules(userData);
                break;
            case "step2_discounts":
                data = getVendorDiscounts(req, userData);
                break;
            case "step2_all":
                data = getVendorCategories(req, userData);
                break;
            case "step2_vendors":
                data = Util.toJson(getVendors(req, userData));
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
}
