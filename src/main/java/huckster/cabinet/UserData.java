package huckster.cabinet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Perevalova Marina on 11.05.2016.
 */
class UserData {
    private DataSource pool;  // Database connection pool
    private String username;
    private String companyName;
    private String currency;
    private int companyId = 0;
    private String period = "month";
    private HashMap<Integer, String> rateContainer = new HashMap<>();
    private HashMap<Integer, String> prcContainer = new HashMap<>();
    private HashMap<Integer, ChartData> chartContainer = new HashMap<>();

    UserData(String user) throws ServletException {
        System.out.println("new userdata!!");
        this.username = user;
        try {
            InitialContext ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/huckster");
            if (pool == null)
                throw new ServletException("Unknown DataSource 'jdbc/huckster'");
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }

/*    private ResultSet getResult(String sql, int companyId, String period) throws SQLException {
        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, companyId);
            ps.setString(2, period);
            ResultSet rs = ps.executeQuery();

            return rs;
        }
    }*/

    public void setPeriod(String period) {
        this.period = period;
        System.out.println("set period " + period);
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
            String sql = " SELECT report_id, " +
                    "             CASE" +
                    "               WHEN report_id IN (4, 7) THEN to_char(value, 'fm90.00')" +
                    "               ELSE value" +
                    "             END AS value" +
                    "   FROM analitic.reports_data" +
                    "  WHERE report_id IN (4, 5, 6, 7)" +
                    "    AND company_id = ?" +
                    "    AND decode(interval, 'ddd', 'day', 'iw', 'week', interval) = ?" +
                    "    AND metric_name = 'curr'";

            try (Connection dbConnection = pool.getConnection();
                 PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setInt(1, companyId);
                ps.setString(2, period);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    rateContainer.put(rs.getInt("report_id"), rs.getString("value"));
                }

                if (rateContainer.isEmpty()) {
                    throw new DataException("No appropriate data");
                }
            }
        }
    }

    private void initPrcContainer(String period) throws SQLException {
        if (prcContainer.isEmpty()) {
            String sql = "SELECT r.report_id, (CASE WHEN sign(r.value-rr.value) = 1 THEN '+' END) || " +
                    "                       trim(to_char(round((r.value-rr.value)/decode(rr.value,0,1,rr.value)*100, 2), '9990.99')) AS value" +
                    "  FROM analitic.reports_data r" +
                    " INNER JOIN analitic.reports_data rr" +
                    "    ON rr.report_id = r.report_id" +
                    "   AND rr.company_id = r.company_id " +
                    "   AND rr.interval = r.interval " +
                    " WHERE r.report_id IN (4, 5, 6, 7) " +
                    "   AND r.company_id = ? " +
                    "   AND r.interval = decode(?, 'day', 'ddd', 'week', 'iw', 'month') " +
                    "   AND r.metric_name = 'curr' " +
                    "   AND rr.metric_name = 'last'";

            try (Connection dbConnection = pool.getConnection();
                 PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setInt(1, companyId);
                ps.setString(2, period);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    prcContainer.put(rs.getInt("report_id"), rs.getString("value"));
                }

                if (prcContainer.isEmpty()) {
                    throw new DataException("No appropriate data");
                }
            }
        }
    }

    private void initChartContainer(String period) throws SQLException {
        if (chartContainer.isEmpty()) {
            String sql = "SELECT * " +
                    "FROM (" +
                    "  SELECT report_id, metric_name, period, CASE report_id" +
                    "                                            WHEN 3 THEN value*1000" +
                    "                                            ELSE to_number(value)" +
                    "                                         END AS value" +
                    "  FROM analitic.reports_data" +
                    "  WHERE report_id IN (1, 2, 3)" +
                    "        AND company_id = ?" +
                    "        AND interval = ?)" +
                    "  PIVOT (" +
                    "    sum(value)" +
                    "    FOR metric_name IN ('curr' AS curr, 'last' AS last))" +
                    "ORDER BY report_id";

            try (Connection dbConnection = pool.getConnection();
                 PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setInt(1, companyId);
                ps.setString(2, period);
                ResultSet rs = ps.executeQuery();

                ChartData chartData = new ChartData("time", "linear", 0);
                ChartLine current = new ChartLine(".current");
                ChartLine last = new ChartLine(".last");
                int reportId = 0;

                while (rs.next()) {
                    if (rs.getInt("report_id") != reportId) {
                        if (reportId != 0) {
                            chartData.addLine(current);
                            chartData.addLine(last);
                            chartContainer.put(reportId, chartData);
                        }
                        chartData = new ChartData("time", "linear", 0);
                        current = new ChartLine(".current");
                        last = new ChartLine(".last");
                    }
                    current.addPoint(new ChartPoint(rs.getString("period"), rs.getInt("curr")));
                    last.addPoint(new ChartPoint(rs.getString("period"), rs.getInt("last")));
                    reportId = rs.getInt("report_id");
                }
                chartData.addLine(current);
                chartData.addLine(last);
                chartContainer.put(reportId, chartData);

                if (chartContainer.isEmpty()) {
                    throw new DataException("No appropriate data");
                }
            }
        }
    }

    private void initCompanyInfo() throws SQLException {
        if (companyId == 0) {
            try (Connection dbConnection = pool.getConnection();
                 PreparedStatement ps = dbConnection.prepareStatement("SELECT company FROM users_auth WHERE upper(user_name) = upper(?)")) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    companyId = rs.getInt("company");
                }
                if (companyId == 0) {
                    throw new DataException("Company not found for user " + username);
                }
            }

            try (Connection dbConnection = pool.getConnection();
                 PreparedStatement ps = dbConnection.prepareStatement("SELECT head_name, price_cur FROM companies WHERE company_id = ?")) {
                ps.setInt(1, companyId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    companyName = rs.getString("head_name");
                    currency = rs.getString("price_cur");
                }
                if (companyName == null) {
                    throw new DataException("Company " + companyId + " not exists");
                }
            }
        }
    }

    void refreshData() {
        rateContainer.clear();
        prcContainer.clear();
        chartContainer.clear();
    }

    boolean isUserExists(String password) throws SQLException {
        String sql = "SELECT count(*) AS count FROM users_auth " +
                "WHERE upper(user_name) = upper(?) " +
                "AND password = sys.hash_md5(? || id || upper(user_name))";

        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getInt("count") > 0) {
                    return true;
                }
            }
            return false;
        }
    }

    public String getPeriod() {
        return period;
    }

    String getCompanyName() throws SQLException {
        initCompanyInfo();
        return companyName;
    }

    String getCurrency() throws SQLException {
        initCompanyInfo();
        return currency;
    }

    String getRate(StatisticPanel.Type rateType, String contextType, String period) throws
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

    ChartData getChartData(Chart.Type type, String period) throws SQLException {
        int reportId = type.getReportId();

        initChartContainer(period);
        return chartContainer.get(reportId);
    }
}