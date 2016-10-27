package huckster.cabinet.web;

import huckster.cabinet.Util;
import huckster.cabinet.repository.UserData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by PerevalovaMA on 19.08.2016.
 */
interface JsonOutput {
    default void writeJson(HttpServletResponse resp, String data) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().print(data);
    }

    default void writeObject(HttpServletResponse resp, Object obj) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().print(Util.toJson(obj));
    }
}
