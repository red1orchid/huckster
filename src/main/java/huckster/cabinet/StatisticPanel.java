package huckster.cabinet;

import huckster.cabinet.repository.UserData;

import java.sql.SQLException;

/**
 * Created by Perevalova Marina on 10.05.2016.
 */
public class StatisticPanel {
    private String content;
    private String label;
    private String icon;
    private String footer;
    private String panelClass;

    public StatisticPanel(UserData userData, Type type, String rate, String percent) throws SQLException {
        String period = userData.getPeriod();
        String currency = userData.getCurrency();
        switch (period) {
            case "day":
                label = String.format(type.getLabel(), " текущий день", currency);
                break;
            case "week":
                label = String.format(type.getLabel(), " текущую неделю", currency);
                break;
            case "month":
                label = String.format(type.getLabel(), " текущий месяц", currency);
                break;
        }
        icon = type.getIcon();
        content = rate;
        footer = String.format("<b>%s%%</b> %s", percent, type.getFooter());
        panelClass = type.getPanelClass();
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

    public String getPanelClass() { return panelClass; }

    public enum Type {
        INCOME("Доход за %s, т.%s.", "доход LFL", "panel-primary", "glyphicon glyphicon-ruble", 5),
        ORDERS("Заказы за %s, шт.", "заказы LFL", "panel-warning", "glyphicon glyphicon-shopping-cart", 6),
        CONVERSION("Конверсия за %s, %%", "конверсия LFL", "panel-success", "glyphicon glyphicon-stats", 4),
        COVERING("Покрытие за %s, %%", "покрытие LFL", "panel-danger", "glyphicon glyphicon-user", 7);

        private final String label;
        private final String footer;
        private final String icon;
        private final int reportId;
        private final String panelClass;

        Type(String label, String footer, String panelClass, String icon, int reportId) {
            this.label = label;
            this.footer = footer;
            this.icon = icon;
            this.reportId = reportId;
            this.panelClass = panelClass;
        }

        public String getLabel() {
            return label;
        }

        public String getFooter() {
            return footer;
        }

        public String getIcon() {
            return icon;
        }

        public int getReportId() {
            return reportId;
        }

        public String getPanelClass() { return panelClass; }
    }
}
