package huckster.cabinet;

/**
 * Created by PerevalovaMA on 10.05.2016.
 */
public class ChartModel {
    private String date;
    private Long visits;

    public ChartModel(String date, Long visits) {
        this.date = date;
        this.visits = visits;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getVisits() {
        return visits;
    }

    public void setVisits(Long visits) {
        this.visits = visits;
    }
}