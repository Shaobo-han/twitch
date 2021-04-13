package com.laioffer.jupiter2.servlet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.jupiter2.entity.Item;
import org.apache.commons.codec.digest.DigestUtils;//加密用的library

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ServletUtil {
    public static void writeItemMap(HttpServletResponse response, Map<String, List<Item>> itemMap) throws IOException {
        response.setContentType("application/json;charset=UTF-8");//告诉前端返回的数据为json格式，支持unicode
        response.getWriter().print(new ObjectMapper().writeValueAsString(itemMap));
    }

    public static String encryptPassword(String userId, String password) throws IOException {
        return DigestUtils.md5Hex(userId + DigestUtils.md5Hex(password)).toLowerCase();
        //.md5Hex()这个是加密的那个library提供的加密方法，是单向的加密方法，只能加密，无法解密。（无法变回原始的数据）
        //最后这个toLowerCase()只是变成小写的功能，看着舒服！
    }

    //把前端传来的json格式的String-->java object
    public static <T> T readRequestBody(Class<T> cl, HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(request.getReader(), cl);
        } catch (JsonParseException | JsonMappingException e) {
            return null;
        }
    }


}
