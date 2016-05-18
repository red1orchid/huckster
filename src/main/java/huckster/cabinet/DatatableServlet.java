package huckster.cabinet;

import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by PerevalovaMA on 18.05.2016.
 */
@WebServlet("/datatable")
public class DatatableServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("SERVLET_GET");
/*        System.out.println(req.getParameter("draw"));
        System.out.println(req.getParameter("start"));
        System.out.println(req.getParameter("length"));
        System.out.println(req.getParameter("search[value]"));
        System.out.println(req.getParameter("columns[1][orderable]"));
        System.out.println(req.getParameter("order[i][dir]"));*/
        int startIdx = Integer.parseInt(req.getParameter("start"));
        int endIdx = startIdx + Integer.parseInt(req.getParameter("length"));
        UserData userData = (UserData) req.getSession().getAttribute("userData");

        Gson json = new Gson();
        TreeMap map = new TreeMap<>();
        List<ArrayList> data = new ArrayList<>();
        try {
            data = userData.getOrders();
        } catch (SQLException | DataException e  ) {
            e.printStackTrace();
        }

        int totalSize = data.size();
        data = data.subList(startIdx, endIdx);

        System.out.println(data);
/*        data.add(Arrays.asList("заказ",
                "правило",
                "артикул",
                "код вендора",
                "модель",
                "цена базовая",
                "цена итоговая",
                "скидка",
                "телефон",
                "город",
                "создан",
                "фраза",
                "статус",
                "коммент"));*/
        map.put("draw", req.getParameter("draw"));
        map.put("recordsTotal", totalSize);
        map.put("recordsFiltered", totalSize);
        map.put("data", data);

        System.out.println(json.toJson(map));

  /*      String json = "{\n" +
                "  \"draw\": 1,\n" +
                "  \"recordsTotal\": 57,\n" +
                "  \"recordsFiltered\": 57,\n" +
                "  \"data\": [\n" +
                "    [\n" +
                "                    \"заказ\",\n" +
                "                    \"правило\",\n" +
                "                    \"артикул\",\n" +
                "                    \"код вендора\",\n" +
                "                    \"модель\",\n" +
                "                    \"цена базовая\",\n" +
                "                    \"цена итоговая\",\n" +
                "                    \"скидка\",\n" +
                "                    \"телефон\",\n" +
                "                    \"город\",\n" +
                "                    \"создан\",\n" +
                "                    \"фраза\",\n" +
                "                    \"статус\",\n" +
                "                    \"коммент\"\n" +
                "   ]\n" +
                "  ]\n" +
                "}";*/

        resp.setContentType("application/json; charset=utf-8");
        try {
            resp.getWriter().print(json.toJson(map));
        } catch (IOException e) {
            e.printStackTrace();
        }/*

        resp.addProperty("draw", 1);
            jsonResponse.addProperty("iTotalRecords", iTotalRecords);
            jsonResponse.addProperty("iTotalDisplayRecords", iTotalDisplayRecords);
            jsonResponse.add("aaData", gson.toJsonTree(companies));

            response.setContentType("application/Json");
            response.getWriter().print(jsonResponse.toString());*/

        /*
        <c:forEach var="order" items="${orders}">
        <tr>
        <td>${order.getId()}</td>
        <td>${order.getRuleId()}</td>
        <td>${order.getArticul()}</td>
        <td>${order.getVendorCode()}</td>
        <td>${order.getModel()}</td>
        <td>${order.getPriceBase()}</td>
        <td>${order.getPriceResult()}</td>
        <td>${order.getDiscount()}</td>
        <td>${order.getPhone()}</td>
        <td>${order.getCity()}</td>
        <td>${order.getDate()}</td>
        <td>${order.getPhrase()}</td>
        <td>${order.getStatus()}</td>
        <td>${order.getComment()}</td>
        </tr>
        </c:forEach>*/

    }
}