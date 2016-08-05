package huckster.cabinet.repository;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PerevalovaMA on 04.08.2016.
 */
public class StatisticDao extends DbDao{
    public List<List> getGoods(int companyId, String period) throws SQLException {
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

    public List<List> getTraffic(int companyId, String period) throws SQLException {
        String sql = String.format("SELECT t.rule, t.ords_%1$s, t.trfc_%1$s, t.conv_%1$s, t.disc_%1$s" +
                "                     FROM analitic.mv_traffic_rules t" +
                "                    WHERE t.company_id = ?", period);

        return makeTable(sql, null, 5, companyId);
    }

    public Map<String, String> getYml(int companyId) throws SQLException {
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
}
