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
import java.util.Map;

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
            case "tree":
                data = getTree(req, userData);
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
        Gson json = new Gson();
        HashMap<String, List> map = new HashMap<>();
        List<List> data = new ArrayList<>();
        try {
            data = userData.getGoods((String) req.getSession().getAttribute("periodGoods"));
        } catch (SQLException | DataException e) {
            e.printStackTrace();
        }

        map.put("data", data);
        return json.toJson(map);
    }

    private String getTraffic(HttpServletRequest req, UserData userData) {
        Gson json = new Gson();
        HashMap<String, List> map = new HashMap<>();
        List<List> data = new ArrayList<>();
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
        List<List> data = new ArrayList<>();
        try {
            data = userData.getOrders(Date.valueOf(startDate), Date.valueOf(endDate));
        } catch (SQLException | DataException e) {
            e.printStackTrace();
        }

        String link = "<a data-id=\"%s\" data-status=\"%s\" data-comment=\"%s\" data-toggle=\"modal\" href=\"#editOrder\"><span class=\"glyphicon glyphicon-pencil\"></a>";
        for (List row : data) {
            //order id, status, comment
            row.add(0, String.format(link, row.get(0), row.get(14), row.get(13)));
            row.remove(15);
        }

        map.put("data", data);
        return json.toJson(map);
    }

    private String getTree(HttpServletRequest req, UserData userData) {
/*        return "[{title: \"item1 with key and tooltip\", tooltip: \"Look, a tool tip!\"},\n" +
                "        {title: \"item2: selected on init\", selected: true},\n" +
                "        {\n" +
                "            title: \"Folder\", key: \"id3\",\n" +
                "            children: [\n" +
                "                {\n" +
                "                    title: \"Sub-item 3.1\",\n" +
                "                    children: [\n" +
                "                        {title: \"Sub-item 3.1.1\", key: \"id3.1.1\"},\n" +
                "                        {title: \"Sub-item 3.1.2\", key: \"id3.1.2\"}\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    title: \"Sub-item 3.2\",\n" +
                "                    children: [\n" +
                "                        {title: \"Sub-item 3.2.1\", key: \"id3.2.1\"},\n" +
                "                        {title: \"Sub-item 3.2.2\", key: \"id3.2.2\"}\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            title: \"Document with some children (expanded on init)\", key: \"id4\", expanded: true,\n" +
                "            children: [\n" +
                "                {\n" +
                "                    title: \"Sub-item 4.1 (active on init)\", active: true,\n" +
                "                    children: [\n" +
                "                        {title: \"Sub-item 4.1.1\", key: \"id4.1.1\"},\n" +
                "                        {title: \"Sub-item 4.1.2\", key: \"id4.1.2\"}\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    title: \"Sub-item 4.2 (selected on init)\", selected: true,\n" +
                "                    children: [\n" +
                "                        {title: \"Sub-item 4.2.1\", key: \"id4.2.1\"},\n" +
                "                        {title: \"Sub-item 4.2.2\", key: \"id4.2.2\"}\n" +
                "                    ]\n" +
                "                },\n" +
                "                {title: \"Sub-item 4.3 (hideCheckbox)\", hideCheckbox: true},\n" +
                "                {title: \"Sub-item 4.4 (unselectable)\", unselectable: true}\n" +
                "            ]\n" +
                "        },\n" +
                "        {title: \"Lazy folder\", folder: true, lazy: true}\n" +
                "    ]";*/

        StaticElements.timeStone("getTree");
        System.out.println("DATA");

        Map<Integer, JsonTreeNode> map = null;
        try {
            map = userData.getSelectedTree(req.getParameter("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StaticElements.timeStone("getMap");
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
        StaticElements.timeStone("getList");

        Gson json = new Gson();
      //  System.out.println(json.toJson(list));
        return json.toJson(list);
    }
}