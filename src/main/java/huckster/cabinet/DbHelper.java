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

import static org.eclipse.jdt.internal.compiler.ast.ASTNode.Unchecked;

/**
 * Created by Perevalova Marina on 11.05.2016.
 */
class DbHelper {
    private DataSource pool;  // Database connection pool
    private HashMap<Integer, String> rateContainer = new HashMap<>();
    private HashMap<Integer, String> prcContainer = new HashMap<>();
    private HashMap<Integer, ChartData> chartContainer = new HashMap<>();

    DbHelper() throws ServletException {
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

    private void initRateContainer(int companyId, String period) throws SQLException {
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

    private void initPrcContainer(int companyId, String period) throws SQLException {
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

    private void initChartContainer(int companyId, String period) throws SQLException {
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

                ChartData chartData = new ChartData("time", "linear");
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
                        chartData = new ChartData("time", "linear");
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

    void refreshData() {
        rateContainer.clear();
        prcContainer.clear();
        chartContainer.clear();
    }

    boolean isUserExists(String username, String password) throws SQLException {
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

    int getCompanyId(String username) throws SQLException {
        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement("SELECT company FROM users_auth WHERE upper(user_name) = upper(?)")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            int companyId = -1;
            while (rs.next()) {
                companyId = rs.getInt("company");
            }

            if (companyId != -1) {
                return companyId;
            } else {
                throw new DataException("Company not found for user " + username);
            }
        }
    }

    String getCompanyName(int companyId) throws SQLException {
        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement("SELECT head_name FROM companies WHERE company_id = ?")) {
            ps.setInt(1, companyId);
            ResultSet rs = ps.executeQuery();

            String companyName = null;
            while (rs.next()) {
                companyName = rs.getString("head_name");
            }

            if (companyName != null) {
                return companyName;
            } else {
                throw new DataException("Company " + companyId + " not exists");
            }
        }
    }

    String getRate(StatisticPanel.Type rateType, String contextType, int companyId, String period) throws
            SQLException {
        int reportId = rateType.getReportId();

        if (contextType.equals("main")) {
            initRateContainer(companyId, period);
            return rateContainer.get(reportId);
        } else if (contextType.equals("footer")) {
            initPrcContainer(companyId, period);
            return prcContainer.get(reportId);
        } else {
            throw new DataException("Unsupported context type");
        }
    }

    ChartData getChartData(Chart.Type type, int companyId, String period) throws SQLException {
        int reportId = type.getReportId();

        initChartContainer(companyId, period);
        return chartContainer.get(reportId);
    }
}