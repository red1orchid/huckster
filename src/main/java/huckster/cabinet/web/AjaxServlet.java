package huckster.cabinet.web;

import huckster.cabinet.Util;
import huckster.cabinet.model.*;
import huckster.cabinet.repository.UserData;
import huckster.cabinet.repository.WidgetSettingsDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by PerevalovaMA on 18.05.2016.
 */
@WebServlet("/ajax")
public class AjaxServlet extends HttpServlet {
    private WidgetSettingsDao dao = new WidgetSettingsDao();
    private Util util = new Util();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        UserData userData = (UserData) req.getSession().getAttribute("userData");
        String data = "";
        String type = req.getParameter("type");

        if (type != null) {
            switch (req.getParameter("type")) {
                case "settings_tree":
                    data = getTree(req, userData);
                    break;
                case "settings_lists":
                    data = getSettings(userData);
                    break;
                //TODO: very slow?
                case "offer_discounts":
                    data = getOfferDiscounts(req, userData);
                    break;
/*                case "vendor_offers":
                    data = Util.toJson(getVendors(req, userData));
                    break;*/
                case "step3_offers":
                    data = getOffers(req, userData);
                    break;
                case "step3_vendors":
                    data = getVendorOffers(userData);
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

    //Step 1
    private String getSettings(UserData userData) {
        HashMap<String, Object> map = new HashMap<>();
        List<String> channels = new ArrayList<>();
        Map<String, String> sources = new LinkedHashMap<>();

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
        String vendor = req.getParameter("vendor");
        if (vendor != null) {
            initOffers(userData);
            offers = userData.getOfferContainer().get(vendor);
        }

        return Util.toJson(offers);
    }

    private String getVendorOffers(UserData userData) {
        initVendorOffers(userData);
        List<String> vendors = userData.getVendorOfferContainer();

        return Util.toJson(vendors);
    }

    private void initOffers(UserData userData) {
        if (userData.getOfferContainer().isEmpty()) {
            try {
                Map<String, List<ListEntity>> offers = dao.getOffers(userData.getCompanyId());
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

    private void initVendorOffers(UserData userData) {
        if (userData.getVendorOfferContainer().isEmpty()) {
            try {
                List<String> vendors = dao.getVendorOffers(userData.getCompanyId());
                if (!vendors.isEmpty()) {
                    userData.setVendorOfferContainer(vendors);
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
                DiscountEntity discount = dao.getVendorsDiscount(Integer.parseInt(id)).orElseThrow(NoDataException::new);
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
            categories = dao.getCategories(userData.getCompanyId());
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
                vendors = dao.getVendors(userData.getCompanyId(), Integer.parseInt(categoryId));
            } else {
                vendors = dao.getVendors(userData.getCompanyId());
            }
        } catch (SQLException e) {
            //TODO: kill modal?
            Util.logError("Failed to load vendors", e, userData);
        }

        return vendors;
    }*/
}