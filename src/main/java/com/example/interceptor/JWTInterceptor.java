package com.example.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.Utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class JWTInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String,String> map = new HashMap<>();
        String token = request.getHeader("token");
        /*String token = "";
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){
                token = cookie.getValue();
            }
        }

        if (token.equals("")){
            map.put("state","false");
            String json = new ObjectMapper().writeValueAsString(map);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(json);
            return false;
        }*/

        try {
            JWTUtils.verify(token);
            return true;
        }catch (SignatureVerificationException e){//签名异常
            e.printStackTrace();
            map.put("msg","111");
        }catch (TokenExpiredException e){ //令牌超时异常
            e.printStackTrace();
            map.put("msg","222");
        }catch (AlgorithmMismatchException e){ //算法不匹配异常
            e.printStackTrace();
            map.put("msg","333");
        }catch (Exception e){ //总异常，token无效
            e.printStackTrace();
            map.put("msg","444");
        }
        map.put("state","false");
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);

//        request.getRequestDispatcher("/login").forward(request,response);
        return false;
    }


}
