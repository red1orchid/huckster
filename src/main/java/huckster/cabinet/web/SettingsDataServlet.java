package huckster.cabinet.web;

import huckster.cabinet.DataException;
import huckster.cabinet.Util;
import huckster.cabinet.model.*;
import huckster.cabinet.repository.CompanyInfoDao;
import huckster.cabinet.repository.SettingsDao;
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
 * Created by PerevalovaMA on 25.10.2016.
 */
@WebServlet("/settings_data")
public class SettingsDataServlet extends UserServlet implements JsonOutput {
    private WidgetSettingsDao widgetDao = new WidgetSettingsDao();
    private CompanyInfoDao companyDao = new CompanyInfoDao();
    private Util util = new Util();

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        String type = req.getParameter("type");
        if (type != null && type.equals("widget_url")) {
            resp.getWriter().print(widgetDao.getUrl(userData.getCompanyId()).orElse(null));
        }
    }

    @Override
    void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        String type = req.getParameter("type");
        if (type != null) {
            writeJson(resp, getJson(type, req, userData));
        }
    }

    private String getJson(String type, HttpServletRequest req, UserData userData) {
        String data = "";
        switch (type) {
            case "save_password":
                data = savePassword(req, userData);
                break;
            case "rule":
                data = getRule(userData);
                break;
            case "settings_tree":
                data = getTree(userData);
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

        //  return Util.toJson(data);
    }

    private Integer stringToInt(String src) {
        if (src != null && !src.isEmpty())
            return Integer.parseInt(src);
        else
            return null;
    }

    //User settings
    private String savePassword(HttpServletRequest req, UserData userData) {
        Response data = null;
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");
        if (oldPassword != null) {
            try {
                if (companyDao.isPasswordCorrect(userData.getCompanyId(), oldPassword)) {
                    companyDao.setPassword(userData.getCompanyId(), newPassword);
                    data = new Response(true);
                } else {
                    data = new Response(false, "wrong_password");
                }
            } catch (SQLException e) {
                Util.logError("Failed to save password", e, userData);
            }
        } else {
            Util.logError("Empty password", userData);
        }

        if (data == null) {
            data = new Response(false, "unknown_error");
        }
        return Util.toJson(data);
    }

    // Geo
    private String getRule(UserData userData) {
        try {
            RuleEntity data = widgetDao.getRule(userData.getCompanyId()).get();
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
            channels = widgetDao.getChannels(userData.getCompanyId());
        } catch (SQLException e) {
            //TODO: fatal?
            Util.logError("Failed to load channels", e, userData);
        }
        return Util.toJson(channels);
    }

    private String getTree(UserData userData) {
        Map<Integer, JsonTreeNode> map = new TreeMap<>();
        try {
            List<TreeEntity> selectedTreeEntities = widgetDao.getTree(userData.getCompanyId());
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
            data = widgetDao.getVendorsDiscounts(userData.getCompanyId());
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
                List<ListEntity<Integer, String>> categories = widgetDao.getCategories(userData.getCompanyId());
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
                List<String> vendors = widgetDao.getVendors(userData.getCompanyId());
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
                Map<Integer, List<String>> vendors = widgetDao.getVendorsByCategory(userData.getCompanyId());
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
            data = widgetDao.getOfferDiscounts(userData.getCompanyId());
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
                List<ListEntity> offers = widgetDao.getOffers(userData.getCompanyId());
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
