package huckster.cabinet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
public class DbData {
    private DataSource pool;

    DbData() throws ServletException {
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

    void updateOrder(int orderId, int status, String comment) throws SQLException {
        String sql = " update analitic.orders_header" +
                "         set processing_status = ?," +
                "             processing_comment = ?" +
                "       where remote_id = ?";

        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setString(2, comment);
            ps.setInt(3, orderId);
            ps.executeQuery();
        }
    }
}
