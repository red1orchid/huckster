package huckster.cabinet;

/**
 * Created by PerevalovaMA on 10.05.2016.
 */
public class StatisticPanel {
    Double content;
    String label;
    String icon;
    String panelType;
    String footer;

    public StatisticPanel(Double content, String label, String icon, String panelType, String footer) {
        this.content = content;
        this.label = label;
        this.icon = icon;
        this.panelType = panelType;
        this.footer = footer;
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

    public String getPanelType() {
        return panelType;
    }
}
