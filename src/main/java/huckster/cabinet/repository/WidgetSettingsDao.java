package huckster.cabinet.repository;

import huckster.cabinet.model.DiscountEntity;
import huckster.cabinet.model.ListEntity;
import huckster.cabinet.model.RuleEntity;
import huckster.cabinet.model.SelectedTreeEntity;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by PerevalovaMA on 04.08.2016.
 */
public class WidgetSettingsDao extends DbDao {
    public List<RuleEntity> getRules(int companyId) throws SQLException {
        List<RuleEntity> list = new ArrayList<>();
        String sql = "SELECT r.id AS empno," +
                "            replace(nvl(r.utm_medium,'все'), 'all', 'все') AS utm_medium," +
                "            replace(nvl(r.utm_source,'все'), 'all', 'все') AS utm_source," +
                "            r.destination," +
                "            r.days," +
                "            r.start_hour," +
                "            r.end_hour" +
                "  FROM sync_rules r" +
                " WHERE r.company_id = ?" +
                " ORDER BY utm_medium desc, utm_source DESC, id ASC";

        Map<Integer, String> devices = getDevices();
        execute(sql, null,
                (rs) -> list.add(new RuleEntity(rs.getInt("empno"), rs.getString("utm_medium"), rs.getString("utm_source"), rs.getInt("destination"),
                        devices.get(rs.getInt("destination")), rs.getString("days"), rs.getInt("start_hour"), rs.getInt("end_hour"))), companyId);
        return list;
    }

    public void insertRule(int companyId, String cities, String channels, String sources, String devices, String days, String startHour, String endHour) throws SQLException {
        String sql = "INSERT INTO sync_rules(company_id, geo, utm_medium, utm_source, days, destination, start_hour, end_hour) " +
                "     VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        //TODO: hours: 00 and 0
        executeUpdate(sql, companyId, cities, channels, sources, days, devices, startHour, endHour);
    }

    public void updateRule(int id, int companyId, String cities, String channels, String sources, String devices, String days, String startHour, String endHour) throws SQLException {
        String sql = "UPDATE sync_rules" +
                "        SET geo         = ?," +
                "            utm_medium  = ?," +
                "            utm_source  = ?," +
                "            days        = ?," +
                "            destination = ?," +
                "            start_hour  = ?," +
                "            end_hour    = ?" +
                "      WHERE id = ?" +
                "        AND company_id = ?";

        //TODO: hours: 00 and 0
        executeUpdate(sql, cities, channels, sources, days, devices, startHour, endHour, id, companyId);
    }

    public void deleteRule(int id, int companyId) throws SQLException {
        String sql = "DELETE FROM sync_rules WHERE id = ? AND company_id = ?";

        executeUpdate(sql, id, companyId);
    }

    public void updateRules(Integer ruleId, int companyId, String cities, String channels, String sources, String devices, String days, String startHour, String endHour) throws SQLException {
        String sql = "MERGE INTO sync_rules r" +
                "     USING (SELECT ? AS id," +
                "                   ? AS company_id," +
                "                   ? AS geo," +
                "                   ? AS utm_medium," +
                "                   ? AS utm_source," +
                "                   ? AS days," +
                "                   ? AS destination," +
                "                   ? AS start_hour," +
                "                   ? AS end_hour" +
                "              FROM dual) d" +
                "     ON (r.id = d.id)" +
                "     WHEN MATCHED THEN" +
                "       UPDATE" +
                "           SET r.company_id  = d.company_id," +
                "               r.geo         = d.geo," +
                "               r.utm_medium  = d.utm_medium," +
                "               r.utm_source  = d.utm_source," +
                "               r.days        = d.days," +
                "               r.destination = d.destination," +
                "               r.start_hour  = d.start_hour," +
                "               r.end_hour    = d.end_hour" +
                "     WHEN NOT MATCHED THEN" +
                "       INSERT" +
                "          (r.company_id," +
                "           r.geo," +
                "           r.utm_medium," +
                "           r.utm_source," +
                "           r.days," +
                "           r.destination," +
                "           r.start_hour," +
                "           r.end_hour)" +
                "       VALUES" +
                "          (d.company_id," +
                "           d.geo," +
                "           d.utm_medium," +
                "           d.utm_source," +
                "           d.days," +
                "           d.destination," +
                "           d.start_hour," +
                "           d.end_hour)";
        //TODO: hours: 00 and 0
        executeUpdate(sql, ruleId, companyId, cities, channels, sources, days, devices, startHour, endHour);
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

    public List<String> getSources(int companyId) throws SQLException {
        String sql = "SELECT replace(c.name, 'all', null) AS name" +
                "       FROM cabinet_utm_source c" +
                "       JOIN (SELECT id," +
                "                    row_number() over(partition by name order by company_id nulls first) r" +
                "               FROM cabinet_utm_source c" +
                "              WHERE c.company_id = ?" +
                "                 OR c.company_id IS NULL" +
                "              ORDER BY name) n" +
                "         ON c.id = n.id" +
                "        AND n.r = 1" +
                "      ORDER BY rating desc";

        List<String> sources = new ArrayList<>();
        execute(sql, 500, (rs) -> sources.add(rs.getString("name")), companyId);
        return sources;
    }

    public List<SelectedTreeEntity> getSelectedTree(Integer ruleId) throws SQLException {
        List<SelectedTreeEntity> list = new ArrayList<>();
        String sql = "SELECT t.id," +
                "     title," +
                "     parent_id," +
                "     (SELECT least(count(*), 1) FROM sync_rules" +
                "       WHERE id = ?" +
                "         AND (geo LIKE '%:' || t.id || ':%' OR geo LIKE '%:' || t.id OR geo LIKE t.id || ':%')) AS is_selected " +
                "FROM cidr_tree t";
        execute(sql, 1000,
                (rs) -> list.add(new SelectedTreeEntity(rs.getInt("id"), rs.getString("title"), rs.getInt("parent_id"), rs.getInt("is_selected") == 1))
                , ruleId);

        return list;
    }

    public List<SelectedTreeEntity> getSelectedTree() throws SQLException {
        List<SelectedTreeEntity> list = new ArrayList<>();
        String sql = " SELECT id, title, parent_id, 0 AS is_selected" +
                "      FROM cidr_tree";
        execute(sql, 1000,
                (rs) -> list.add(new SelectedTreeEntity(rs.getInt("id"), rs.getString("title"), rs.getInt("parent_id"), rs.getInt("is_selected") == 1)));
        return list;
    }

    /*    List<List> getRules(int companyId) throws SQLException {
        String sql = "SELECT r.id AS empno," +
                "            replace(nvl(r.utm_medium,'все'), 'all', 'все') AS utm_medium," +
                "            replace(nvl(r.utm_source,'все'), 'all', 'все') AS utm_source," +
                "            decode(r.destination, 0, 'все', 1, 'ПК и ноутбуки', 2, 'мобильные') AS destination," +
                "            r.days || ', ' || r.start_hour || '-' || r.end_hour || 'чч' AS runmode" +
                "  FROM analitic.clients_rules r" +
                " WHERE r.company_id = ?" +
                " ORDER BY utm_medium desc, utm_source DESC, id ASC";

        return makeTable(sql, null, 5, companyId);
    }*/

    public Map<Integer, String> getSegments(int companyId) throws SQLException {
        String sql = "SELECT id || ' - ' || REPLACE(NVL(utm_medium, 'все каналы'), 'all', 'все каналы') || ', ' ||" +
                "                           REPLACE(NVL(utm_source, 'все источники'), 'all', 'все источники') AS display_value," +
                "            id AS return_value" +
                "       FROM sync_rules" +
                "      WHERE company_id = ?" +
                "      ORDER BY ID DESC";

        Map<Integer, String> rules = new LinkedHashMap<>();
        execute(sql, 500, (rs) -> rules.put(rs.getInt("return_value"), rs.getString("display_value")), companyId);
        return rules;
    }

    public List<DiscountEntity> getVendorsDiscounts(int companyId, int ruleId) throws SQLException {
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
                "        AND sync_rules_id = ?" +
                "      ORDER BY category DESC, vendors DESC";

        execute(sql, 100, (rs) -> {
            discounts.add(new DiscountEntity(rs.getInt("id"), rs.getInt("category_id"), rs.getString("category"), rs.getString("vendors"), rs.getInt("min_price"), rs.getInt("max_price"),
                    rs.getInt("step1"), rs.getInt("step2")));
        }, companyId, ruleId);

        return discounts;
    }

    public void insertVendorsDiscount(int companyId, int ruleId, Integer categoryId, String vendor, Integer step1, Integer step2, Integer minPrice, Integer maxPrice) throws SQLException {
        String sql = "INSERT INTO sync_discounts_cat_vndrs(company_id, sync_rules_id, category_id, vendors, step1, step2, min_price, max_price)" +
                "     VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, companyId, ruleId, categoryId, vendor, step1, step2, minPrice, maxPrice);
    }

    public void updateVendorsDiscount(int id, int companyId, int ruleId, Integer categoryId, String vendor, Integer step1, Integer step2, Integer minPrice, Integer maxPrice) throws SQLException {
        String sql = "UPDATE sync_discounts_cat_vndrs" +
                "        SET sync_rules_id     = ?," +
                "            category_id = ?," +
                "            vendors     = ?," +
                "            step1       = ?," +
                "            step2       = ?," +
                "            min_price   = ?," +
                "            max_price   = ?," +
                "            atime       = sysdate" +
                "      WHERE id = ? AND company_id = ?";

        executeUpdate(sql, ruleId, categoryId, vendor, step1, step2, minPrice, maxPrice, id, companyId);
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
                "        AND rownum < 500" +
                "      ORDER BY c.name";

        execute(sql, 500, (rs) -> list.add(new ListEntity<Integer, String>(rs.getInt("return_value"), rs.getString("display_value"))), companyId);
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

    public List<DiscountEntity> getOfferDiscounts(int companyId, int ruleId) throws SQLException {
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
                "        AND d.sync_rules_id = ?" +
                "      ORDER BY d.atime DESC";

        execute(sql, 100, (rs) -> {
            discounts.add(new DiscountEntity(rs.getInt("id"), rs.getString("offer_id"), rs.getString("name"), rs.getInt("step1"), rs.getInt("step2"), rs.getString("url")));
        }, companyId, ruleId);

        return discounts;
    }

    public void insertOfferDiscount(int companyId, int ruleId, int offerId, Integer step1, Integer step2) throws SQLException {
        String sql = "INSERT INTO sync_discounts_offers(company_id, offer_id, sync_rules_id, step1, step2)" +
                "     VALUES(?, ?, ?, ?, ?)";

        executeUpdate(sql, companyId, offerId, ruleId, step1, step2);
    }

    public void updateOfferDiscount(int id, int companyId, int ruleId, int offerId, Integer step1, Integer step2) throws SQLException {
        String sql = "UPDATE sync_discounts_offers" +
                "        SET sync_rules_id = ?, offer_id = ?, step1 = ?, step2 = ?" +
                "      WHERE id = ?" +
                "        AND company_id = ?";

        executeUpdate(sql, ruleId, offerId, step1, step2, id, companyId);
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
