package huckster.cabinet.repository;

import huckster.cabinet.model.CompanyEntity;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by PerevalovaMA on 04.08.2016.
 */
public class CompanyInfoDao extends DbDao {
    public boolean isUserExists(String username, String password) throws SQLException {
        String sql = "SELECT count(*) AS count FROM users_auth " +
                "      WHERE upper(user_name) = upper(?) " +
                "        AND password = sys.hash_md5(? || id || upper(user_name))";

        return selectValue(sql, null, (rs) -> rs.getInt("count") > 0, username, password).orElse(false);
    }

    public Optional<Integer> getCompanyId(String username) throws SQLException {
        return selectValue("SELECT company " +
                        "     FROM users_auth " +
                        "    WHERE upper(user_name) = upper(?)"
                , null, (rs) -> rs.getInt("company"), username);
    }

    public Optional<CompanyEntity> getCompanyInfo(int companyId) throws SQLException {
        return selectValue("SELECT head_name, price_cur " +
                        "             FROM companies " +
                        "            WHERE company_id = ?", null, (rs) -> new CompanyEntity(companyId, rs.getString("head_name"), rs.getString("price_cur"))
                , companyId);
    }
}
