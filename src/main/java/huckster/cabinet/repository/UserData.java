package huckster.cabinet.repository;

import huckster.cabinet.Util;
import huckster.cabinet.model.ChartData;
import huckster.cabinet.model.CompanyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.ws.rs.NotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
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
    /*

        public void setUser(String username) {
            this.username = username;
            companyId = 0;
            companyName = null;
            currency = null;
            clear();
        }
    */

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

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

    public void clear() {
        rateContainer.clear();
        percentContainer.clear();
        chartContainer.clear();
    }
/*


    public Map<Integer, JsonTreeNode> getSelectedTree(String ruleId) {
        List<SelectedTreeEntity> selectedTreeEntities;
        try {
            WidgetSettingsDao dao = new WidgetSettingsDao();
            if (ruleId != null) {
                selectedTreeEntities = dao.getSelectedTree(Integer.parseInt(ruleId));
            } else {
                selectedTreeEntities = dao.getSelectedTree();
            }
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return new TreeMap<>();
        }

        return selectedTreeEntities.stream()
                .collect(Collectors.toMap(SelectedTreeEntity::getId, v -> new JsonTreeNode(v.getId(), v.getTitle(), v.getParentId(), v.isSelected(), null)));
    }
*/
}