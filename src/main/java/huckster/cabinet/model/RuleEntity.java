package huckster.cabinet.model;

/**
 * Created by PerevalovaMA on 21.07.2016.
 */
public class RuleEntity {
    private int id;
    private String channels;
    private String sources;
    private int devices;
    private String days;
    private String timeFrom;
    private String timeTo;

    public RuleEntity(int id, String channels, String sources, int devices, String days, String timeFrom, String timeTo) {
        this.id = id;
        this.sources = sources;
        this.channels = channels;
        this.devices = devices;
        this.days = days;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public int getId() {
        return id;
    }

    public String getSources() {
        return sources;
    }

    public String getChannels() {
        return channels;
    }

    public int getDevices() {
        return devices;
    }

    public String getDays() {
        return days;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }
}
