package huckster.cabinet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by PerevalovaMA on 10.05.2016.
 */
@WebServlet(
        name = "LogoutServlet",
        urlPatterns = {"/logout"}
)
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("user")) {
                c.setMaxAge(0);
                resp.addCookie(c);
                System.out.println("delete cookie");
            }
        }

        resp.sendRedirect("/");
    }

    private boolean checkLogin(String username, String password) {
        if (username.equals("1admin") && password.equals("nikita"))
            return true;
        else
            return false;
    }

}