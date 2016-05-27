package huckster.cabinet;

import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Array;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static huckster.cabinet.StaticElements.DEFAULT_END_DATE;
import static huckster.cabinet.StaticElements.DEFAULT_START_DATE;

/**
 * Created by PerevalovaMA on 18.05.2016.
 */
@WebServlet("/datatable")
public class DatatableServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        UserData userData = (UserData) req.getSession().getAttribute("userData");
        String data = "";
        switch (req.getParameter("type")) {
            case "orders":
                data = getOrders(req, userData);
                break;
            case "goods":
                data = getGoods(req, userData);
                break;
            case "traffic":
                data = getTraffic(req, userData);
                break;
        }

        resp.setContentType("application/json; charset=utf-8");
        try {
            resp.getWriter().print(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getGoods(HttpServletRequest req, UserData userData) {
        StaticElements.timeStone("stat get goods");
        Gson json = new Gson();
        HashMap<String, List> map = new HashMap<>();
        List<ArrayList> data = new ArrayList<>();
        try {
            data = userData.getGoods((String) req.getSession().getAttribute("periodGoods"));
        } catch (SQLException | DataException e) {
            e.printStackTrace();
        }

        map.put("data", data);
        StaticElements.timeStone("stat get goods done");
        return json.toJson(map);
    }

    private String getTraffic(HttpServletRequest req, UserData userData) {
        Gson json = new Gson();
        HashMap<String, List> map = new HashMap<>();
        List<ArrayList> data = new ArrayList<>();
        try {
            data = userData.getTraffic((String) req.getSession().getAttribute("periodTraffic"));
        } catch (SQLException | DataException e) {
            e.printStackTrace();
        }

        map.put("data", data);
        return json.toJson(map);
    }

    private String getOrders(HttpServletRequest req, UserData userData) {
        LocalDate startDate = (LocalDate) req.getSession().getAttribute("startDate");
        LocalDate endDate = (LocalDate) req.getSession().getAttribute("endDate");
        if (startDate == null) {
            startDate = DEFAULT_START_DATE;
        }
        if (endDate == null) {
            endDate = DEFAULT_END_DATE;
        }

        Gson json = new Gson();
        HashMap<String, List> map = new HashMap<>();
        List<ArrayList> data = new ArrayList<>();
        try {
            data = userData.getOrders(Date.valueOf(startDate), Date.valueOf(endDate));
        } catch (SQLException | DataException e) {
            e.printStackTrace();
        }

        String link = "<a data-id=\"%s\" data-status=\"%s\" data-comment=\"%s\" data-toggle=\"modal\" href=\"#editOrder\"><span class=\"glyphicon glyphicon-pencil\"></a>";
        for (ArrayList row : data) {
            //order id, status, comment
            row.add(0, String.format(link, row.get(0), row.get(14), row.get(13)));
            row.remove(15);
        }

        map.put("data", data);
        return json.toJson(map);
    }
}