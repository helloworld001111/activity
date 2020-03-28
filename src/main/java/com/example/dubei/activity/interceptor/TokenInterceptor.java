package com.example.dubei.activity.interceptor;

import com.example.dubei.activity.base.cache.Cache;
import com.example.dubei.activity.base.exception.TokenException;
import com.example.dubei.activity.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token check
 * Created by yuyong.zhao on 2017-07-31 17:00.
 */
@Slf4j
//@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final Long EXPIRE_TIME_1_DAY = 24*60*60*1000l; // 操作延期时间

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpResponse, Object handler){
        Cache.put("baby","V1cxR2FXVlJQVDFmTVRVNE5UTXlPRGcwTkRZeU1nPT0=");
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod method = (HandlerMethod)handler;
        if(!method.hasMethodAnnotation(LoginInterceptor.class)){
            return true;
        }
        String token = httpServletRequest.getHeader("Token");
        log.info("Token 验证拦截器==============" + token);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");

        if (token == null || "".equals(token.trim())) {
            log.warn("Token验证失败。参数token为空");
            throw new TokenException("Token验证失败。参数token为空");
        }
        String userId = null;
        try{
            userId = TokenUtil.decryptSalt(token).split("_")[0];
        }catch(Exception e){
            log.error("token解析失败",e);
            throw new TokenException("token解析失败");
        }
        if (Cache.getValue(userId)==null) {
            log.warn(String.format("用户(%s)未登录.", userId));
            throw new TokenException(String.format("用户(%s)未登录.", userId));
        } else if (!Cache.getValue(userId).equals(token)) {
            log.warn(String.format("用户(%s)token验证不匹配，可能（just possible）被其他用户登陆.", userId));
//            throw new TokenException(String.format("用户(%s)token验证不匹配，可能（just possible）被其他用户登陆.", userId));
        }
        resetExpireTime(userId);
        log.info("Token验证成功。token = "  + token);
        return true;
    }

    private static void resetExpireTime(String userId) {
        String token = (String) Cache.getValue(userId);
        log.debug(String.format("用户token验证成功，更新过期时间。userid=%s, token=%s", userId, token));
        Cache.put(userId, token, (System.currentTimeMillis() + EXPIRE_TIME_1_DAY));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
