package huckster.cabinet.web;

import huckster.cabinet.OperationStatus;
import huckster.cabinet.Util;
import huckster.cabinet.model.DiscountEntity;
import huckster.cabinet.model.ListEntity;
import huckster.cabinet.repository.UserData;
import huckster.cabinet.repository.WidgetSettingsDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PerevalovaMA on 26.10.2016.
 */
@WebServlet("/vendor_discounts")
/*TODO: ajax only*/
public class VendorDiscounts extends UserServlet implements JsonOutput {
    private WidgetSettingsDao dao = new WidgetSettingsDao();
    private Util util = new Util();

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        String type = req.getParameter("type");
        if (type != null) {
            switch (type) {
                case "discounts":
                    writeJson(resp, getVendorDiscounts(userData));
                    break;
                case "vendors_categories":
                    writeObject(resp, getVendorsAndCategories(req, userData));
                    break;
                case "vendors":
                    writeObject(resp, getVendors(req, userData));
                    break;
            }
        }
    }

    @Override
    void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        String type = req.getParameter("type");
        if (type != null) {
            switch (type) {
                case "save": {
                    writeObject(resp, getOperationStatus(saveDiscount(req, userData), "Ошибка сохранения. Проверьте правильность вводимых данных"));
                }
                break;
                case "delete": {
                    //TODO: text?!
                    writeObject(resp, getOperationStatus(deleteDiscount(req, userData), "Ошибка удаления"));
                }
                break;
            }
        }
    }

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

    private Map<String, Object> getVendorsAndCategories(HttpServletRequest req, UserData userData) {
        Map<String, Object> map = new HashMap<>();
        initCategories(userData);
        map.put("categories", userData.getCategoryContainer());

        map.put("vendors", getVendors(req, userData));
        return map;
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

    private boolean saveDiscount(HttpServletRequest req, UserData userData) {
        boolean success = false;
        Integer id = stringToInt(req.getParameter("id"));
        Integer categoryId = stringToInt(req.getParameter("category"));
        String vendor = req.getParameter("vendors");
        Integer step1 = stringToInt(req.getParameter("discount1"));
        Integer step2 = stringToInt(req.getParameter("discount2"));
        Integer minPrice = stringToInt(req.getParameter("minPrice"));
        Integer maxPrice = stringToInt(req.getParameter("maxPrice"));
        LocalDate startDate = Util.parseDate(req.getParameter("startDate"));
        LocalDate endDate = Util.parseDate(req.getParameter("endDate"));

        if (id != null) {
            try {
                dao.updateVendorsDiscount(id, userData.getCompanyId(), categoryId, vendor, step1, step2, minPrice, maxPrice, startDate, endDate);
                success = true;
            } catch (SQLException e) {
                Util.logError("Failed to update vendor discount " + req.getParameter("id"), e, userData);
            }
        } else {
            try {
                dao.insertVendorsDiscount(userData.getCompanyId(), categoryId, vendor, step1, step2, minPrice, maxPrice, startDate, endDate);
                success = true;
            } catch (SQLException e) {
                Util.logError("Failed to insert new vendor discount", e, userData);
            }
        }
        return success;
    }

    private boolean deleteDiscount(HttpServletRequest req, UserData userData) {
        Integer id = stringToInt(req.getParameter("id"));
        if (id != null) {
            try {
                dao.deleteVendorsDiscount(Integer.parseInt(req.getParameter("id")), userData.getCompanyId());
                return true;
            } catch (SQLException e) {
                Util.logError("Failed to delete vendor discount " + req.getParameter("id"), e, userData);
            }
        } else {
            Util.logError("Empty vendor discount id", userData);
        }
        return false;
    }
}
