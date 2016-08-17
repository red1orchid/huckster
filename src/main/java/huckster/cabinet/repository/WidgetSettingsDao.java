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
                "  FROM analitic.clients_rules r" +
                " WHERE r.company_id = ?" +
                " ORDER BY utm_medium desc, utm_source DESC, id ASC";

        execute(sql, null,
                (rs) -> list.add(new RuleEntity(rs.getInt("empno"), rs.getString("utm_medium"), rs.getString("utm_source"), rs.getInt("destination"),
                        rs.getString("days"), rs.getString("start_hour"), rs.getString("end_hour"))), companyId);
        return list;
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
                "       FROM analitic.clients_utm_medium c" +
                "       JOIN (SELECT id," +
                "                    row_number() over(partition by name order by company_id nulls first) r" +
                "               FROM analitic.clients_utm_medium c" +
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

    public Map<String, String> getSources(int companyId) throws SQLException {
        String sql = "SELECT nvl(c.name_display, replace(substr(c.name, 1, 25), 'all', null)) AS name_display," +
                "            replace(c.name, 'all', null) AS name" +
                "       FROM analitic.clients_utm_source c" +
                "       JOIN (SELECT id," +
                "                    row_number() over(partition by name order by company_id nulls first) r" +
                "               FROM analitic.clients_utm_source c" +
                "              WHERE c.company_id = ?" +
                "                 OR c.company_id IS NULL" +
                "              ORDER BY name) n" +
                "         ON c.id = n.id" +
                "        AND n.r = 1" +
                "      ORDER BY rating desc";

        Map<String, String> sources = new LinkedHashMap<>();
        execute(sql, 500, (rs) -> sources.put(rs.getString("name"), rs.getString("name_display")), companyId);
        return sources;
    }

    public List<SelectedTreeEntity> getSelectedTree(Integer ruleId) throws SQLException {
        List<SelectedTreeEntity> list = new ArrayList<>();
        String sql = "SELECT t.id," +
                "     title," +
                "     parent_id," +
                "     (SELECT least(count(*), 1) FROM analitic.clients_rules" +
                "       WHERE id = ?" +
                "         AND (geo LIKE '%:' || t.id || ':%' OR geo LIKE '%:' || t.id OR geo LIKE t.id || ':%')) AS is_selected " +
                "FROM analitic.cidr_tree t";
        execute(sql, 1000,
                (rs) -> list.add(new SelectedTreeEntity(rs.getInt("id"), rs.getString("title"), rs.getInt("parent_id"), rs.getInt("is_selected") == 1))
                , ruleId);

        return list;
    }

    public List<SelectedTreeEntity> getSelectedTree() throws SQLException {
        List<SelectedTreeEntity> list = new ArrayList<>();
        String sql = " SELECT id, title, parent_id, 0 AS is_selected" +
                "      FROM analitic.cidr_tree";
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

    public void updateRules(Integer ruleId, int companyId, String cities, String channels, String sources, String devices, String days, String startHour, String endHour) throws SQLException {
        String sql = "MERGE INTO analitic.clients_rules r" +
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

    public Map<Integer, String> getSegments(int companyId) throws SQLException {
        String sql = "SELECT id || ' - ' || REPLACE(NVL(utm_medium, 'все каналы'), 'all', 'все каналы') || ', ' ||" +
                "                           REPLACE(NVL(utm_source, 'все источники'), 'all', 'все источники') AS display_value," +
                "            id AS return_value" +
                "       FROM analitic.clients_rules" +
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
                "                   FROM analitic.yml_categories c" +
                "                  WHERE c.category_id = NVL(t.category_id, 0)" +
                "                    AND c.company_id = t.company_id" +
                "                    AND rownum = 1), '*все категории*') AS category," +
                "            NVL(vendor, '*все вендоры*') AS vendor," +
                "            step1," +
                "            step2," +
                "            min_price," +
                "            max_price" +
                "       FROM analitic.clients_discounts t" +
                "      WHERE company_id = ?" +
                "        AND rule_id = ?" +
                "      ORDER BY category DESC, vendor DESC";

        execute(sql, 100, (rs) -> {
            discounts.add(new DiscountEntity(rs.getInt("id"), rs.getInt("category_id"), rs.getString("category"), rs.getString("vendor"), rs.getInt("min_price"), rs.getInt("max_price"),
                    rs.getInt("step1"), rs.getInt("step2")));
        }, companyId, ruleId);

        return discounts;
        //  return makeTable(sql, 100, 7, companyId, ruleId);
    }

/*    public Optional<DiscountEntity> getVendorsDiscount(int discountId) throws SQLException {
        String sql = "SELECT id," +
                "            category_id AS category," +
                "            vendor," +
                "            step1," +
                "            step2," +
                "            min_price," +
                "            max_price" +
                "       FROM analitic.clients_discounts t" +
                "      WHERE id = ?";

        return selectValue(sql, 100, (rs) ->
                        new DiscountEntity(discountId, rs.getString("category"), rs.getString("vendor"), rs.getInt("min_price"), rs.getInt("max_price"),
                                rs.getInt("step1"), rs.getInt("step2"))
                , discountId);
    }*/

    public List<ListEntity<Integer, String>> getCategories(int companyId) throws SQLException {
        List<ListEntity<Integer, String>> list = new ArrayList<>();
        String sql = "SELECT c.category_id || ' - ' || c.name AS display_value, " +
                "            c.category_id AS return_value" +
                "       FROM analitic.yml_categories c" +
                "      WHERE c.company_id = ?" +
                "        AND c.name IS NOT NULL" +
                "        AND rownum < 500" +
                "      ORDER BY c.name";

        execute(sql, 500, (rs) -> list.add(new ListEntity<Integer, String>(rs.getInt("return_value"), rs.getString("display_value"))), companyId);
        return list;
    }

    public Map<Integer, List<String>> getVendorsByCategory(int companyId) throws SQLException {
        Map<Integer, List<String>> map = new HashMap<>();
        String sql = "SELECT v.vendor, v.category_id" +
                "       FROM analitic.yml_vendors v" +
                "      WHERE v.company_id = ?" +
                "        AND v.vendor IS NOT NULL" +
                "      ORDER BY 1";

        execute(sql, 1000, (rs) -> {
            map.putIfAbsent(rs.getInt("category_id"), new ArrayList<>());
            map.get(rs.getInt("category_id")).add(rs.getString("vendor"));
        }, companyId);
        return map;
    }

    public List<String> getVendors(int companyId) throws SQLException {
        List<String> vendors = new ArrayList<>();
        String sql = "SELECT DISTINCT v.vendor" +
                "       FROM analitic.yml_vendors v" +
                "      WHERE v.company_id = ?" +
                "        AND v.vendor IS NOT NULL" +
                "      ORDER BY 1";

        execute(sql, 500, (rs) -> {
            vendors.add(rs.getString("vendor"));
        }, companyId);

        return vendors;
    }

/*    public List<String> getVendors(int companyId, int categoryId) throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT v.vendor" +
                "       FROM analitic.yml_vendors v" +
                "      WHERE v.company_id = ?" +
                "        AND v.category_id = ?" +
                "      ORDER BY 1";

        execute(sql, 500, (rs) -> list.add(rs.getString("vendor")), companyId, categoryId);
        return list;
    }*/
}
