package huckster.cabinet;

import java.sql.SQLException;

/**
 * Created by PerevalovaMA on 10.05.2016.
 */
public class StatisticPanel {
    Double content;
    String label;
    String icon;
    String panelClass;
    String footer;

    public StatisticPanel(Type type, String period, int companyId) {
        switch (period) {
            case "day":
                label = String.format(type.getLabel(), " за текущий день");
                break;
            case "week":
                label = String.format(type.getLabel(), " за текущую неделю");
                break;
            case "month":
                label = String.format(type.getLabel(), " за текущий месяц");
                break;
        }
        icon = type.getIcon();
        panelClass = type.getPanelClass();
        try {
            content = DbStatistic.getRate(type, companyId, period);
        } catch (SQLException | DataException e) {
            content = 0.0;
            e.printStackTrace();
        }
        String prc;
        try {
            prc = DbStatistic.getRatePrc(type, companyId, period);
        } catch (SQLException | DataException e) {
            prc = "0.0";
            e.printStackTrace();
        }
        footer = String.format("<b>%s</b>%% %s", prc, type.getFooter());
    }

    public String getFooter() {
        return footer;
    }

    public Double getContent() {
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

    public enum Type {
        INCOME("Доход за %s, т.р.", "доход LFL", "panel-primary", "glyphicon glyphicon-ruble"),
        ORDERS("Заказы за %s, шт.", "заказы LFL", "panel-warning", "glyphicon glyphicon-shopping-cart"),
        CONVERSION("Конверсия за %s, %%", "конверсия LFL", "panel-success", "glyphicon glyphicon-stats"),
        COVERING("Покрытие за %s, %%", "покрытие LFL", "panel-danger", "glyphicon glyphicon-user");

        private final String label;
        private final String footer;
        private final String panelClass;
        private final String icon;

        private Type(String label, String footer, String panelClass, String icon) {
            this.label = label;
            this.footer = footer;
            this.panelClass = panelClass;
            this.icon = icon;
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
    }
}
