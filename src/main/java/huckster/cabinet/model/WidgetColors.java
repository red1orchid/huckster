package huckster.cabinet.model;

/**
 * Created by PerevalovaMA on 02.11.2016.
 */
public class WidgetColors {
    private String body;
    private String buttonTop;
    private String buttonBottom;

    public WidgetColors(String body, String buttonTop, String buttonBottom) {
        this.body = body;
        this.buttonTop = buttonTop;
        this.buttonBottom = buttonBottom;
    }

    public String getBody() {
        return body;
    }

    public String getButtonTop() {
        return buttonTop;
    }

    public String getButtonBottom() {
        return buttonBottom;
    }
}
