package huckster.cabinet;

import com.google.gson.Gson;
import huckster.cabinet.model.ChartData;
import huckster.cabinet.repository.UserData;

import java.sql.SQLException;

/**
 * Created by Perevalova Marina on 15.05.2016.
 */
public class Chart {
    private ChartData data;
    private String title;
    private String icon;
    private Type type;

    public Chart(UserData userData, Type type, ChartData chartData) throws SQLException {
        String period = userData.getPeriod();
        String currency = userData.getCurrency();
        switch (period) {
            case "day":
                title = String.format(type.getTitle(), " за текущий день", currency);
                break;
            case "week":
                title = String.format(type.getTitle(), " за текущую неделю", currency);
                break;
            case "month":
                title = String.format(type.getTitle(), " за текущий месяц", currency);
                break;
        }
        icon = "glyphicon glyphicon-stats";
        this.type = type;
        data = chartData;
    }

    public String getData() {
        Gson json = new Gson();
        return json.toJson(data);
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() { return icon; }

    public String getId() {
        return type.getId();
    }

    public String getVar() {
        return type.getVarName();
    }

    public enum Type {
        INCOME("Доход за %s LFL, %s.", 1, "chart_inc", "data_inc"),
        ORDERS("Заказы за %s LFL, шт.", 2, "chart_ord", "data_ord"),
        CONVERSION("Конверсия за %s LFL, %%", 3, "chart_cnv", "data_cnv");

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
