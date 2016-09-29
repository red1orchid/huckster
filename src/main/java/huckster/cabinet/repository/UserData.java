package huckster.cabinet.repository;

import huckster.cabinet.model.ChartData;
import huckster.cabinet.model.CompanyEntity;
import huckster.cabinet.model.ListEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.ws.rs.NotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Perevalova Marina on 11.05.2016.
 */
public class UserData {
    private String period = "month";
    private String periodTraffic = "month";
    private String periodGoods = "month";
    private CompanyEntity companyInfo;
    private LocalDate startDate = LocalDate.now().minusDays(7);
    private LocalDate endDate = LocalDate.now();
    private Map<Integer, String> rateContainer = new HashMap<>();
    private Map<Integer, String> percentContainer = new HashMap<>();
    private Map<Integer, ChartData> chartContainer = new HashMap<>();
    private Map<Integer, List<String>> vendorCatContainer = new HashMap<>();
    private List<String> vendorContainer = new ArrayList<>();
    private List<String> vendorOfferContainer = new ArrayList<>();
    private List<ListEntity<Integer, String>> categoryContainer = new ArrayList<>();
    private List<ListEntity> offerContainer = new ArrayList<>();
    private static final Logger LOG = LoggerFactory.getLogger(UserData.class);
    private CompanyInfoDao dao = new CompanyInfoDao();

    public UserData(String user) throws ServletException {
        try {
            Integer companyId = dao.getCompanyId(user).orElseThrow(NotFoundException::new);
            companyInfo = dao.getCompanyInfo(companyId).orElseThrow(NotFoundException::new);
        } catch (SQLException | NotFoundException e) {
            LOG.error("Failed to load company info for user " + user, e);
            //TODO: fatal error, go to 500
        }
    }

    public String getPeriod() {
        return period;
    }

    public String getPeriodTraffic() {
        return periodTraffic;
    }

    public String getPeriodGoods() { return periodGoods; }

    public int getCompanyId() {
        return companyInfo.getId();
    }

    public String getCompanyName() {
        return companyInfo.getName();
    }

    public String getCurrency() {
        return companyInfo.getCurrency();
    }

    public Map<Integer, String> getRateContainer() {
        return rateContainer;
    }

    public Map<Integer, String> getPercentContainer() {
        return percentContainer;
    }

    public Map<Integer, ChartData> getChartContainer() {
        return chartContainer;
    }

    public List<ListEntity> getOfferContainer() {
        return offerContainer;
    }

    public List<String> getVendorOfferContainer() {
        return vendorOfferContainer;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Map<Integer, List<String>> getVendorCatContainer() { return vendorCatContainer; }

    public List<String> getVendorContainer() { return vendorContainer; }

    public List<ListEntity<Integer, String>> getCategoryContainer() { return categoryContainer; }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setPeriodGoods(String periodGoods) {
        this.periodGoods = periodGoods;
    }

    public void setPeriodTraffic(String periodTraffic) {
        this.periodTraffic = periodTraffic;
    }

    public void setRateContainer(Map<Integer, String> rateContainer) {
        this.rateContainer = rateContainer;
    }

    public void setPercentContainer(Map<Integer, String> percentContainer) {
        this.percentContainer = percentContainer;
    }

    public void setChartContainer(Map<Integer, ChartData> chartContainer) {
        this.chartContainer = chartContainer;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setVendorContainer(List<String> vendorContainer) {
        this.vendorContainer = vendorContainer;
    }

    public void setVendorOfferContainer(List<String> vendorOfferContainer) {
        this.vendorOfferContainer = vendorOfferContainer;
    }

    public void setVendorCatContainer(Map<Integer, List<String>> vendorCatContainer) {
        this.vendorCatContainer = vendorCatContainer;
    }

    public void setCategoryContainer(List<ListEntity<Integer, String>> categoryContainer) {
        this.categoryContainer = categoryContainer;
    }

    public void setOfferContainer(List<ListEntity> itemContainer) {
        this.offerContainer = itemContainer;
    }

    public void clear() {
        rateContainer.clear();
        percentContainer.clear();
        chartContainer.clear();
        vendorContainer.clear();
        vendorCatContainer.clear();
        categoryContainer.clear();
        offerContainer.clear();
    }
}