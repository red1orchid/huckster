package huckster.cabinet.repository;

import huckster.cabinet.model.CompanyEntity;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;

/**
 * Created by PerevalovaMA on 04.08.2016.
 */
public class CompanyInfoDao extends DbDao {
    public boolean isUserExists(String username, String password) throws SQLException {
        String sql = "SELECT count(*) AS count FROM auth " +
                "      WHERE upper(user_name) = upper(?) " +
                "        AND password = sys.hash_md5(? || id || upper(user_name))";

        return selectValue(sql, (rs) -> rs.getInt("count") > 0, username, password).orElse(false);
    }

    public boolean isPasswordCorrect(int companyId, String password) throws SQLException {
        String sql = "SELECT count(*) AS count FROM auth " +
                "      WHERE company_id = ? " +
                "        AND password = sys.hash_md5(? || id || upper(user_name))";

        return selectValue(sql, (rs) -> rs.getInt("count") > 0, companyId, password).orElse(false);
    }

    public void setPassword(int companyId, String password) throws SQLException {
        String sql = "UPDATE auth" +
                "        SET password = sys.hash_md5(? || id || upper(user_name))" +
                "      WHERE company_id = ? AND user_name <> company_id || 'ADMIN'";

        executeUpdate(sql, password, companyId);
    }

    public boolean isEmailExists(String email) throws SQLException {
        return (selectValue("SELECT count(*) FROM auth WHERE UPPER(user_name) = UPPER(?)", (rs) -> rs.getInt(1), email).orElse(0) > 0);
    }

    public boolean restorePassword(String email) throws SQLException {
        try (Connection dbConnection = pool.getConnection();
             CallableStatement cs = dbConnection.prepareCall("{? = call CHANGEPASS(?)}")) {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, email);
            cs.execute();
            return (cs.getInt(1) == 1);
        }
    }

    public Optional<Integer> getCompanyId(String username) throws SQLException {
        return selectValue("SELECT company_id " +
                        "     FROM auth " +
                        "    WHERE upper(user_name) = upper(?)"
                , (rs) -> rs.getInt("company_id"), username);
    }

    public Optional<CompanyEntity> getCompanyInfo(int companyId) throws SQLException {
        return selectValue("SELECT name, price_cur " +
                        "     FROM companies " +
                        "    WHERE id = ?", (rs) -> new CompanyEntity(companyId, rs.getString("name"), rs.getString("price_cur"))
                , companyId);
    }

    public boolean isWidgetActive(int companyId) throws SQLException {
        return (selectValue("SELECT is_widget_active" +
                        "     FROM companies " +
                        "    WHERE id = ?", (rs) -> rs.getInt("is_widget_active")
                , companyId).orElse(0) == 1);
    }

    public Optional<CompanyEntity> getCompanyInfoByToken(String token) throws SQLException {
        return selectValue("SELECT id, name, price_cur " +
                        "     FROM companies " +
                        "    WHERE reg_token = ?", (rs) -> new CompanyEntity(rs.getInt("id"), rs.getString("name"), rs.getString("price_cur"))
                , token);
    }

    public void deleteToken(int companyId) throws SQLException {
        executeUpdate("UPDATE companies " +
                "         SET reg_token = null" +
                "       WHERE id = ?", companyId);
    }
}
