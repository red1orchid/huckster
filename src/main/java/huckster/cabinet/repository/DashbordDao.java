package huckster.cabinet.repository;

import huckster.cabinet.model.TwoLineChartEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by PerevalovaMA on 04.08.2016.
 */
public class DashbordDao extends DbDao {
    public DashbordDao() throws SQLException {
        //   alterSession("ALTER SESSION SET NLS_NUMERIC_CHARACTERS = ',.'");
    }

    public HashMap<Integer, String> getStatisticRates(int companyId, String period) throws SQLException {
        String sql = " SELECT report_id, " +
                "             CASE" +
                "               WHEN report_id IN (4, 7) THEN to_char(value, 'fm9999990.00')" +
                "               ELSE to_char(value)" +
                "             END AS value" +
                "   FROM reports_data" +
                "  WHERE report_id IN (4, 5, 6, 7)" +
                "    AND company_id = ?" +
                "    AND decode(interval, 'ddd', 'day', 'iw', 'week', interval) = ?" +
                "    AND metric = 'curr'";

        HashMap<Integer, String> rates = new HashMap<>();

        execute(sql, null, (rs) -> {
            rates.put(rs.getInt("report_id"), rs.getString("value"));
        }, companyId, period);
        return rates;
    }

    public HashMap<Integer, String> getStatisticPercents(int companyId, String period) throws SQLException {
        String sql = "SELECT r.report_id, (CASE WHEN sign(r.value-rr.value) = 1 THEN '+' END) || " +
                "                       trim(to_char(round((r.value-rr.value)/decode(rr.value,0,1,rr.value)*100, 2), '999990.99')) AS value" +
                "  FROM reports_data r" +
                " INNER JOIN reports_data rr" +
                "    ON rr.report_id = r.report_id" +
                "   AND rr.company_id = r.company_id " +
                "   AND rr.interval = r.interval " +
                " WHERE r.report_id IN (4, 5, 6, 7) " +
                "   AND r.company_id = ? " +
                "   AND r.interval = decode(?, 'day', 'ddd', 'week', 'iw', 'month') " +
                "   AND r.metric = 'curr' " +
                "   AND rr.metric = 'last'";

        HashMap<Integer, String> percents = new HashMap<>();

        execute(sql, null, (rs) -> {
            percents.put(rs.getInt("report_id"), rs.getString("value"));
        }, companyId, period);
        return percents;
    }

    public List<TwoLineChartEntity> getChartData(int companyId, String period) throws SQLException {
        String sql = "SELECT *" +
                "       FROM (SELECT report_id, metric, period, value" +
                "               FROM reports_data" +
                "              WHERE report_id IN (1, 2, 3)" +
                "                AND company_id = ?" +
                "                AND interval = ?)" +
                "     PIVOT(sum(value)" +
                "        FOR metric IN('curr' AS curr, 'last' AS last))" +
                "      ORDER BY report_id";

        List<TwoLineChartEntity> chartData = new ArrayList<>();

        execute(sql, null, (rs) -> {
            chartData.add(new TwoLineChartEntity(rs.getInt("report_id"), rs.getString("period"), rs.getInt("curr"), rs.getInt("last")));
        }, companyId, period);
        return chartData;
    }
}
