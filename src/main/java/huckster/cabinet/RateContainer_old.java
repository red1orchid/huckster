/*
package huckster.cabinet;

import java.util.HashMap;

*/
/**
 * Created by Perevalova Marina on 15.05.2016.
 *//*

class DataContainer {
*/
/*    // 10 min
    private static final long REFRESH_TIME = 10 * 60 * 1000;
    private static long lastRefresh;*//*

    private HashMap<ContainerKey, String> map = new HashMap<>();

    void put(int reportId, String period, String value) {
        ContainerKey key = new ContainerKey(reportId, period);
        map.put(key, value);
    }

    String get(int reportId, String period) {
        return map.get(new ContainerKey(reportId, period));
    }

    boolean isEmpty() {
        return map.isEmpty();
    }

*/
/*    void setLastRefresh() {
        lastRefresh = System.currentTimeMillis();
    }

    void isOldData() {

    }*//*


    private class ContainerKey {
        Integer reportId;
        String period;

        ContainerKey(Integer reportId, String period) {
            this.reportId = reportId;
            this.period = period;
        }

        @Override
        public boolean equals(Object object) {
            return (object.getClass() == this.getClass())
                    && ((ContainerKey) object).reportId.equals(reportId)
                    && ((ContainerKey) object).period.equals(period);
        }

        @Override
        public int hashCode() {
            return reportId.hashCode() + period.hashCode();
        }
    }
}
*/
