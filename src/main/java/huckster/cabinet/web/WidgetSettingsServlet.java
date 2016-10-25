package huckster.cabinet.web;

import huckster.cabinet.Util;
import huckster.cabinet.model.RuleEntity;
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

/**
 * Created by PerevalovaMA on 30.05.2016.
 */
@WebServlet("/widget_settings")
public class WidgetSettingsServlet extends UserServlet implements JsonOutput {
    private static final Logger LOG = LoggerFactory.getLogger(WidgetSettingsServlet.class);
    private WidgetSettingsDao dao = new WidgetSettingsDao();

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
    }

    @Override
    protected void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        String type = req.getParameter("type");
        if (type != null) {
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

    private Integer stringToInt(String src) {
        if (src != null && !src.isEmpty())
            return Integer.parseInt(src);
        else
            return null;
    }

    private boolean isNumber(String src) {
        return (src != null && !src.isEmpty());
    }
}
