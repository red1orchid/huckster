package huckster.cabinet.repository;

import huckster.cabinet.model.DiscountEntity;
import huckster.cabinet.model.ListEntity;
import huckster.cabinet.model.RuleEntity;
import huckster.cabinet.model.TreeEntity;
import org.apache.commons.codec.language.bm.Rule;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by PerevalovaMA on 04.08.2016.
 */
public class WidgetSettingsDao extends DbDao {
    public Optional<String> getUrl(int companyId) throws SQLException {
        String sql = "SELECT 'http://' || t.url || '?utm_medium=' || t.utm_medium || chr(38) || 'utm_campaign=gold' AS url" +
                "       FROM (SELECT t.* FROM sync_offers_auto t ORDER BY DBMS_RANDOM.RANDOM) t" +
                "      WHERE rownum = 1" +
                "        AND company_id = ?";

        return selectValue(sql, (rs) -> rs.getString("url"), companyId);
    }

    public Optional<RuleEntity> getRule(int companyId) throws SQLException {
        String sql = "SELECT id AS empno," +
                "            utm_medium," +
                "            destination," +
                "            days," +
                "            start_hour," +
                "            end_hour," +
                "            geo" +
                "  FROM companies" +
                " WHERE id = ?";

        Map<Integer, String> devices = getDevices();
        return selectValue(sql, (rs) -> new RuleEntity(rs.getString("utm_medium"), rs.getInt("destination"),
                        devices.get(rs.getInt("destination")), rs.getString("days"), rs.getInt("start_hour"), rs.getInt("end_hour"), rs.getString("geo")), companyId);
    }

    public void updateRule(int companyId, RuleEntity rule) throws SQLException {
        String sql = "UPDATE companies" +
                "        SET geo         = ?," +
                "            utm_medium  = ?," +
                "            days        = ?," +
                "            destination = ?," +
                "            start_hour  = ?," +
                "            end_hour    = ?" +
                "      WHERE id = ?";

        executeUpdate(sql, rule.getGeo(), rule.getChannels(), rule.getDays(), rule.getDevices(), rule.getTimeFrom(), rule.getTimeTo(), companyId);
    }

    public Map<Integer, String> getDevices() {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "Все устройства");
        map.put(1, "ПК и ноутбуки");
        map.put(2, "Мобильные");

        return map;
    }

    public List<String> getChannels(int companyId) throws SQLException {
        String sql = "SELECT replace(c.name, 'all', null) AS name" +
                "       FROM cabinet_utm_medium c" +
                "       JOIN (SELECT id," +
                "                    row_number() over(partition by name order by company_id nulls first) r" +
                "               FROM cabinet_utm_medium c" +
                "              WHERE c.company_id = ?" +
                "                 OR c.company_id IS NULL" +
                "              ORDER BY name) n" +
                "         ON c.id = n.id" +
                "        AND n.r = 1" +
                "      ORDER BY rating desc";

        List<String> channels = new ArrayList<>();
        execute(sql, 100, (rs) -> channels.add(rs.getString("name")), companyId);
        return channels;
    }

    public List<TreeEntity> getTree(int companyId) throws SQLException {
        List<TreeEntity> list = new ArrayList<>();
        String sql = "SELECT t.id," +
                "            title," +
                "            parent_id," +
                "           (SELECT least(count(*), 1) FROM companies" +
                "             WHERE id = ?" +
                "               AND (geo LIKE '%:' || t.id || ':%' OR geo LIKE '%:' || t.id OR geo LIKE t.id || ':%')) AS is_selected " +
                "       FROM cidr_tree t";
        execute(sql, 1200,
                (rs) -> list.add(new TreeEntity(rs.getInt("id"), rs.getString("title"), rs.getInt("parent_id"), rs.getInt("is_selected") == 1))
                , companyId);

        return list;
    }

    public List<DiscountEntity> getVendorsDiscounts(int companyId) throws SQLException {
        List<DiscountEntity> discounts = new ArrayList<>();
        String sql = "SELECT id," +
                "            category_id," +
                "            NVL((SELECT category_id || ' - ' || NAME" +
                "                   FROM categories c" +
                "                  WHERE c.category_id = NVL(t.category_id, 0)" +
                "                    AND c.company_id = t.company_id" +
                "                    AND rownum = 1), '*все категории*') AS category," +
                "            NVL(vendors, '*все вендоры*') AS vendors," +
                "            step1," +
                "            step2," +
                "            min_price," +
                "            max_price" +
                "       FROM sync_discounts_cat_vndrs t" +
                "      WHERE company_id = ?" +
                "      ORDER BY category DESC, vendors DESC";

        execute(sql, 100, (rs) -> {
            discounts.add(new DiscountEntity(rs.getInt("id"), rs.getInt("category_id"), rs.getString("category"), rs.getString("vendors"), rs.getInt("min_price"), rs.getInt("max_price"),
                    rs.getInt("step1"), rs.getInt("step2")));
        }, companyId);

        return discounts;
    }

    public void insertVendorsDiscount(int companyId, Integer categoryId, String vendor, Integer step1, Integer step2, Integer minPrice, Integer maxPrice) throws SQLException {
        String sql = "INSERT INTO sync_discounts_cat_vndrs(company_id, category_id, vendors, step1, step2, min_price, max_price)" +
                "     VALUES(?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, companyId, categoryId, vendor, step1, step2, minPrice, maxPrice);
    }

    public void updateVendorsDiscount(int id, int companyId, Integer categoryId, String vendor, Integer step1, Integer step2, Integer minPrice, Integer maxPrice) throws SQLException {
        String sql = "UPDATE sync_discounts_cat_vndrs" +
                "        SET category_id = ?," +
                "            vendors     = ?," +
                "            step1       = ?," +
                "            step2       = ?," +
                "            min_price   = ?," +
                "            max_price   = ?," +
                "            atime       = sysdate" +
                "      WHERE id = ? AND company_id = ?";

        executeUpdate(sql, categoryId, vendor, step1, step2, minPrice, maxPrice, id, companyId);
    }

    public void deleteVendorsDiscount(int id, int companyId) throws SQLException {
        String sql = "DELETE FROM sync_discounts_cat_vndrs WHERE id = ? and company_id = ?";

        executeUpdate(sql, id, companyId);
    }

    public List<ListEntity<Integer, String>> getCategories(int companyId) throws SQLException {
        List<ListEntity<Integer, String>> list = new ArrayList<>();
        String sql = "SELECT c.category_id || ' - ' || c.name AS display_value, " +
                "            c.category_id AS return_value" +
                "       FROM categories c" +
                "      WHERE c.company_id = ?" +
                "        AND c.name IS NOT NULL" +
                "      ORDER BY c.name";

        execute(sql, 500, (rs) -> list.add(new ListEntity<>(rs.getInt("return_value"), rs.getString("display_value"))), companyId);
        return list;
    }

    public Map<Integer, List<String>> getVendorsByCategory(int companyId) throws SQLException {
        Map<Integer, List<String>> map = new HashMap<>();
        String sql = "SELECT DISTINCT vendor, category_id" +
                "       FROM offers" +
                "      WHERE company_id = ?" +
                "        AND vendor IS NOT NULL" +
                "      ORDER BY 1";

        execute(sql, 1000, (rs) -> {
            map.putIfAbsent(rs.getInt("category_id"), new ArrayList<>());
            map.get(rs.getInt("category_id")).add(rs.getString("vendor"));
        }, companyId);
        return map;
    }

    public List<String> getVendors(int companyId) throws SQLException {
        List<String> vendors = new ArrayList<>();
        String sql = "SELECT DISTINCT vendor" +
                "       FROM offers" +
                "      WHERE company_id = ?" +
                "        AND vendor IS NOT NULL" +
                "      ORDER BY 1";

        execute(sql, 500, (rs) -> {
            vendors.add(rs.getString("vendor"));
        }, companyId);

        return vendors;
    }

    public List<DiscountEntity> getOfferDiscounts(int companyId) throws SQLException {
        List<DiscountEntity> discounts = new ArrayList<>();
        String sql = "SELECT d.id," +
                "            d.offer_id," +
                "            f.name," +
                "            d.step1," +
                "            d.step2," +
                "            f.url," +
                "            d.atime" +
                "       FROM sync_discounts_offers d" +
                "      INNER JOIN offers f" +
                "         ON f.company_id = d.company_id" +
                "        AND f.offer_id = d.offer_id" +
                "      WHERE d.company_id = ?" +
                "      ORDER BY d.atime DESC";

        execute(sql, 100, (rs) -> {
            discounts.add(new DiscountEntity(rs.getInt("id"), rs.getString("offer_id"), rs.getString("name"), rs.getInt("step1"), rs.getInt("step2"), rs.getString("url")));
        }, companyId);

        return discounts;
    }

    public void insertOfferDiscount(int companyId, int offerId, Integer step1, Integer step2) throws SQLException {
        String sql = "INSERT INTO sync_discounts_offers(company_id, offer_id, step1, step2)" +
                "     VALUES(?, ?, ?, ?)";

        executeUpdate(sql, companyId, offerId, step1, step2);
    }

    public void updateOfferDiscount(int id, int companyId, int offerId, Integer step1, Integer step2) throws SQLException {
        String sql = "UPDATE sync_discounts_offers" +
                "        SET offer_id = ?, step1 = ?, step2 = ?" +
                "      WHERE id = ?" +
                "        AND company_id = ?";

        executeUpdate(sql, offerId, step1, step2, id, companyId);
    }

    public void deleteOfferDiscount(int id, int companyId) throws SQLException {
        String sql = "DELETE FROM sync_discounts_offers WHERE id = ? and company_id = ?";

        executeUpdate(sql, id, companyId);
    }

    public List<ListEntity> getOffers(int companyId) throws SQLException {
        List<ListEntity> list = new ArrayList<>();
        String sql = " SELECT offer_id || ' - ' || name as name, offer_id" +
                "        FROM offers" +
                "       WHERE company_id = ?" +
                "       ORDER BY name";

        execute(sql, 10000, (rs) ->
                        list.add(new ListEntity<>(rs.getInt("offer_id"), rs.getString("name")))
                , companyId);

        return list;
    }
}
