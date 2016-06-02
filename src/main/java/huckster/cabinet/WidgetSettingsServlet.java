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
        req.getRequestDispatcher("/jsp/widget_settings.jsp").forward(req, resp);
    }

    void getTree(HttpServletRequest req) throws SQLException {
        UserData userData = (UserData) req.getSession().getAttribute("userData");
     //   List<TreeNode> list = userData.getTree();
        Map<Integer, JsonTreeNode> map = userData.getTree();
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

        Gson json = new Gson();
        System.out.println(json.toJson(list));


/*        int level = -1;
        Map<Integer, JsonTreeNode> map = new HashMap<>();
        for(TreeNode node : list) {
            if (level != node.getLevel()) {
                level = node.getLevel();
                break;
            } else {
                map.put(node.getId(), new JsonTreeNode(node.getId(), node.getData(), null));
            }
        }

        for(Map.Entry<Integer, JsonTreeNode> j : map.entrySet()) {
            System.out.println(j.getKey());
        }*/

/*        list.stream().forEach(a -> {
            System.out.println(a.getId());
            System.out.println(a.getLevel());
        });*/
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DODODO");
        System.out.println(req.getParameter("tree"));
        System.out.println(req.getParameter("test"));
    }
}
