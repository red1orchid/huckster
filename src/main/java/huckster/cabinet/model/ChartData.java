package huckster.cabinet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Perevalova Marina on 13.05.2016.
 */
public class ChartData {
    private String xScale;
    private String yScale;
    private int yMin;
    ArrayList<ChartLine> main = new ArrayList<>();

    public ChartData() {
    }

    public void setProperties(String xScale, String yScale, int yMin) {
        this.xScale = xScale;
        this.yScale = yScale;
        this.yMin = yMin;
    }

    public static Map<Integer, ChartData> makeData(List<TwoLineChartEntity> list, String line1Name, String line2Name) {
        Map<Integer, ChartData> reportsChartData = new HashMap<>();

        for (TwoLineChartEntity entity : list) {
            ChartData chartData = reportsChartData.get(entity.getReportId());
            if (chartData == null) {
                chartData = new ChartData();
                chartData.addLine(new ChartLine(line1Name));
                chartData.addLine(new ChartLine(line2Name));
                reportsChartData.put(entity.getReportId(), chartData);
            }

            chartData.getLine(0).addPoint(new ChartPoint(entity.getPeriod(), entity.getValue1()));
            chartData.getLine(1).addPoint(new ChartPoint(entity.getPeriod(), entity.getValue2()));
        }

        return reportsChartData;
    }

    private void addLine(ChartLine l)  {
        main.add(l);
    }

    private ChartLine getLine(int index) {
        return main.get(index);
    }
}
