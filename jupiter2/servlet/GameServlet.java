package com.laioffer.jupiter2.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
//jackson这个library，把Object的变量和要返回的json格式String的key对应起来

import com.laioffer.jupiter2.entity.Game;
import com.laioffer.jupiter2.external.TwitchClient;
import com.laioffer.jupiter2.external.TwitchException;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
//快速构建json格式的String，作为response。直接往里put数据后，就是一个json格式（这个object的class 叫做 JSONObject），然后它也可以直接.toString()

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "GameServlet", urlPatterns = {"/game"})
public class GameServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        JSONObject jsonRequest = new JSONObject(IOUtils.toString(request.getReader()));
        //接收前端的数据，把前端传来的数据读出来，并且中间把stream转换成String，最后变成一个json格式的object（class为JSONObject）
        //????? new class时，后面的括号是传什么的？？object的初始化嘛？
//        String name = jsonRequest.getString("name");
//        String developer = jsonRequest.getString("developer");
//        String releaseTime = jsonRequest.getString("release_time");
//        String website = jsonRequest.getString("website");
//        float price = jsonRequest.getFloat("price");
//
//        System.out.println("Name is: " + name);
//        System.out.println("Developer is: " + developer);
//        System.out.println("Release time is: " + releaseTime);
//        System.out.println("Website is: " + website);
//        System.out.println("Price is: " + price);
//
//        response.setContentType("application/json");
//        JSONObject jsonResponse = new JSONObject();
//        jsonResponse.put("status", "ok");
//        response.getWriter().print(jsonResponse);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("application/json");
//        ObjectMapper mapper = new ObjectMapper();
        // jackson的功能就体现在这个mapper里
//        Game game = new Game("World of Warcraft", "Blizzard Entertainment", "Feb 11, 2005", "https://www.worldofwarcraft.com", 49.99);
//        response.getWriter().print(mapper.writeValueAsString(game));
        // 把game的fields和json里的key对应起来，直接填写，这样就省去了很多代码量
        String gameName = request.getParameter("game_name");
        //获取URL里的game_name这个参数，可能为null
        TwitchClient client = new TwitchClient();
        //new了一个自己定义的TwitchClient对象！ 后面会调用这个class的方法！比如searchGame()

        response.setContentType("application/json;charset=UTF-8");
        //告诉前端返回的数据是json格式
        try {
            if (gameName != null) {
                response.getWriter().print(new ObjectMapper().writeValueAsString(client.searchGame(gameName)));
                //jackson作用：object -> json格式的String（因为给前端返回的得是json），体现在ObjectMapper这个class里！！
            } else {
                response.getWriter().print(new ObjectMapper().writeValueAsString(client.topGames(0)));
            }
        } catch (TwitchException e) {
            throw new ServletException(e);
        }



    }
}
