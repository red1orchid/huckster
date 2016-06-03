package huckster.cabinet;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PerevalovaMA on 30.05.2016.
 */
@WebServlet("/widget_settings")
public class WidgetSettingsServlet extends UserServlet {
    @Override
    void initDataGet(HttpServletRequest req, HttpServletResponse resp, UserData userData) throws ServletException, IOException, SQLException {
        req.setAttribute("rules", userData.getRules());
        req.getRequestDispatcher("/jsp/widget_settings.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DODODO");
        System.out.println(req.getParameter("tree"));
    }
}
