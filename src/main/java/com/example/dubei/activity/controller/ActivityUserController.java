package com.example.dubei.activity.controller;

import com.example.dubei.activity.base.pojo.Result;
import com.example.dubei.activity.interceptor.LoginInterceptor;
import com.example.dubei.activity.pojo.ActivityUser;
import com.example.dubei.activity.service.ActivityUserService;
import com.example.dubei.activity.util.ActionHelperUtils;
import com.example.dubei.activity.util.ParameterHandlerUtils;
import com.example.dubei.activity.util.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("activityUser")
@Slf4j
public class ActivityUserController {

    @Autowired
    private ActivityUserService activityUserService;

    @RequestMapping("queryById")
    public void queryById(HttpServletRequest request,Integer userId, HttpSession session, HttpServletResponse response) throws IOException {
        String redirectUrl = request.getParameter("redirectUrl");
        String name = request.getParameter("name");
        System.out.println(name+"==="+redirectUrl);
        log.info(ActionHelperUtils.getRequestParams());
        Map<String,Object> map = activityUserService.queryById(userId);
//        ActionHelperUtils.generateResult(Result.success(map),response);
        response.sendRedirect("https://www.baidu.com");
    }

    @RequestMapping("queryByCondition")
    @LoginInterceptor
    public void queryByCondition(String wechatNickName,String realName, String phoneNumber,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        Integer count = activityUserService.queryCountByCondition(wechatNickName,realName,phoneNumber);
        List<Map<String,Object>> listMap = activityUserService.queryListByCondition(null,realName,phoneNumber);
        ActionHelperUtils.generateGridJson(count,listMap,response);
    }

    @RequestMapping("queryByConditionPage")
    @LoginInterceptor
    public void queryByConditionPage(String wechatNickName,String realName, String phoneNumber,int start,int pageSize,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        Integer count = activityUserService.queryCountByCondition(wechatNickName,realName,phoneNumber);
        List<Map<String,Object>> listMap = activityUserService.queryListByCondition(null,realName,phoneNumber,start,pageSize);
        ActionHelperUtils.generateGridJson(count,listMap,response);
    }

    @RequestMapping("saveOrUpdate")
    public void saveOrUpdate(Integer userId,ActivityUser user,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        ValidateUtils.notNull(userId,"userId can not be null");
        user.setId(userId);
        activityUserService.saveOrUpdate(user);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

    @RequestMapping("validateGetDods")
    public void validateGetDods(Integer userId,Integer godsId,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        activityUserService.validateGetDods(userId,godsId);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

    @RequestMapping("signIn")
    public void signId(Integer userId,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        boolean signInResult = activityUserService.signIn(userId);
        if(signInResult){
            ActionHelperUtils.generateResult(Result.success(null),response);
        }else{
            ActionHelperUtils.generateResult(Result.fail("今日已签到哦"),response);
        }
    }

    @RequestMapping("querySignInInfo")
    public void querySignInInfo(Integer userId,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        Map<String, Object> map = activityUserService.querySignInInfo(userId);
        ActionHelperUtils.generateResult(Result.success(map),response);
    }

    //更新用户标签。不诚信用户。评论等。
    @RequestMapping("updateById")
    public void updateById(Integer userId,Byte isDishonest,String remark,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        ActivityUser activityUser = new ActivityUser();
        ValidateUtils.notNull(userId,"userId不能为空");
        ValidateUtils.notNull(isDishonest,"isDishonest字段不能为空");
        activityUser.setId(userId);
        activityUser.setIsDishonest(isDishonest);
        activityUser.setRemark(StringUtils.isEmpty(remark)?"":remark);
        activityUserService.update(activityUser);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

    @RequestMapping("queryUserIdByOpenid")
    public void queryUserIdByOpenid(String openid){
        return;
    }

    @RequestMapping("deleteById")
    @LoginInterceptor
    public void deleteById(Integer userId,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        ValidateUtils.notNull(userId,"userId can not be null");
        activityUserService.deleteById(userId);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

}
