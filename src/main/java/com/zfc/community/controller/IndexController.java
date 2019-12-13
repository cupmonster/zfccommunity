package com.zfc.community.controller;

import com.zfc.community.mode.Mybatis;
import com.zfc.community.mode.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index(HttpServletRequest request) throws IOException {
        Cookie[] cookies=request.getCookies();
        for (Cookie cookie:cookies) {
            if (cookie.getName().equals("token")){
                String token=cookie.getValue();
                User user=(new Mybatis()).findUser(token);
                if (user!=null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        return "index";
    }
}
