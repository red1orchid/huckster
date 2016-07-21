package huckster.cabinet.repository;

import huckster.cabinet.Chart;
import huckster.cabinet.DataException;
import huckster.cabinet.StatisticPanel;
import huckster.cabinet.model.*;

import javax.servlet.ServletException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Perevalova Marina on 11.05.2016.
 */
public class UserData {
    private DbDao dao = new DbDao();
    private String username;
    private String companyName;
    private String currency;
    private int companyId;
    private String period = "month";
    private Map<Integer, String> rateContainer = new HashMap<>();
    private Map<Integer, String> prcContainer = new HashMap<>();
    private Map<Integer, ChartData> chartContainer = new HashMap<>();

    public UserData(String user) throws ServletException {
        this.username = user;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setUser(String username) {
        this.username = username;
        companyId = 0;
        companyName = null;
        currency = null;
        refreshData();
    }

    private void initRateContainer(String period) throws SQLException {
        if (rateContainer.isEmpty()) {
            rateContainer = dao.getStatisticRates(companyId, period);

            if (rateContainer.isEmpty()) {
                throw new DataException("No appropriate data");
            }
        }
    }

    private void initPrcContainer(String period) throws SQLException {
        if (prcContainer.isEmpty()) {
            prcContainer = dao.getStatisticPercents(companyId, period);

            if (prcContainer.isEmpty()) {
                throw new DataException("No appropriate data");
            }
        }
    }

    private void initChartContainer(String period) throws SQLException {
        if (chartContainer.isEmpty()) {
            List<TwoLineChartEntity> chartRawData = dao.getChartData(companyId, period);
            chartContainer = ChartData.makeData(chartRawData, ".current", ".last");
            chartContainer.values().stream()
                    .forEach(v -> v.setProperties("time", "linear", 0));

            if (chartContainer.isEmpty()) {
                throw new DataException("No appropriate data");
            }
        }
    }

    private void initCompanyInfo() throws SQLException {
        if (companyId == 0) {
            companyId = dao.getCompanyId(username).orElse(0);

            if (companyId == 0) {
                throw new DataException("Company not found for user " + username);
            }

            Optional<CompanyEntity> company = dao.getCompanyInfo(companyId);
            if (!company.isPresent()) {
                throw new DataException("Company " + companyId + " not exists");
            } else {
                companyName = company.get().getName();
                currency = company.get().getCurrency();
            }
        }
    }

    public void refreshData() {
        rateContainer.clear();
        prcContainer.clear();
        chartContainer.clear();
    }

    public String getCompanyName() throws SQLException {
        initCompanyInfo();
        return companyName;
    }

    public String getCurrency() throws SQLException {
        initCompanyInfo();
        return currency;
    }

    public String getRate(StatisticPanel.Type rateType, String contextType, String period) throws
            SQLException {
        int reportId = rateType.getReportId();

        if (contextType.equals("main")) {
            initRateContainer(period);
            return rateContainer.get(reportId);
        } else if (contextType.equals("footer")) {
            initPrcContainer(period);
            return prcContainer.get(reportId);
        } else {
            throw new DataException("Unsupported context type");
        }
    }

    public ChartData getChartData(Chart.Type type, String period) throws SQLException {
        int reportId = type.getReportId();

        initChartContainer(period);
        return chartContainer.get(reportId);
    }

    public List<List> getOrders(Date startDate, Date endDate) {
        try {
            return dao.getOrders(companyId, startDate, endDate);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<List> getGoods(String period) {
        try {
            return dao.getGoods(companyId, period);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<List> getTraffic(String period) {
        try {
            return dao.getTraffic(companyId, period);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<RuleEntity> getRules() {
        try {
            return dao.getRules(companyId);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Map<String, String> getYml() {
        try {
            return dao.getYml(companyId);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return new TreeMap<>();
        }
    }

    public Map<Integer, JsonTreeNode> getSelectedTree(String ruleId) {
        List<SelectedTreeEntity> selectedTreeEntities;
        try {
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

    public List<String> getChannels() {
        try {
            return dao.getChannels(companyId);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Map<String, String> getSources() {
        try {
            return dao.getSources(companyId);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return new TreeMap<>();
        }
    }
}