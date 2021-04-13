package com.laioffer.jupiter2.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.jupiter2.db.MySQLConnection;
import com.laioffer.jupiter2.db.MySQLException;
import com.laioffer.jupiter2.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        ObjectMapper mapper = new ObjectMapper();
        //老生常谈，用mapper来读取request的数据，变成object
//        User user = mapper.readValue(request.getReader(), User.class);
        User user = ServletUtil.readRequestBody(User.class, request);

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean isUserAdded = false;
        MySQLConnection connection = null;
        try {
            connection = new MySQLConnection();
            //加密，并且重新set了user的password。（setPassword()）
            user.setPassword(ServletUtil.encryptPassword(user.getUserId(), user.getPassword()));
            isUserAdded = connection.addUser(user);
        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            connection.close();
        }

        if (!isUserAdded) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            //user重复，返回的 SC_CONFLICT==409
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
