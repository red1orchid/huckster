package huckster.cabinet;

import huckster.cabinet.DataException;
import huckster.cabinet.UserData;

import java.sql.SQLException;

/**
 * Created by Perevalova Marina on 10.05.2016.
 */
public class StatisticPanel {
    private String content;
    private String label;
    private String icon;
    private String panelClass;
    private String footer;

    StatisticPanel(UserData userData, Type type) throws SQLException {
        String period = userData.getPeriod();
        String currency = userData.getCurrency();
        switch (period) {
            case "day":
                label = String.format(type.getLabel(), " за текущий день", currency);
                break;
            case "week":
                label = String.format(type.getLabel(), " за текущую неделю", currency);
                break;
            case "month":
                label = String.format(type.getLabel(), " за текущий месяц", currency);
                break;
        }
        icon = type.getIcon();
        panelClass = type.getPanelClass();
        try {
            content = userData.getRate(type, "main", period);
        } catch (SQLException | DataException e) {
            content = "0";
            e.printStackTrace();
        }
        String prc;
        try {
            prc = userData.getRate(type, "footer", period);
        } catch (SQLException | DataException e) {
            prc = "0.0";
            e.printStackTrace();
        }
        footer = String.format("<b>%s%%</b> %s", prc, type.getFooter());
    }

    public String getFooter() {
        return footer;
    }

    public String getContent() {
        return content;
    }

    public String getLabel() {
        return label;
    }

    public String getIcon() {
        return icon;
    }

    public String getPanelClass() {
        return panelClass;
    }

    enum Type {
        INCOME("Доход за %s, т.%s.", "доход LFL", "panel-primary", "glyphicon glyphicon-ruble", 5),
        ORDERS("Заказы за %s, шт.", "заказы LFL", "panel-warning", "glyphicon glyphicon-shopping-cart", 6),
        CONVERSION("Конверсия за %s, %%", "конверсия LFL", "panel-success", "glyphicon glyphicon-stats", 4),
        COVERING("Покрытие за %s, %%", "покрытие LFL", "panel-danger", "glyphicon glyphicon-user", 7);

        private final String label;
        private final String footer;
        private final String panelClass;
        private final String icon;
        private final int reportId;

        Type(String label, String footer, String panelClass, String icon, int reportId) {
            this.label = label;
            this.footer = footer;
            this.panelClass = panelClass;
            this.icon = icon;
            this.reportId = reportId;
        }

        public String getLabel() {
            return label;
        }

        public String getFooter() {
            return footer;
        }

        public String getPanelClass() {
            return panelClass;
        }

        public String getIcon() {
            return icon;
        }

        public int getReportId() {
            return reportId;
        }
    }
}
