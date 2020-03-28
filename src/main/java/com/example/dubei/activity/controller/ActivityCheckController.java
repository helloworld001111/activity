package com.example.dubei.activity.controller;

import com.example.dubei.activity.base.pojo.Result;
import com.example.dubei.activity.interceptor.LoginInterceptor;
import com.example.dubei.activity.pojo.ActivityCheck;
import com.example.dubei.activity.service.ActivityCheckService;
import com.example.dubei.activity.util.ActionHelperUtils;
import com.example.dubei.activity.util.ValidateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("activityCheck")
@Slf4j
public class ActivityCheckController {

    @Autowired
    private ActivityCheckService activityCheckService;

    @RequestMapping("queryByCondition")
    @LoginInterceptor
    public void queryByCondition(String realName, String beginUploadTime, String endUploadTime, Byte dealStatus, HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        Integer count = activityCheckService.queryCountByCondition(realName,null, beginUploadTime, endUploadTime, dealStatus);
        List<Map<String,Object>> listMap = activityCheckService.queryListByCondition(realName,null,beginUploadTime,endUploadTime,dealStatus);
        ActionHelperUtils.generateGridJson(count,listMap,response);
    }

    @RequestMapping("queryByConditionPage")
    @LoginInterceptor
    public void queryByCondition(String realName, String beginUploadTime, String endUploadTime, Byte dealStatus,int start,int pageSize,HttpServletRequest request,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        Integer count = activityCheckService.queryCountByCondition(realName,null, beginUploadTime, endUploadTime, dealStatus);
        List<Map<String,Object>> listMap = activityCheckService.queryListByCondition(realName,null,beginUploadTime,endUploadTime,dealStatus,start,pageSize);
        ActionHelperUtils.generateGridJson(count,listMap,response);
    }

    @RequestMapping("save")
    public void save(HttpServletResponse response, @RequestBody Map<String,Object> map) throws IOException {
        List<String> imgList = (List)map.get("imgList");
        Integer userId = Integer.valueOf((String)map.get("userId"));
        log.info(ActionHelperUtils.getRequestParams());
        ValidateUtils.notEmpty(imgList,"图片不能为空");
        ValidateUtils.notNull(userId,"用户不能为空");
         activityCheckService.save(imgList,userId);
         ActionHelperUtils.generateResult(Result.success(null),response);
    }

    @RequestMapping("updateById")
    @LoginInterceptor
    public void updateById(Integer id,Byte dealStatus,String dealCause,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        ValidateUtils.notNull(id,"id不能为空");
        ValidateUtils.notNull(dealStatus,"dealStatus不能为空");
        activityCheckService.update(id,dealStatus,dealCause);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

}
