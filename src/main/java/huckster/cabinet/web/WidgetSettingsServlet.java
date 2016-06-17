package huckster.cabinet.web;

import huckster.cabinet.StaticElements;
import huckster.cabinet.repository.UserData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by PerevalovaMA on 30.05.2016.
 */
@WebServlet("/widget_settings")
public class WidgetSettingsServlet extends UserServlet {
    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        StaticElements.timeStone("stat: start");
        req.setAttribute("rules", userData.getRules());
        StaticElements.timeStone("stat: get rules");
        req.setAttribute("channels", userData.getChannels());
        StaticElements.timeStone("stat: get channels");
        req.setAttribute("sources", userData.getSources());
        StaticElements.timeStone("stat: get sources");
        req.setAttribute("channel", "cpa:referral");
        StaticElements.timeStone("stat: get data");
        req.getRequestDispatcher("/jsp/widget_settings.jsp").forward(req, resp);
        StaticElements.timeStone("stat: done");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DODODO");
        System.out.println(req.getParameter("tree"));
        System.out.println(req.getParameter("channels"));
        System.out.println(req.getParameter("sources"));
    }
}
