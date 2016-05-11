package huckster.cabinet;

/**
 * Created by PerevalovaMA on 10.05.2016.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class HighChartServlet
 */
@WebServlet(
        name = "XChartServlet",
        urlPatterns = {"/chart"}
)
public class XChartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        String json = "{\n" +
                "  \"xScale\": \"time\",\n" +
                "  \"yScale\": \"linear\",\n" +
                "  \"main\": [\n" +
                "    {\n" +
                "      \"className\": \".pizza\",\n" +
                "      \"data\": [\n" +
                "        {\n" +
                "          \"x\": \"2012-10-05\",\n" +
                "          \"y\": 6\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-06\",\n" +
                "          \"y\": 6\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-07\",\n" +
                "          \"y\": 8\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-08\",\n" +
                "          \"y\": 3\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-09\",\n" +
                "          \"y\": 4\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-10\",\n" +
                "          \"y\": 9\n" +
                "        },\n" +
                "        {\n" +
                "          \"x\": \"2012-11-11\",\n" +
                "          \"y\": 6\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem("Работа с заказами", null, "glyphicon glyphicon-shopping-cart"));
        list.add(new MenuItem("Настройки виджета", null, "glyphicon glyphicon-plus-sign"));
        list.add(new MenuItem("Статистика и аналитика", null, "glyphicon glyphicon-stats"));
        list.add(new MenuItem("Общие настройки", null, "glyphicon glyphicon-cog"));

        System.out.println(json);
        request.setAttribute("data", json);
        request.setAttribute("menu", list);
        request.getRequestDispatcher("/jsp/chart.jsp").forward(request, response);
    }
/*
    *//**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     *//*
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        String json = "";
        List<ChartDto> chartDtos = new ArrayList<ChartDto>();
        for (ChartModel chartModel : chartModels) {
            ChartDto chartDto = new ChartDto(chartModel.getDate(), chartModel.getVisits());
            chartDtos.add(chartDto);
        }
        if (!chartDtos.isEmpty()) {
            //convert list of pojo model to json for plotting on graph
            json = gson.toJson(chartDtos);
        } else {
            json = "No record found";
        }
        System.out.println(json);
        //write to the response
        response.getWriter().write(json);
    }*/

}