package huckster.cabinet.repository;

import huckster.cabinet.model.CompanySettingsEntity;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by PerevalovaMA on 12.08.2016.
 */
public class SettingsDao extends DbDao {
    public Optional<CompanySettingsEntity> getCompanySettings(int companyId) throws SQLException {
        String sql = "SELECT feed_url, mailto, mailto_admin, metric_key, is_manual_enable" +
                "       FROM companies" +
                "      WHERE company_id = ?";

        return selectValue(sql, null, (rs) ->
                        new CompanySettingsEntity(companyId, rs.getString("feed_url"), rs.getString("mailto"), rs.getString("mailto_admin"),
                                rs.getString("metric_key"), rs.getInt("is_manual_enable"))
                , companyId);
    }
}
