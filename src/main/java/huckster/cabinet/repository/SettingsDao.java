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
                "      WHERE id = ?";

        return selectValue(sql, null, (rs) ->
                        new CompanySettingsEntity(companyId, rs.getString("feed_url"), rs.getString("mailto"), rs.getString("mailto_admin"),
                                rs.getString("metric_key"), rs.getInt("is_manual_enable"))
                , companyId);
    }

    public void updateCompanySettings(int companyId, String yml, String orderEmails, String contactEmails, String yandexKey, int isActive) throws SQLException {
        String sql = "UPDATE v_companies" +
                "        SET feed_url         = ?," +
                "            mailto           = ?," +
                "            mailto_admin     = ?," +
                "            metric_key       = ?," +
                "            is_manual_enable = ?" +
                "      WHERE company_id = ?";

        executeUpdate(sql, yml, orderEmails, contactEmails, yandexKey, isActive, companyId);
    }

    public List<UrlEntity> getBlockedUrls(int companyId) throws SQLException {
        List<UrlEntity> list = new ArrayList<>();
        String sql = "SELECT id," +
                "            regexp_replace(url, '^(http://|https://)|(www.)|(\\\\?\\\\S+)|(/$)') AS url," +
                "            is_basket," +
                "            to_char(atime, 'DD.MM.YYYY') AS atime" +
                "       FROM block_pages" +
                "      WHERE company_id = ?" +
                "      ORDER BY ID DESC";

        execute(sql, null, (rs) -> list.add(new UrlEntity(rs.getInt("id"), rs.getString("url"), rs.getInt("is_basket"), rs.getString("atime")))
                , companyId);

        return list;
    }

    public void updateBlockedUrl(int companyId, int id, String url, int isBasket) throws SQLException {
        String sql = "UPDATE block_pages" +
                "        SET url = ?, " +
                "            is_basket = ?, " +
                "            atime = sysdate" +
                "      WHERE id = ? AND company_id = ?";

        executeUpdate(sql, url, isBasket, id, companyId);
    }

    public void insertBlockedUrl(int companyId, String url, int isBasket) throws SQLException {
        String sql = "INSERT INTO block_pages(company_id, url, is_basket)" +
                "     VALUES(?, ?, ?)";

        executeUpdate(sql, companyId, url, isBasket);
    }

    public void deleteBlockedUrl(int companyId, int id) throws SQLException {
        String sql = "DELETE FROM block_pages WHERE id = ? AND company_id = ?";

        executeUpdate(sql, id, companyId);
    }
}
