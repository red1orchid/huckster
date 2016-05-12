package huckster.cabinet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.sql.*;

import static huckster.cabinet.StatisticPanel.Type;

/**
 * Created by PerevalovaMA on 11.05.2016.
 */
public class DbHelper {
    private static final String DB_DRIVER = "oracle.jdbc.OracleDriver";
    private static final String DB_CONNECTION = "jdbc:oracle:thin:@//188.166.92.58:1521/orcldb";
    private static final String DB_USER = "huckster";
    private static final String DB_PASSWORD = "19643258";

    private DataSource pool;  // Database connection pool

    public DbHelper() throws ServletException {
        try {
            // Create a JNDI Initial context to be able to lookup the DataSource
            InitialContext ctx = new InitialContext();
            // Lookup the DataSource, which will be backed by a pool
            //   that the application server provides.
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/huckster");
            if (pool == null)
                throw new ServletException("Unknown DataSource 'jdbc/huckster'");
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }

    private static Connection getConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbConnection;
    }

    public boolean isUserExists(String username, String password) throws SQLException {
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

    public static int getCompanyId(String username) throws SQLException {
        try (Connection dbConnection = getConnection();
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

    public static String getCompanyName(int companyId) throws SQLException {
        try (Connection dbConnection = getConnection();
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

    public static String getRatePrc(Type incomeType, int companyId, String period) throws SQLException {
        String sql = null;
        switch (incomeType) {
            case INCOME:
                sql = "select (case when sign(r.value-rr.value) = 1 then '+' end) || trim(to_char(round((r.value-rr.value)/decode(rr.value,0,1,rr.value)*100, 2), '9990.99')) as value" +
                        "  from analitic.reports_data r" +
                        " inner join analitic.reports_data rr" +
                        "    on rr.report_id = r.report_id" +
                        "   and rr.company_id = r.company_id " +
                        "   and rr.interval = r.interval " +
                        " where r.report_id = 5 " +
                        "   and r.company_id = ? " +
                        "   and r.interval = decode(?, 'day', 'ddd', 'week', 'iw', 'month') " +
                        "   and r.metric_name = 'curr' " +
                        "   and rr.metric_name = 'last'";
                break;
            case ORDERS:
                break;
            case CONVERSION:
                break;
            case COVERING:
                break;
        }

        try (Connection dbConnection = getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, companyId);
            ps.setString(2, period);
            ResultSet rs = ps.executeQuery();

            String ratePrc = null;
            while (rs.next()) {
                ratePrc = rs.getString("value");
            }

            if (ratePrc != null) {
                return ratePrc;
            } else {
                throw new DataException("No appropriate data");
            }
        }
    }

    public static Double getRate(Type incomeType, int companyId, String period) throws SQLException {
        String sql = null;
        switch (incomeType) {
            case INCOME:
                sql = " select value from analitic.reports_data " +
                        " where report_id = 5 and company_id = ? " +
                        " and interval = decode(?, 'day', 'ddd', 'week', 'iw', 'month') " +
                        " and metric_name ='curr'";
                break;
            case ORDERS:
                break;
            case CONVERSION:
                break;
            case COVERING:
                break;
        }

        try (Connection dbConnection = getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, companyId);
            ps.setString(2, period);
            ResultSet rs = ps.executeQuery();

            Double rate = null;
            while (rs.next()) {
                rate = rs.getDouble("value");
            }

            if (rate != null) {
                return rate;
            } else {
                throw new DataException("No appropriate data");
            }
        }
    }
}