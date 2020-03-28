package com.example.dubei.activity.controller;

import com.example.dubei.activity.base.pojo.Result;
import com.example.dubei.activity.interceptor.LoginInterceptor;
import com.example.dubei.activity.pojo.ActivityUserGetdGods;
import com.example.dubei.activity.service.ActivityUserGetdGodsService;
import com.example.dubei.activity.util.ActionHelperUtils;
import com.example.dubei.activity.util.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("activityUserGetdGods")
@Slf4j
public class ActivityUserGetdGodsController {

    @Autowired
    private ActivityUserGetdGodsService activityUserGetdGodsService;

    @RequestMapping("queryByCondition")
    public void queryByCondition(Integer userId,String realName,String deliverStatus, HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        Integer count = activityUserGetdGodsService.queryCountByCondition(userId,realName,deliverStatus);
        List<Map<String, Object>> listMap = activityUserGetdGodsService.queryListByCondition(userId,realName,deliverStatus);
        ActionHelperUtils.generateGridJson(count,listMap,response);
    }

    @RequestMapping("queryByConditionPage")
    public void queryByConditionPage(Integer userId,String realName, String deliverStatus,int start,int pageSize,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        Integer count = activityUserGetdGodsService.queryCountByCondition(userId,realName, deliverStatus);
        List<Map<String, Object>> listMap = activityUserGetdGodsService.queryListByCondition(userId,realName,deliverStatus,start,pageSize);
        ActionHelperUtils.generateGridJson(count,listMap,response);
    }

    @RequestMapping("getGods")
    public void getGods(ActivityUserGetdGods activityUserGetdGods,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        activityUserGetdGodsService.getGods(activityUserGetdGods);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

    @RequestMapping("updateById")
    public void updateById(Integer id, String receiverAddress, String receiverName, String receiverPhoneNumber,
                           String remark,String deliverStatus,String transportationId, HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        ValidateUtils.notNull(id,"id can not be null");
//        ValidateUtils.notEmpty(receiverAddress,"id can not be null");
//        ValidateUtils.notEmpty(receiverName,"receiverName can not be null");
//        ValidateUtils.notEmpty(receiverPhoneNumber,"receiverPhoneNumber can not be null");
        activityUserGetdGodsService.updateById(id,receiverAddress,receiverName,receiverPhoneNumber,remark,deliverStatus,transportationId);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

    @RequestMapping("deleteById")
    @LoginInterceptor
    public void deleteById(Integer id,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        activityUserGetdGodsService.deleteById(id);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }
}
