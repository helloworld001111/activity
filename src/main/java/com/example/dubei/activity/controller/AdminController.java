package com.example.dubei.activity.controller;

import com.example.dubei.activity.base.cache.Cache;
import com.example.dubei.activity.base.exception.TokenException;
import com.example.dubei.activity.base.pojo.Result;
import com.example.dubei.activity.util.ActionHelperUtils;
import com.example.dubei.activity.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping("admin")
@Controller
@Slf4j
public class AdminController {

    //登陆
    @RequestMapping("doLogin")
    public void doLogin(String username, String password, HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        if(("admin".equalsIgnoreCase(username)||"junjun".equalsIgnoreCase(username)||"mingming".equalsIgnoreCase(username)||"baby".equalsIgnoreCase(username))&&"123456".equalsIgnoreCase(password)){
            String token = TokenUtil.generate(username);
            //默认1小时后失效
            Cache.put(username,token);
            ActionHelperUtils.generateResult(Result.success(token),response);
        }else{
            throw new AuthenticationException("用户名或密码错误");
        }
    }


    //登出
    @RequestMapping("logout")
    public void logout(HttpServletRequest request,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        String account = null;
        try{
            account = TokenUtil.getAccount(request);
        }catch(Exception e){
            throw new TokenException("token解析失败");
        }
        if(!Cache.contains(account)){
            throw new TokenException("无效的token");
        }
        Cache.remove(account);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }
}
