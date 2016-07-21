package huckster.cabinet.repository;

import huckster.cabinet.model.CompanyEntity;
import huckster.cabinet.model.RuleEntity;
import huckster.cabinet.model.SelectedTreeEntity;
import huckster.cabinet.model.TwoLineChartEntity;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
public class DbDao {
    private DataSource pool;

    public DbDao() {
        try {
            InitialContext ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/huckster");
            if (pool == null)
                throw new ServletException("Unknown DataSource 'jdbc/huckster'");
        } catch (NamingException | ServletException ex) {
            //TODO: exception?
            ex.printStackTrace();
        }
    }

    private <T> Optional<T> selectValue(String sql,
                                        Integer fetchSize,
                                        ResultSetSelectProcessor<T> processor,
                                        Object... params) throws SQLException {
        T value = null;
        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            int cnt = 0;
            for (Object param : params) {
                ps.setObject(++cnt, param);
            }
            if (fetchSize != null) {
                ps.setFetchSize(fetchSize);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    value = processor.process(rs);
                }
            }
        }
        return Optional.of(value);
    }

    private void execute(String sql,
                         Integer fetchSize,
                         ResultSetExecuteProcessor processor,
                         Object... params) throws SQLException {
        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            int cnt = 0;
            for (Object param : params) {
                ps.setObject(++cnt, param);
            }
            if (fetchSize != null) {
                ps.setFetchSize(fetchSize);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    processor.process(rs);
                }
            }
        }
    }

    private List<List> makeTable(String sql, Integer fetchSize, int columns, Object... params) throws SQLException {
        List<List> table = new ArrayList<>();

        execute(sql, fetchSize, (rs) -> {
            List<String> row = new ArrayList<>();
            for (int i = 1; i <= columns; i++) {
                row.add(rs.getString(i));
            }
            table.add(row);
        }, params);

        return table;
    }

    public boolean isUserExists(String username, String password) throws SQLException {
        String sql = "SELECT count(*) AS count FROM users_auth " +
                "      WHERE upper(user_name) = upper(?) " +
                "        AND password = sys.hash_md5(? || id || upper(user_name))";

        return selectValue(sql, null, (rs) -> rs.getInt("count") > 0, username, password).orElse(false);
    }

    public void updateOrder(int orderId, int status, String comment) throws SQLException {
        String sql = " UPDATE analitic.orders_header" +
                "         SET processing_status = ?," +
                "             processing_comment = ?" +
                "       WHERE remote_id = ?";

        execute(sql, null, (rs) -> {
        }, status, comment, orderId);
    }

    HashMap<Integer, String> getStatisticRates(int companyId, String period) throws SQLException {
        String sql = " SELECT report_id, " +
                "             CASE" +
                "               WHEN report_id IN (4, 7) THEN to_char(value, 'fm90.00')" +
                "               ELSE value" +
                "             END AS value" +
                "   FROM analitic.reports_data" +
                "  WHERE report_id IN (4, 5, 6, 7)" +
                "    AND company_id = ?" +
                "    AND decode(interval, 'ddd', 'day', 'iw', 'week', interval) = ?" +
                "    AND metric_name = 'curr'";

        HashMap<Integer, String> rates = new HashMap<>();

        execute(sql, null, (rs) -> {
            rates.put(rs.getInt("report_id"), rs.getString("value"));
        }, companyId, period);
        return rates;
    }

    HashMap<Integer, String> getStatisticPercents(int companyId, String period) throws SQLException {
        String sql = "SELECT r.report_id, (CASE WHEN sign(r.value-rr.value) = 1 THEN '+' END) || " +
                "                       trim(to_char(round((r.value-rr.value)/decode(rr.value,0,1,rr.value)*100, 2), '999990.99')) AS value" +
                "  FROM analitic.reports_data r" +
                " INNER JOIN analitic.reports_data rr" +
                "    ON rr.report_id = r.report_id" +
                "   AND rr.company_id = r.company_id " +
                "   AND rr.interval = r.interval " +
                " WHERE r.report_id IN (4, 5, 6, 7) " +
                "   AND r.company_id = ? " +
                "   AND r.interval = decode(?, 'day', 'ddd', 'week', 'iw', 'month') " +
                "   AND r.metric_name = 'curr' " +
                "   AND rr.metric_name = 'last'";

        HashMap<Integer, String> percents = new HashMap<>();

        execute(sql, null, (rs) -> {
            percents.put(rs.getInt("report_id"), rs.getString("value"));
        }, companyId, period);
        return percents;
    }

    List<TwoLineChartEntity> getChartData(int companyId, String period) throws SQLException {
        String sql = "SELECT * " +
                "FROM (" +
                "  SELECT report_id, metric_name, period, CASE report_id" +
                "                                            WHEN 3 THEN value*1000" +
                "                                            ELSE to_number(value)" +
                "                                         END AS value" +
                "  FROM analitic.reports_data" +
                "  WHERE report_id IN (1, 2, 3)" +
                "        AND company_id = ?" +
                "        AND interval = ?)" +
                "  PIVOT (" +
                "    sum(value)" +
                "    FOR metric_name IN ('curr' AS curr, 'last' AS last))" +
                "ORDER BY report_id";

        List<TwoLineChartEntity> chartData = new ArrayList<>();

        execute(sql, null, (rs) -> {
            chartData.add(new TwoLineChartEntity(rs.getInt("report_id"), rs.getString("period"), rs.getInt("curr"), rs.getInt("last")));
        }, companyId, period);
        return chartData;
    }

    Optional<Integer> getCompanyId(String username) throws SQLException {
        return selectValue("SELECT company " +
                        "     FROM users_auth " +
                        "    WHERE upper(user_name) = upper(?)"
                , null, (rs) -> rs.getInt("company"), username);
    }

    Optional<CompanyEntity> getCompanyInfo(int companyId) throws SQLException {
        return selectValue("SELECT head_name, price_cur " +
                        "             FROM companies " +
                        "            WHERE company_id = ?", null, (rs) -> new CompanyEntity(rs.getString("head_name"), rs.getString("price_cur"))
                , companyId);
    }

    List<List> getOrders(int companyId, Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT h.remote_id as order_id," +
                "            h.rule_id," +
                "            t.offer_id," +
                "            f.vendor_code," +
                "            f.name as model," +
                "            t.base_price," +
                "            t.end_price," +
                "            t.discount," +
                "            h.phone," +
                "            h.city," +
                "            to_char(h.ctime, 'DD.MM.YYYY HH24:MI')," +
                "            h.phrase," +
                "            decode(h.processing_status, 0, 'принят', 1, 'в работе', 2, 'обработан', 3, 'выкуплен', 4, 'отложен', 5, 'отменен') as processing_status," +
                "            h.processing_comment," +
                "            h.processing_status" +
                "       FROM analitic.orders_header h" +
                "      INNER JOIN analitic.orders_items t" +
                "         ON h.id = t.orders_headers_id" +
                "       LEFT JOIN analitic.mv_offers f" +
                "         ON f.company_id = h.company_id" +
                "        AND f.offer_id = t.offer_id" +
                "      WHERE h.company_id = ?" +
                "        AND trunc(h.ctime) BETWEEN ? AND ?";

        return makeTable(sql, 500, 15, companyId, startDate, endDate);
    }

    List<List> getGoods(int companyId, String period) throws SQLException {
        String sql = String.format("SELECT t.offer_id," +
                "                          t.name," +
                "                          t.category," +
                "                          t.vendor," +
                "                          t.uniq_clients_views_%1$s," +
                "                          t.uniq_clients_widget_%1$s," +
                "                          t.orders_basket_%1$s," +
                "                          t.orders1_%1$s," +
                "                          t.orders2_%1$s," +
                "                          t.orders3_%1$s," +
                "                          nvl(t.image_info, t.reco)" +
                "                     FROM analitic.offers_stats @DB_ORA_PRODUCT t" +
                "                    WHERE t.company_id = ?" +
                "                      AND rownum < 100" +
                "                    ORDER BY t.uniq_clients_widget_%1$s DESC", period);

        return makeTable(sql, 100, 11, companyId);
    }

    List<List> getTraffic(int companyId, String period) throws SQLException {
        String sql = String.format("SELECT t.rule, t.ords_%1$s, t.trfc_%1$s, t.conv_%1$s, t.disc_%1$s" +
                "                     FROM analitic.mv_traffic_rules t" +
                "                    WHERE t.company_id = ?", period);

        return makeTable(sql, null, 5, companyId);
    }

    Map<String, String> getYml(int companyId) throws SQLException {
        String sql = "SELECT id," +
                "            head_company," +
                "            head_url," +
                "            to_char(ctime, 'DD.MM.YYYY') as ctime," +
                "            to_char(feed_date, 'DD.MM.YYYY HH24:MI') as feed_date," +
                "            to_char(get_time, 'DD.MM.YYYY HH24:MI') as get_time," +
                "            offers," +
                "            offers24," +
                "            offers168" +
                "       FROM analitic.yml_stats" +
                "      WHERE id = ?";

        Map<String, String> map = new LinkedHashMap<>();

        execute(sql, null, (rs) -> {
            map.put("Идентификатор", rs.getString("id"));
            map.put("Компания", "<strong>" + rs.getString("head_company") + "</strong>");
            map.put("Адрес сайта", "<strong>" + rs.getString("head_url") + "</strong>");
            map.put("Дата регистрации", rs.getString("ctime"));
            map.put("Дата в текущем YML", rs.getString("feed_date"));
            map.put("Дата обновления YML", rs.getString("get_time"));
            map.put("Товаров в YML", "<strong>" + rs.getString("offers") + "</strong>");
            map.put("YML прошлый день", rs.getString("offers24"));
            map.put("YML прошлая неделя", rs.getString("offers168"));
        }, companyId);

        return map;
    }

    List<SelectedTreeEntity> getSelectedTree(Integer ruleId) throws SQLException {
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

    List<SelectedTreeEntity> getSelectedTree() throws SQLException {
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

    List<RuleEntity> getRules(int companyId) throws SQLException {
        List<RuleEntity> list = new ArrayList<>();
        String sql = "SELECT r.id AS empno," +
                "            replace(nvl(r.utm_medium,'все'), 'all', 'все') AS utm_medium," +
                "            replace(nvl(r.utm_source,'все'), 'all', 'все') AS utm_source," +
                "            decode(r.destination, 0, 'все', 1, 'ПК и ноутбуки', 2, 'мобильные') AS destination," +
                "            r.days," +
                "            r.start_hour," +
                "            r.end_hour" +
                "  FROM analitic.clients_rules r" +
                " WHERE r.company_id = ?" +
                " ORDER BY utm_medium desc, utm_source DESC, id ASC";

        execute(sql, null,
                (rs) -> list.add(new RuleEntity(rs.getInt("empno"), rs.getString("utm_medium"), rs.getString("utm_source"), rs.getString("destination"),
                        rs.getString("days"), rs.getString("start_hour"), rs.getString("end_hour"))), companyId);
        return list;
    }

    List<String> getChannels(int companyId) throws SQLException {
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

    Map<String, String> getSources(int companyId) throws SQLException {
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
}
