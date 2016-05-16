package huckster.cabinet;

import java.util.ArrayList;

/**
 * Created by Perevalova Marina on 13.05.2016.
 */
class ChartData {
    private String xScale;
    private String yScale;
    private int yMin;
    ArrayList<ChartLine> main = new ArrayList<>();

    ChartData(String xScale, String yScale, int yMin) {
        this.xScale = xScale;
        this.yScale = yScale;
        this.yMin = yMin;
    }

    void addLine(ChartLine l) {
        main.add(l);
    }
}
