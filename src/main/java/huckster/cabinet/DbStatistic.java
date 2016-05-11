package huckster.cabinet;

import java.sql.*;

/**
 * Created by PerevalovaMA on 11.05.2016.
 */
public class DbStatistic {
    private static final String DB_DRIVER = "oracle.jdbc.OracleDriver";
    private static final String DB_CONNECTION = "jdbc:oracle:thin:@//188.166.92.58:1521/orcldb";
    private static final String DB_USER = "huckster";
    private static final String DB_PASSWORD = "19643258";

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

    public static boolean isUserExists(String username, String password) throws SQLException {
        boolean isUserExists = false;

        try (Connection dbConnection = getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(
                     "select count(*) as count from users_auth " +
                             "where upper(user_name) = upper(?) " +
                             "and password = sys.hash_md5(? || id || upper(user_name))"))

        {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getInt("count") > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return isUserExists;
    }
}