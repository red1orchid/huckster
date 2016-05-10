package huckster.cabinet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marina on 09.05.2016.
 */
@WebServlet(
        name = "DashbordServlet",
        urlPatterns = {""}
)
public class DashbordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isCookie(req)) {
            req.setAttribute("menu", getMenu());
            req.setAttribute("panels", getPanels());
            System.out.println(getMenu().size());
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        }
        else {
            resp.sendRedirect("login");
        }
    }

    private List<MenuItem> getMenu() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem("Работа с заказами", null, "glyphicon glyphicon-shopping-cart"));
        list.add(new MenuItem("Настройки виджета", null, "glyphicon glyphicon-plus-sign"));
        list.add(new MenuItem("Статистика и аналитика", null, "glyphicon glyphicon-stats"));
        list.add(new MenuItem("Общие настройки", null, "glyphicon glyphicon-cog"));

        return list;
    }

    private List<StatisticPanel> getPanels() {
        List<StatisticPanel> list = new ArrayList<>();
        list.add(new StatisticPanel(3248.0, "Доход за текущий день, т.р.", "glyphicon glyphicon-ruble", "panel-primary", "<b>+23.72%</b> доход LFL"));
        list.add(new StatisticPanel(312.0, "Заказы за текущий месяц, шт.", "glyphicon glyphicon-shopping-cart", "panel-warning", "<b>+23.72%</b> заказы LFL"));
        list.add(new StatisticPanel(4.29, "Конверсия за текущий месяц, %", "glyphicon glyphicon-stats", "panel-success", "<b>+23.72%</b> конверсия LFL"));
        list.add(new StatisticPanel(32.52, "Покрытие за текущий месяц, %", "glyphicon glyphicon-user", "panel-danger", "<b>+23.72%</b> покрытие LFL"));
        return list;
    }

    private boolean isCookie(HttpServletRequest req) {
        boolean isCookie = false;
        Cookie[] cookies = req.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("user")) {
                System.out.println(c.getValue());
                // string userId= c.getValue();
                isCookie = true;
            }
        }

        return isCookie;
    }
}
