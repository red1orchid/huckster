package huckster.cabinet;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
public class Util {
    static long timeStone = System.currentTimeMillis();
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final LocalDate DEFAULT_START_DATE = LocalDate.now().minusDays(7);
    public static final LocalDate DEFAULT_END_DATE = LocalDate.now();

    public static void timeStone(String message) {
        long newTimeStone = System.currentTimeMillis();
        System.out.println(message + ": " + (newTimeStone - timeStone));
        timeStone = newTimeStone;
    }

    public static <T>String toJson(T obj) {
        return new Gson().toJson(obj);
    }
}