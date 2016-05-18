package huckster.cabinet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
class StaticElements{
    static long timeStone = System.currentTimeMillis();

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