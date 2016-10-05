package huckster.cabinet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import huckster.cabinet.repository.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
public class Util {
    static long timeStone = System.currentTimeMillis();
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);

    public static void timeStone(String message) {
        long newTimeStone = System.currentTimeMillis();
        System.out.println(message + ": " + (newTimeStone - timeStone));
        timeStone = newTimeStone;
    }

    public static <T> String toJson(T obj) {
        return new Gson().toJson(obj);
    }

    public static <T> String toJsonWithNulls(T obj) {
        return new GsonBuilder().serializeNulls().create().toJson(obj);
    }

    public <T> String toJsonWithDataWrap(T obj) {
        return new GsonBuilder().serializeNulls().create().toJson(new JsonDataWrapper<T>(obj));
    }

    public static void logError(String message, Exception e, UserData userData) {
        LOG.error(message + " for company " + userData.getCompanyId(), e);
    }

    public static void logError(String message, UserData userData) {
        LOG.error(message + " for company " + userData.getCompanyId());
    }

    public class JsonDataWrapper<T> {
        private T data;

        public JsonDataWrapper(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }
}