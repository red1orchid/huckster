package huckster.cabinet.repository;

import huckster.cabinet.model.CompanySettingsEntity;
import huckster.cabinet.model.UrlEntity;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by PerevalovaMA on 12.08.2016.
 */
public class SettingsDao extends DbDao {
    public Set<Integer> getSynchronizingCompanies() throws SQLException {
        Set<Integer> companies = new HashSet<>();
        String sql = "SELECT DISTINCT company_id " +
                "       FROM sync_queue " +
                "      WHERE type = 1 " +
                "       AND srv_oper_time is null" +
                "       AND is_user = 1";
        execute(sql, null, (rs) -> companies.add(rs.getInt(1)));
        return companies;
    }

    public Optional<CompanySettingsEntity> getCompanySettings(int companyId) throws SQLException {
        String sql = "SELECT feed_url, mailto, mailto_admin, metric_key, work_mode" +
                "       FROM companies" +
                "      WHERE id = ?";

        return selectValue(sql, (rs) ->
                        new CompanySettingsEntity(companyId, rs.getString("feed_url"), rs.getString("mailto"), rs.getString("mailto_admin"),
                                rs.getString("metric_key"), rs.getString("work_mode"))
                , companyId);
    }

    public void updateCompanySettings(int companyId, String yml, String orderEmails, String contactEmails, String yandexKey, String workMode) throws SQLException {
        String sql = "UPDATE companies" +
                "        SET feed_url         = ?," +
                "            mailto           = ?," +
                "            mailto_admin     = ?," +
                "            metric_key       = ?," +
                "            work_mode = ?" +
                "      WHERE id = ?";

        executeUpdate(sql, yml, orderEmails, contactEmails, yandexKey, workMode, companyId);
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

    public boolean isAutoMode(int companyId) throws SQLException {
        return selectValue("SELECT is_auto_mode " +
                        "     FROM companies " +
                        "    WHERE id = ?", (rs) -> rs.getInt("is_auto_mode") == 1
                , companyId).orElse(false);
    }

    public void setAutoMode(int companyId, boolean isAutoMode) throws SQLException {
        executeUpdate("UPDATE companies " +
                "      SET is_auto_mode = ?" +
                "    WHERE id = ?", isAutoMode ? 1 : 0, companyId);
    }

    public boolean isScriptInstalled(int companyId) throws SQLException {
        return selectValue("SELECT COUNT(*) FROM companies" +
                        "    WHERE id = ?" +
                        "      AND atime < sysdate - 1", (rs) -> rs.getInt(1) == 0
                , companyId).orElse(false);
    }
}
