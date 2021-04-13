package com.laioffer.jupiter2.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.jupiter2.db.MySQLConnection;
import com.laioffer.jupiter2.db.MySQLException;
import com.laioffer.jupiter2.entity.FavoriteRequestBody;
import com.laioffer.jupiter2.entity.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "FavoriteServlet", urlPatterns = {"/favorite"})
public class FavoriteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        //如果session == null，则没登录（这几行判断有没有session，也就是有没有login的代码，用Spring的话，是不需要自己写的！！！）
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);//SC_FORBIDDEN == 403 错误
            return;
        }
        String userId = (String) session.getAttribute("user_id");
        //通过session的记录来获取user_id!（这个user_id是session.setAttribute()时自己添加的）

//        String userId = request.getParameter("user_id");
        //request.getParameter()是读request的url里的参数，后面第33行的的request.getReader()是读body的，就是获得request的body.

//        ObjectMapper mapper = new ObjectMapper();
        //mapper：把json和object对应起来，下面的readValue()里的两个参数是把request的body-A转换成B类型的数据
//        FavoriteRequestBody body = mapper.readValue(request.getReader(), FavoriteRequestBody.class);
        FavoriteRequestBody body = ServletUtil.readRequestBody(FavoriteRequestBody.class, request);


        if (body == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        MySQLConnection connection = null;
        try {
            connection = new MySQLConnection();
            connection.setFavoriteItem(userId, body.getFavoriteItem());
        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String userId = (String) session.getAttribute("user_id");
//        String userId = request.getParameter("user_id");

//        ObjectMapper mapper = new ObjectMapper();
//        FavoriteRequestBody body = mapper.readValue(request.getReader(), FavoriteRequestBody.class);
        FavoriteRequestBody body = ServletUtil.readRequestBody(FavoriteRequestBody.class, request);

        if (body == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        MySQLConnection connection = null;  //只声明，不是自动赋值null？？？？？
        try {
            connection = new MySQLConnection();
            connection.unsetFavoriteItem(userId, body.getFavoriteItem().getId());
        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {    //?????????????????
            if (connection != null) {
                connection.close();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String userId = (String) session.getAttribute("user_id");

//        String userId = request.getParameter("user_id");

        Map<String, List<Item>> itemMap;
        MySQLConnection connection = null;
        try {
            connection = new MySQLConnection();
            itemMap = connection.getFavoriteItems(userId);
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().print(new ObjectMapper().writeValueAsString(itemMap));
            //把得到的HashMap结果装到response里返回给前端

            ServletUtil.writeItemMap(response, itemMap);//把上面的两行放在了这个函数里

        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

    }
}
