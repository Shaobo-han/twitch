package com.laioffer.jupiter2.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.jupiter2.db.MySQLConnection;
import com.laioffer.jupiter2.db.MySQLException;
import com.laioffer.jupiter2.entity.LoginRequestBody;
import com.laioffer.jupiter2.entity.LoginResponseBody;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        LoginRequestBody body = mapper.readValue(request.getReader(), LoginRequestBody.class);
        LoginRequestBody body = ServletUtil.readRequestBody(LoginRequestBody.class, request);

        if (body == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//返回的是400
            return;
        }

        String username;
        MySQLConnection connection = null;
        try {
            connection = new MySQLConnection();
            String userId = body.getUserId();
            String password = ServletUtil.encryptPassword(body.getUserId(), body.getPassword());
            //也是需要加密，因为判断的是加密后的Password与数据库存储的是不是一致。
            username = connection.verifyLogin(userId, password);
        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            connection.close();
        }

        if (!username.isEmpty()) {
            //getSession有两个功能：1. 创建session；2. 如果session已经存在，则返回已存在的session
            HttpSession session = request.getSession();//session的id在它创建出来时，就已经自己存在了！并且Tomcat会
            //自动返回给前端，不需要自己动手写
            session.setAttribute("user_id", body.getUserId());//可以再session中添加任何想添加的attribute
            session.setMaxInactiveInterval(600);//设置过期时间，单位为s.

            LoginResponseBody loginResponseBody = new LoginResponseBody(body.getUserId(), username);
            response.setContentType("application/json;charset=UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            // 放到response里，用来返回给前端
            response.getWriter().print(new ObjectMapper().writeValueAsString(loginResponseBody));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
