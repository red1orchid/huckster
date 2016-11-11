package huckster.cabinet.model;

/**
 * Created by PerevalovaMA on 21.07.2016.
 */
public class RuleEntity {
    private String channels;
    private int devices;
    private String strDevices;
    private String days;
    //  private boolean[] daysArray = new boolean[7];
    private int timeFrom;
    private int timeTo;
    private String geo;

    public RuleEntity(String channels, int devices, String strDevices, String days, int timeFrom, int timeTo, String geo) {
        this.channels = channels;
        this.devices = devices;
        this.strDevices = strDevices;
        this.days = days;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.geo = geo;
    }

    public RuleEntity(String channels, int devices, String days, int timeFrom, int timeTo, String geo) {
        this.channels = channels;
        this.devices = devices;
        this.days = days;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.geo = geo;
    }

    public String getChannels() {
        return channels;
    }

    public int getDevices() {
        return devices;
    }

    public String getStrDevices() {
        return strDevices;
    }

    public String getDays() {
        return days;
    }

    public boolean[] getDaysArray() {
        boolean[] daysArray = new boolean[7];
        if (days != null) {
            for (String day : days.split(":")) {
                daysArray[Integer.parseInt(day) - 1] = true;
            }
        }

        return daysArray;
    }

    public int getTimeFrom() {
        return timeFrom;
    }

    public int getTimeTo() {
        return timeTo;
    }

    public String getGeo() {
        return geo;
    }
}
