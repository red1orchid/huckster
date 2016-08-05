package huckster.cabinet;

import com.google.gson.Gson;
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

    public static void timeStone(String message) {
        long newTimeStone = System.currentTimeMillis();
        System.out.println(message + ": " + (newTimeStone - timeStone));
        timeStone = newTimeStone;
    }

    public static <T>String toJson(T obj) {
        return new Gson().toJson(obj);
    }

    public static void logError(String message, Exception e, UserData userData) {
        LOG.error(message + " for company " + userData.getCompanyName(), e);
    }

    public static void logError(String message, UserData userData) {
        LOG.error(message + " for company " + userData.getCompanyName());
    }
}