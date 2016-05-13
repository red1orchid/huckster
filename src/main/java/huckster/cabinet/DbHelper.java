package huckster.cabinet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static huckster.cabinet.StatisticPanel.Type;

/**
 * Created by PerevalovaMA on 11.05.2016.
 */
public class DbHelper {
    private DataSource pool;  // Database connection pool

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

    boolean isUserExists(String username, String password) throws SQLException {
        String sql = "select count(*) as count from users_auth " +
                "where upper(user_name) = upper(?) " +
                "and password = sys.hash_md5(? || id || upper(user_name))";
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
             PreparedStatement ps = dbConnection.prepareStatement("select company from users_auth where upper(user_name) = upper(?)")) {
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
             PreparedStatement ps = dbConnection.prepareStatement("select head_name from companies where company_id = ?")) {
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

    String getRate(Type rateType, String contextType, int companyId, String period) throws SQLException {
        String sql;
        if (contextType.equals("main")) {
            sql = " select case" +
                    "         when report_id in (4, 7) then to_char(value, 'fm90.00')" +
                    "         else value" +
                    "       end as value" +
                    "   from analitic.reports_data" +
                    "  where report_id = ?" +
                    "    and company_id = ?" +
                    "    and interval = decode(?, 'day', 'ddd', 'week', 'iw', 'month')" +
                    "    and metric_name = 'curr'";
        } else if (contextType.equals("footer")) {
            sql = "select (case when sign(r.value-rr.value) = 1 then '+' end) || trim(to_char(round((r.value-rr.value)/decode(rr.value,0,1,rr.value)*100, 2), '9990.99')) as value" +
                    "  from analitic.reports_data r" +
                    " inner join analitic.reports_data rr" +
                    "    on rr.report_id = r.report_id" +
                    "   and rr.company_id = r.company_id " +
                    "   and rr.interval = r.interval " +
                    " where r.report_id = ? " +
                    "   and r.company_id = ? " +
                    "   and r.interval = decode(?, 'day', 'ddd', 'week', 'iw', 'month') " +
                    "   and r.metric_name = 'curr' " +
                    "   and rr.metric_name = 'last'";
        } else {
            throw new DataException("Unsupported context type");
        }
        int reportId;

        switch (rateType) {
            case INCOME:
                reportId = 5;
                break;
            case ORDERS:
                reportId = 6;
                break;
            case CONVERSION:
                reportId = 4;
                break;
            case COVERING:
                reportId = 7;
                break;
            default:
                throw new DataException("Unsupported rate type");
        }

        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, reportId);
            ps.setInt(2, companyId);
            ps.setString(3, period);
            ResultSet rs = ps.executeQuery();

            String rate = null;
            while (rs.next()) {
                rate = rs.getString("value");
            }

            if (rate != null) {
                return rate;
            } else {
                throw new DataException("No appropriate data");
            }
        }
    }

    ChartData getChartData(int companyId, String period, int reportId) throws SQLException {
        String sql = "select * from (" +
                "        select metric_name, period, value" +
                "          from analitic.reports_data" +
                "         where company_id = ?" +
                "           and interval = ?" +
                "           and report_id = ?)" +
                " pivot(sum(value)" +
                "   for metric_name in('curr' as curr, 'last' as last))" +
                " order by 1";

        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, companyId);
            ps.setString(2, period);
            ps.setInt(3, reportId);
            ResultSet rs = ps.executeQuery();

          // ArrayList<ChartPoint2> charData = new ArrayList<>();
            ChartData chartData = new ChartData("time", "linear");
            ChartLine current = new ChartLine(".current");
            ChartLine last = new ChartLine(".last");
            while (rs.next()) {
                current.addPoint(new ChartPoint(rs.getString("period"), rs.getInt("curr")));
                last.addPoint(new ChartPoint(rs.getString("period"), rs.getInt("last")));
            }
            chartData.addLine(current);
            chartData.addLine(last);

            return chartData;
        }
    }
}