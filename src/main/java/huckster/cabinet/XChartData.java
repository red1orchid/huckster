package huckster.cabinet;

/**
 * Created by PerevalovaMA on 10.05.2016.
 */
import java.util.ArrayList;
import java.util.List;

public class XChartData {

    private static final List<ChartModel> CHART_MODELS;

    static {
        CHART_MODELS = new ArrayList<ChartModel>();
        CHART_MODELS.add(new ChartModel("06/01/2015", 99l));
        CHART_MODELS.add(new ChartModel("06/02/2015", 102l));
        CHART_MODELS.add(new ChartModel("06/03/2015", 130l));
        CHART_MODELS.add(new ChartModel("06/04/2015", 120l));
        CHART_MODELS.add(new ChartModel("06/05/2015", 109l));
        CHART_MODELS.add(new ChartModel("06/06/2015", 154l));
        CHART_MODELS.add(new ChartModel("06/07/2015", 127l));
        CHART_MODELS.add(new ChartModel("06/08/2015", 146l));
        CHART_MODELS.add(new ChartModel("06/09/2015", 93l));
        CHART_MODELS.add(new ChartModel("06/10/2015", 96l));
        CHART_MODELS.add(new ChartModel("06/11/2015", 115l));
        CHART_MODELS.add(new ChartModel("06/12/2015", 121l));
        CHART_MODELS.add(new ChartModel("06/13/2015", 165l));
        CHART_MODELS.add(new ChartModel("06/14/2015", 198l));
        CHART_MODELS.add(new ChartModel("06/15/2015", 176l));
        CHART_MODELS.add(new ChartModel("06/16/2015", 201l));
        CHART_MODELS.add(new ChartModel("06/17/2015", 187l));
        CHART_MODELS.add(new ChartModel("06/18/2015", 189l));
        CHART_MODELS.add(new ChartModel("06/19/2015", 147l));
        CHART_MODELS.add(new ChartModel("06/20/2015", 174l));
        CHART_MODELS.add(new ChartModel("06/21/2015", 159l));
    }

    public static List<ChartModel> getHighChartDataList() {
        return CHART_MODELS;
    }

}