package com.zfc.community.provider;

import com.alibaba.fastjson.JSON;
import com.zfc.community.dto.AccessTokenDto;
import com.zfc.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDto accessTokenDto) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        //将对象的内容转换成JSON格式
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDto));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            //st中装的是access_token
            String st = response.body().string();
            String[] split=st.split("&");
            String tokenstr=split[0];
            String token=tokenstr.split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
           String  st= response.body().string();
           //直接将st的JSON对象转换成java类对象
           GithubUser githubUser=JSON.parseObject(st,GithubUser.class);
           return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

