package huckster.cabinet.repository;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by PerevalovaMA on 04.08.2016.
 */
public class OrdersDao extends DbDao {
    public List<List> getOrders(int companyId, Date startDate, Date endDate) throws SQLException {
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

    public void updateOrder(int orderId, int status, String comment) throws SQLException {
        String sql = " UPDATE analitic.orders_header" +
                "         SET processing_status = ?," +
                "             processing_comment = ?" +
                "       WHERE remote_id = ?";

        executeUpdate(sql, status, comment, orderId);
    }
}
