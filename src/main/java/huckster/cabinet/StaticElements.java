package huckster.cabinet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
class StaticElements{
    static long timeStone = System.currentTimeMillis();
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    static final LocalDate DEFAULT_START_DATE = LocalDate.now().minusDays(7);
    static final LocalDate DEFAULT_END_DATE = LocalDate.now();

    static List<MenuItem> getMenu() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem("Работа с заказами", "/orders", "glyphicon glyphicon-shopping-cart"));
        list.add(new MenuItem("Настройки виджета", null, "glyphicon glyphicon-plus-sign"));
        list.add(new MenuItem("Статистика и аналитика", null, "glyphicon glyphicon-stats"));
        list.add(new MenuItem("Общие настройки", null, "glyphicon glyphicon-cog"));

        return list;
    }

    static void timeStone(String message) {
        long newTimeStone = System.currentTimeMillis();
        System.out.println(message + ": " + (newTimeStone - timeStone));
        timeStone = newTimeStone;
    }
}