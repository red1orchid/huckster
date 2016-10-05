package huckster.cabinet.repository;

import huckster.cabinet.model.OrderEntity;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PerevalovaMA on 04.08.2016.
 */
public class OrdersDao extends DbDao {
    public List<OrderEntity> getOrders(int companyId, Date startDate, Date endDate) throws SQLException {
        List<OrderEntity> orders = new ArrayList<>();
        String sql = "SELECT h.id as order_id," +
                "            t.offer_id," +
                "            f.vendor_code," +
                "            f.name as model," +
                "            t.base_price," +
                "            t.end_price," +
                "            t.discount," +
                "            h.phone," +
                "            h.city," +
                "            to_char(h.ctime, 'DD.MM.YYYY HH24:MI') as ctime," +
                "            h.phrase," +
                "            decode(h.processing_status, 0, 'принят', 1, 'в работе', 2, 'обработан', 3, 'выкуплен', 4, 'отложен', 5, 'отменен') as status_title," +
                "            h.processing_comment," +
                "            h.processing_status" +
                "       FROM orders_header h" +
                "      INNER JOIN orders_items t" +
                "         ON h.id = t.orders_header_id" +
                "       LEFT JOIN offers f" +
                "         ON f.company_id = h.company_id" +
                "        AND f.offer_id = t.offer_id" +
                "      WHERE h.company_id = ?" +
                "        AND trunc(h.ctime) BETWEEN ? AND ?";

        execute(sql, 500, (rs) -> {
            orders.add(new OrderEntity(rs.getInt("order_id"), rs.getString("offer_id"), rs.getString("vendor_code"), rs.getString("model"),
                    rs.getDouble("base_price"), rs.getDouble("end_price"), rs.getInt("discount"), rs.getString("phone"), rs.getString("city"), rs.getString("ctime"),
                    rs.getString("phrase"), rs.getInt("processing_status"), rs.getString("status_title"), rs.getString("processing_comment")));
        }, companyId, startDate, endDate);

        return orders;
          //  return makeTable(sql, 500, 15, companyId, startDate, endDate);
    }

    public void updateOrder(int orderId, int status, String comment) throws SQLException {
        String sql = " UPDATE orders_header" +
                "         SET processing_status = ?," +
                "             processing_comment = ?" +
                "       WHERE remote_id = ?";

        executeUpdate(sql, status, comment, orderId);
    }
}
