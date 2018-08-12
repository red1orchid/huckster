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
import java.util.List;

/**
 * Created by PerevalovaMA on 26.10.2016.
 */
@WebServlet("/offer_discounts")
/*TODO: ajax only*/
public class OfferDiscountsServlet extends UserServlet implements JsonOutput{
    private WidgetSettingsDao dao = new WidgetSettingsDao();
    private Util util = new Util();

    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        String type = req.getParameter("type");
        if (type != null) {
            switch (type) {
                case "discounts":
                    writeJson(resp, getOfferDiscounts(userData));
                    break;
                case "offers":
                    writeObject(resp, getOffers(req, userData));
                    break;
            }
        }
    }

    @Override
    protected void initDataPost(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException {
        String type = req.getParameter("type");
        if (type != null) {
            OperationStatus status;
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

    private String getOfferDiscounts(UserData userData) {
        List<DiscountEntity> data = new ArrayList<>();
        try {
            data = dao.getOfferDiscounts(userData.getCompanyId());
        } catch (SQLException e) {
            //TODO: some message?
            Util.logError("Failed to load item discounts", e, userData);
        }

        return util.toJsonWithDataWrap(data);
    }

    private List<ListEntity> getOffers(HttpServletRequest req, UserData userData) {
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

        return offers;
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

    private boolean saveDiscount(HttpServletRequest req, UserData userData) {
        Integer id = stringToInt(req.getParameter("id"));
        Integer offerId = stringToInt(req.getParameter("offerId"));
        Integer step1 = stringToInt(req.getParameter("discount1"));
        Integer step2 = stringToInt(req.getParameter("discount2"));
        LocalDate startDate = Util.parseDate(req.getParameter("startDate"));
        LocalDate endDate = Util.parseDate(req.getParameter("endDate"));
        boolean success = false;

        if (offerId == null) {
            Util.logError("Failed to update offer discount: empty offerId", userData);
        } else if (id != null) {
            try {
                dao.updateOfferDiscount(id, userData.getCompanyId(), offerId, step1, step2, startDate, endDate);
                success = true;
            } catch (SQLException e) {
                Util.logError("Failed to update offer discount " + req.getParameter("id"), e, userData);
            }
        } else {
            try {
                dao.insertOfferDiscount(userData.getCompanyId(), offerId, step1, step2, startDate, endDate);
                success = true;
            } catch (SQLException e) {
                Util.logError("Failed to insert offer discount", e, userData);
            }
        }

        return success;
    }

    private boolean deleteDiscount(HttpServletRequest req, UserData userData) {
        Integer id = stringToInt(req.getParameter("id"));
        if (id != null) {
            try {
                dao.deleteOfferDiscount(Integer.parseInt(req.getParameter("id")), userData.getCompanyId());
                return true;
            } catch (SQLException e) {
                Util.logError("Failed to delete rule " + req.getParameter("id"), e, userData);
            }
        } else {
            Util.logError("Empty rule id", userData);
        }
        return false;
    }
}
