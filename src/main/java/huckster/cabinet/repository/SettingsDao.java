package huckster.cabinet.repository;

import huckster.cabinet.model.CompanySettingsEntity;
import huckster.cabinet.model.UrlEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public List<UrlEntity> getBlockedUrls(int companyId) throws SQLException {
        List<UrlEntity> list = new ArrayList<>();
        String sql = "SELECT id," +
                "            regexp_replace(url, '^(http://|https://)|(www.)|(\\\\?\\\\S+)|(/$)') AS url," +
                "            is_basket," +
                "            to_char(ctime, 'DD.MM.YYYY') AS ctime" +
                "       FROM analitic.companies_pages_blocked" +
                "      WHERE company_id = ?" +
                "      ORDER BY ID DESC";

        execute(sql, null, (rs) -> list.add(new UrlEntity(rs.getInt("id"), rs.getString("url"), rs.getInt("is_basket"), rs.getString("ctime")))
                , companyId);

        return list;
    }
}
