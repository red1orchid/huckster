package huckster.cabinet;

import com.google.gson.Gson;

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

        resp.setContentType("application/json; charset=utf-8");
        try {
            resp.getWriter().print(getOrders(req, userData));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        HashMap map = new HashMap<>();
        List<ArrayList> data = new ArrayList<>();
        try {
            data = userData.getOrders(Date.valueOf(startDate), Date.valueOf(endDate));
        } catch (SQLException | DataException e) {
            e.printStackTrace();
        }
                /*        int startIdx = Integer.parseInt(req.getParameter("start"));
        int endIdx = startIdx + Integer.parseInt(req.getParameter("length"));*/
/*        int totalSize = data.size();
        data = data.subList(startIdx, endIdx);

        map.put("draw", req.getParameter("draw"));
        map.put("recordsTotal", totalSize);
        map.put("recordsFiltered", totalSize);*/

        map.put("data", data);
        return json.toJson(map);
    }
}