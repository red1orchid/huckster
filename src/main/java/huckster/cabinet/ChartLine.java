package huckster.cabinet;

import java.util.ArrayList;

/**
 * Created by Perevalova Marina on 13.05.2016.
 */
class ChartLine {
    String className;
    ArrayList<ChartPoint> data = new ArrayList<>();

    ChartLine(String className) {
        this.className = className;
    }

    void addPoint(ChartPoint p) {
        data.add(p);
    }
}
