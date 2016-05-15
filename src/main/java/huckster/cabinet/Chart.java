package huckster.cabinet;

import com.google.gson.Gson;

import java.sql.SQLException;

/**
 * Created by Perevalova Marina on 15.05.2016.
 */
public class Chart {
    private ChartData data;
    private String title;
    private Type type;

    public Chart(DbHelper db, Type type, int companyId, String period) {
        this.type = type;
        title = type.getTitle();

        try {
            data = db.getChartData(type, companyId, period);
        } catch (SQLException | DataException e) {
            e.printStackTrace();
        }
    }

    public String getData() {
        Gson json = new Gson();
        return json.toJson(data);
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return type.getId();
    }

    public String getVar() {
        return type.getVarName();
    }

    enum Type {
        INCOME("Доход за %s, т.р.", 1, "chart_inc", "data_inc"),
        ORDERS("Заказы за %s, шт.", 2, "chart_ord", "data_ord"),
        CONVERSION("Конверсия за %s, %%", 3, "chart_cnv", "data_cnv");

        private final String title;
        private final int reportId;
        private final String id;
        private final String varName;

        Type(String title, int reportId, String id, String varName) {
            this.title = title;
            this.reportId = reportId;
            this.id = id;
            this.varName = varName;
        }

        public String getVarName() { return varName; }

        public String getTitle() {
            return title;
        }

        public int getReportId() {
            return reportId;
        }

        public String getId() {
            return id;
        }
    }
}
