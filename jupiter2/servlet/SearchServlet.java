package com.laioffer.jupiter2.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.jupiter2.external.TwitchClient;
import com.laioffer.jupiter2.external.TwitchException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gameId = request.getParameter("game_id");
        if (gameId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        TwitchClient client = new TwitchClient();
        try {
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().print(new ObjectMapper().writeValueAsString(client.searchItems(gameId)));
            // writeValueAsString()是把object convert成json格式的String，跟server作为前端跟db沟通时用到的ObjectMapper()b .readValue()正相反。
            //readValue()是把json变成object

            ServletUtil.writeItemMap(response, client.searchItems(gameId));

        } catch (TwitchException e) {
            throw new ServletException(e);
        }

    }
}
