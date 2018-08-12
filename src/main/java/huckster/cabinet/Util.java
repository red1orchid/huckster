package huckster.cabinet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import huckster.cabinet.repository.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
public class Util {
    static long timeStone = System.currentTimeMillis();
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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

    public static LocalDate parseDate(String dateStr) {
        if (dateStr != null && !dateStr.isEmpty()) {
            return LocalDate.parse(dateStr, FORMATTER);
        } else {
            return null;
        }
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