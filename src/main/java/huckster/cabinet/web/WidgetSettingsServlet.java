package huckster.cabinet.web;

import huckster.cabinet.DataException;
import huckster.cabinet.Util;
import huckster.cabinet.model.*;
import huckster.cabinet.repository.CompanyInfoDao;
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
    private CompanyInfoDao companyDao = new CompanyInfoDao();
    private Util util = new Util();

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
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
                        if (isNumber(req.getParameter("id"))) {
                            try {
                                dao.updateVendorsDiscount(Integer.parseInt(req.getParameter("id")), userData.getCompanyId(), categoryId, vendor, step1, step2, minPrice, maxPrice);
                            } catch (SQLException e) {
                                //TODO: error?
                                Util.logError("Failed to update vendor discount " + req.getParameter("id"), e, userData);
                            }
                        } else {
                            try {
                                dao.insertVendorsDiscount(userData.getCompanyId(), categoryId, vendor, step1, step2, minPrice, maxPrice);
                            } catch (SQLException e) {
                                //TODO: error?
                                Util.logError("Failed to insert new vendor discount", e, userData);
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
                        try {
                            RuleEntity rule = new RuleEntity(req.getParameter("channels"), Integer.parseInt(req.getParameter("devices")), req.getParameter("days"),
                                    Integer.parseInt(req.getParameter("hourFrom")), Integer.parseInt(req.getParameter("hourTo")), req.getParameter("tree"));
                            dao.updateRule(userData.getCompanyId(), rule);
                        } catch (NumberFormatException | SQLException e) {
                            //TODO: error?
                            Util.logError("Failed to update widget settings (rule)", e, userData);
                        }
                    }
                    break;
                    case "save_offer_discount": {
                        int offerId = Integer.parseInt(req.getParameter("offerId"));
                        Integer step1 = stringToInt(req.getParameter("discount1"));
                        Integer step2 = stringToInt(req.getParameter("discount2"));
                        if (isNumber(req.getParameter("id"))) {
                            try {
                                dao.updateOfferDiscount(Integer.parseInt(req.getParameter("id")), userData.getCompanyId(), offerId, step1, step2);
                            } catch (SQLException e) {
                                //TODO: error?
                                Util.logError("Failed to update offer discount " + req.getParameter("id"), e, userData);
                            }
                        } else {
                            try {
                                dao.insertOfferDiscount(userData.getCompanyId(), offerId, step1, step2);
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

    private String getAjaxData(String type, HttpServletRequest req, UserData userData) {
        String data = "";
        switch (type) {
            case "rule":
                data = getRule(userData);
                break;
            case "settings_tree":
                data = getTree(req, userData);
                break;
            case "channels":
                data = getChannels(userData);
                break;
            case "vendors_discounts":
                data = getVendorDiscounts(userData);
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
    private String getRule(UserData userData) {
        try {
            RuleEntity data = dao.getRule(userData.getCompanyId()).get();
            return util.toJsonWithDataWrap(data);
        } catch (SQLException | NoSuchElementException e) {
            Util.logError("Failed to load rule", e, userData);
            //TODO: fatal
            throw new DataException("Failed to load rule");
        }
    }

    private String getChannels(UserData userData) {
        List<String> channels = new ArrayList<>();

        try {
            channels = dao.getChannels(userData.getCompanyId());
        } catch (SQLException e) {
            //TODO: fatal?
            Util.logError("Failed to load channels", e, userData);
        }
        return Util.toJson(channels);
    }

    private String getTree(HttpServletRequest req, UserData userData) {
        Map<Integer, JsonTreeNode> map = new TreeMap<>();
        try {
            List<TreeEntity> selectedTreeEntities = dao.getTree(userData.getCompanyId());
            map = selectedTreeEntities.stream()
                    .collect(Collectors.toMap(TreeEntity::getId, v -> new JsonTreeNode(v.getId(), v.getTitle(), v.getParentId(), v.isSelected(), null)));
        } catch (SQLException e) {
            //TODO: kill modal?
            Util.logError("Failed to load tree", e, userData);
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

    // Vendor and categories discounts
    private String getVendorDiscounts(UserData userData) {
        List<DiscountEntity> data = new ArrayList<>();
        try {
            data = dao.getVendorsDiscounts(userData.getCompanyId());
        } catch (SQLException e) {
            //TODO: some message?
            Util.logError("Failed to load vendor discounts", e, userData);
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
        try {
            data = dao.getOfferDiscounts(userData.getCompanyId());
        } catch (SQLException e) {
            //TODO: some message?
            Util.logError("Failed to load item discounts", e, userData);
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
