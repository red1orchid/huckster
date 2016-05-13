package huckster.cabinet;

import java.util.ArrayList;

/**
 * Created by PerevalovaMA on 13.05.2016.
 */
class ChartData {
    private String xScale;
    private String yScale;
    ArrayList<ChartLine> main = new ArrayList<>();

    ChartData(String xScale, String yScale) {
        this.xScale = xScale;
        this.yScale = yScale;
    }

    void addLine(ChartLine l) {
        main.add(l);
    }
}
