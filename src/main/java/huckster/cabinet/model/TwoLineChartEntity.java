package huckster.cabinet.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PerevalovaMA on 17.06.2016.
 */
public class TwoLineChartEntity {
    private int reportId;
    private String period;
    private int value1;
    private int value2;

    public TwoLineChartEntity(int reportId, String period, int value1, int value2) {
        this.reportId = reportId;
        this.period = period;
        this.value1 = value1;
        this.value2 = value2;
    }

    public int getReportId() {
        return reportId;
    }

    public String getPeriod() {
        return period;
    }

    public int getValue1() {
        return value1;
    }

    public int getValue2() {
        return value2;
    }


}
