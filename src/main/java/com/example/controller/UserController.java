package com.example.controller;

import com.example.Utils.JWTUtils;
import com.example.bean.User;
import com.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(User user, HttpServletResponse response) {

        Map<String,Object> map = new HashMap<>();
        try {
            User login = userService.login(user);
            Map<String,String> payload = new HashMap<>();
            payload.put("uid",String.valueOf(login.getUid()));
            payload.put("username",login.getUname());

            String token = JWTUtils.getToken(payload);
            /*Cookie cookie = new Cookie("token",token);
            Cookie permission = new Cookie("permission",String.valueOf(login.getPermission()));
            response.addCookie(cookie);
            response.addCookie(permission);*/
            map.put("state",true);
            map.put("msg","认证成功");
            map.put("token",token);

        }catch (Exception e){
            map.put("state",false);
            map.put("msg",e.getMessage());
        }


     return map;

    }

    @PostMapping("/register")
    @ResponseBody
    public Map<String,String> register(User user){

        Map<String,String> map = new HashMap<>();
        User Uname = userService.search(user.getUname());
        if (Uname != null){
            map.put("msg","用户名已存在！");
        }else {
            userService.insert(user);
            map.put("msg","注册成功！");
        }

        return map;
    }

}
