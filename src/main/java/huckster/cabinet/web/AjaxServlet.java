package huckster.cabinet.web;

import com.google.gson.Gson;
import huckster.cabinet.DataException;
import huckster.cabinet.Util;
import huckster.cabinet.model.JsonTreeNode;
import huckster.cabinet.repository.UserData;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static huckster.cabinet.Util.DEFAULT_END_DATE;
import static huckster.cabinet.Util.DEFAULT_START_DATE;

/**
 * Created by PerevalovaMA on 18.05.2016.
 */
@WebServlet("/ajax")
public class AjaxServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        UserData userData = (UserData) req.getSession().getAttribute("userData");
        String data = "";
        String type = req.getParameter("type");

        if (type != null) {
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
                case "tree":
                    data = getTree(req, userData);
                    break;
                case "channels":
                    data = Util.toJson(userData.getChannels());
                    break;
                case "sources":
                    data = Util.toJson(userData.getSources());
                    break;
            }

            resp.setContentType("application/json; charset=utf-8");
            try {
                resp.getWriter().print(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getGoods(HttpServletRequest req, UserData userData) {
        List<List> data = userData.getGoods((String) req.getSession().getAttribute("periodGoods"));
        HashMap<String, List> map = new HashMap<>();

        map.put("data", data);
        return Util.toJson(map);
    }

    private String getTraffic(HttpServletRequest req, UserData userData) {
        List<List> data = userData.getTraffic((String) req.getSession().getAttribute("periodTraffic"));
        HashMap<String, List> map = new HashMap<>();

        map.put("data", data);
        return Util.toJson(map);
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

        List<List> data = userData.getOrders(Date.valueOf(startDate), Date.valueOf(endDate));
        HashMap<String, List> map = new HashMap<>();

        String link = "<a data-id=\"%s\" data-status=\"%s\" data-comment=\"%s\" data-toggle=\"modal\" href=\"#editOrder\"><span class=\"glyphicon glyphicon-pencil\"></a>";
        for (List row : data) {
            //order id, status, comment
            row.add(0, String.format(link, row.get(0), row.get(14), row.get(13)));
            row.remove(15);
        }

        map.put("data", data);
        return Util.toJson(map);
    }

    private String getTree(HttpServletRequest req, UserData userData) {
        Map<Integer, JsonTreeNode> map = userData.getSelectedTree(req.getParameter("id"));
        List list = new ArrayList<>();

        for(Map.Entry<Integer, JsonTreeNode> entry : map.entrySet()) {
            JsonTreeNode node = entry.getValue();
            if (node.getParent() == 0) {
                list.add(node);
            } else {
                //  System.out.println(node.getParent());
                map.get(node.getParent()).addChild(node);
            }
        }

        return Util.toJson(list);
    }
}