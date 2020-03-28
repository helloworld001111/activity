package com.example.dubei.activity.controller;

import com.example.dubei.activity.base.pojo.Result;
import com.example.dubei.activity.interceptor.LoginInterceptor;
import com.example.dubei.activity.service.ActivityNoticeService;
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
@RequestMapping("activityNotice")
@Slf4j
public class ActivityNoticeController {

    @Autowired
    private ActivityNoticeService activityNoticeService;

    @RequestMapping("queryAll")
    public void queryAll(String title,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        List<Map<String, Object>> listMap = activityNoticeService.queryListByCondition(title);
        Integer count = activityNoticeService.queryCountByCondition(title);
        ActionHelperUtils.generateGridJson(count,listMap,response);
    }

    @RequestMapping("save")
    @LoginInterceptor
    public void save(String title,String content,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        ValidateUtils.notEmpty(title,"title不能为空");
        ValidateUtils.notEmpty(content,"content不能为空");
        activityNoticeService.save(title,content);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

    @RequestMapping("updateById")
    @LoginInterceptor
    public void updateById(Integer id,String title,String content,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        ValidateUtils.notNull(id,"id不能为空");
        ValidateUtils.notEmpty(title,"title不能为空");
        ValidateUtils.notEmpty(content,"content不能为空");
        activityNoticeService.update(id,title,content);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

    @RequestMapping("deleteById")
    @LoginInterceptor
    public void deleteById(Integer id,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        ValidateUtils.notNull(id,"id不能为空");
        activityNoticeService.deleteById(id);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }
}
