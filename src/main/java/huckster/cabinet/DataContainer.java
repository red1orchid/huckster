/*
package huckster.cabinet;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

*/
/**
 * Created by Perevalova Marina on 15.05.2016.
 *//*

class DataContainer {
    private HashMap<Integer, Object> container = new HashMap<>();
    private int companyId;
    private String period;
    private Type type;

    DataContainer(Type type) {
*/
/*        switch(type) {
            case RATE:
                valueContainer = new HashMap<>();
                break;
            case PERCENT:
                valueContainer = new HashMap<>();
                break;
            case CHART:
                ChartData chartData = new ChartData("time", "linear");
                chartContainer = new HashMap<>();
                break;
        }*//*

        this.type = type;
    }

    void init(int companyId, String period) {
        this.companyId = companyId;
        this.period = period;
        container.clear();
    }

    boolean isEmpty() {
        return container.isEmpty();
    }

    void fill(DataSource pool, String sql, ResultSetProcessor r) throws SQLException {
        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, companyId);
            ps.setString(2, period);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                switch (type) {
                    case RATE:
                        container.put(rs.getInt("report_id"), rs.getString("value"));
                        break;
                    case PERCENT:
                        container.put(rs.getInt("report_id"), rs.getString("value"));
                        break;
                    case CHART: {
                        ChartData chartData;// = new ChartData("time", "linear");
                        ChartLine current;// = new ChartLine(".current");
                        ChartLine last;// = new ChartLine(".last");
                        int reportId = 0;

                        while (rs.next()) {
                            if (rs.getInt("report_id") != reportId) {
                                if (chartData != null) {
                                    chartData.addLine(current);
                                    chartData.addLine(last);
                                    container.put(reportId, chartData);
                                }
                                chartData = new ChartData("time", "linear");
                                current = new ChartLine(".current");
                                last = new ChartLine(".last");
                            }
                            current.addPoint(new ChartPoint(rs.getString("period"), rs.getInt("curr")));
                            last.addPoint(new ChartPoint(rs.getString("period"), rs.getInt("last")));
                            reportId = rs.getInt("report_id");
                        }
                        chartData.addLine(current);
                        chartData.addLine(last);
                        container.put(reportId, chartData);
                    }
                    break;
                }
                r.execute(rs);
            }

            if (container.isEmpty()) {
                throw new DataException("No appropriate data");
            }
        }
    }

    public HashMap<Integer, Object> getContainer() {
        return container;
    }

    enum Type {
        RATE, PERCENT, CHART
    }
}
*/
