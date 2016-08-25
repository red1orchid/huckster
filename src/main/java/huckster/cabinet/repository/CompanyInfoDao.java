package huckster.cabinet.repository;

import huckster.cabinet.model.CompanyEntity;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by PerevalovaMA on 04.08.2016.
 */
public class CompanyInfoDao extends DbDao {
    public boolean isUserExists(String username, String password) throws SQLException {
        String sql = "SELECT count(*) AS count FROM auth " +
                "      WHERE upper(user_name) = upper(?) " +
                "        AND password = sys.hash_md5(? || id || upper(user_name))";

        return selectValue(sql, null, (rs) -> rs.getInt("count") > 0, username, password).orElse(false);
    }

    public Optional<Integer> getCompanyId(String username) throws SQLException {
        return selectValue("SELECT company_id " +
                        "     FROM auth " +
                        "    WHERE upper(user_name) = upper(?)"
                , null, (rs) -> rs.getInt("company_id"), username);
    }

    public Optional<CompanyEntity> getCompanyInfo(int companyId) throws SQLException {
        return selectValue("SELECT name, price_cur " +
                        "     FROM companies " +
                        "    WHERE id = ?", null, (rs) -> new CompanyEntity(companyId, rs.getString("name"), rs.getString("price_cur"))
                , companyId);
    }
}
