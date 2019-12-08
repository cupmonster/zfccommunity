package com.zfc.community.controller;

import com.zfc.community.dto.AccessTokenDto;
import com.zfc.community.dto.GithubUser;
import com.zfc.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect_uri}")
    private String redirectUri;
    @RequestMapping("/callback")
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state) {
        AccessTokenDto accessTokenDto=new AccessTokenDto();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setState(state);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(redirectUri);
        String accessToken=githubProvider.getAccessToken(accessTokenDto);
        GithubUser githubUser=githubProvider.getUser(accessToken);
        System.out.println(githubUser);
        return "index";
    }
}
