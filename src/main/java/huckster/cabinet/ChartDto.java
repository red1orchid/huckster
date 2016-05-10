package huckster.cabinet;

/**
 * Created by PerevalovaMA on 10.05.2016.
 */
public class ChartDto {

    private String label;
    private Long value;

    public ChartDto(String label, Long value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}
