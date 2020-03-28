package com.example.dubei.activity.controller;

import com.example.dubei.activity.base.pojo.Result;
import com.example.dubei.activity.interceptor.LoginInterceptor;
import com.example.dubei.activity.service.ActivitySlideshowService;
import com.example.dubei.activity.util.ActionHelperUtils;
import com.example.dubei.activity.util.JsonUtils;
import com.example.dubei.activity.util.ValidateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("activitySlideshow")
@Slf4j
public class ActivitySlideshowController {

    @Autowired
    private ActivitySlideshowService activitySlideshowService;

    @RequestMapping("queryByCondition")
    public void queryByCondition(HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        Integer count = activitySlideshowService.queryCountByCondition();
        List<Map<String,Object>> listMap = activitySlideshowService.queryListByCondition();
        ActionHelperUtils.generateGridJson(count,listMap,response);
    }

    //上传新的数据
    @RequestMapping("save")
    @LoginInterceptor
    public void save(@RequestBody Map<String,Object> map,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        List imgList = (List)map.get("imgList");
        String href = (String)map.get("href");
        ValidateUtils.notNull(imgList,"file can not be empty");
        ValidateUtils.notEmpty(href,"href can not be empty");
        activitySlideshowService.save(imgList,href);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

    @RequestMapping("updateById")
    @LoginInterceptor
    public void updateById(@RequestBody Map<String,Object> map,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        List imgList = (List)map.get("imgList");
        String href = (String)map.get("href");
        Integer id = Integer.valueOf((String)map.get("id"));
        ValidateUtils.notNull(id,"id can not be empty");
        ValidateUtils.notNull(imgList,"file can not be empty");
        ValidateUtils.notEmpty(href,"href can not be empty");
        activitySlideshowService.updateById(id,imgList,href);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

    @RequestMapping("deleteById")
    @LoginInterceptor
    public void delteById(Integer id,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        activitySlideshowService.deleteById(id);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

}
