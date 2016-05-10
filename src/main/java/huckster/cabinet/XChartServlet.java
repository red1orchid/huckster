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

    List<ChartModel> chartModels;

    /**
     * @see Servlet#init(ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        //get data for XChart
        chartModels = XChartData.getHighChartDataList();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/chart.jsp").forward(request, response);
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        String json = "";
        //get start date from request parameter
        String sDate = request.getParameter("start");
        //get end date from request parameter
        String eDate = request.getParameter("end");
        if (sDate != null && eDate != null) {
            System.out.println("Start Date : " + sDate);
            System.out.println("End Date : " + eDate);
            //Actual data should come from database
            //select data from database based on start date and end date
            //here I am neither going to fetch data from database nor fetch data based on date range
            //you need to manipulate those things from database
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
        } else {
            json = "Date must be selected.";
        }
        System.out.println(json);
        //write to the response
        response.getWriter().write(json);
    }

}