package com.zfc.community.controller;

import com.zfc.community.dto.AccessTokenDto;
import com.zfc.community.dto.GithubUser;
import com.zfc.community.mapper.UserMapper;
import com.zfc.community.mode.Mybatis;
import com.zfc.community.mode.User;
import com.zfc.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;  //注入githubProvider对象
    @Value("${github.client.id}")
    private String clientId;                //将参数配置到对应变量中
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect_uri}")
    private String redirectUri;
    //private Mybatis mybatis;   //注入接口类型变量
    @RequestMapping("/callback")     //如果路径中有/callback则拦截执行此方法
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request) throws IOException {
        //获取两个参数，并把所有已知的参数封装进accessTokenDto对象中
        AccessTokenDto accessTokenDto=new AccessTokenDto();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setState(state);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(redirectUri);
                      //调用githubProvider中的方法，访问https://github.com/login/oauth/access_token
                       //并且携带accessTokenDto中的所有参数，获取response中的内容accessToken
        String accessToken=githubProvider.getAccessToken(accessTokenDto);
                     //githubProvider对象调用getUser方法，访问"https://api.github.com/user?access_token="+accessToken
                        //从response对象中获取githubUser对象中的参数name/id/bio
        GithubUser githubUser=githubProvider.getUser(accessToken);
        if (githubUser!=null){
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setName(githubUser.getName());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            (new Mybatis()).retainUserInformation(user);
            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        }else{
            return "redirect:/";

        }
       // return "index";
    }
}
