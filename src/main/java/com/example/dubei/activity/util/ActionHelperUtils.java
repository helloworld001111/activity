package com.example.dubei.activity.util;

import com.alibaba.druid.support.json.JSONUtils;
import com.example.dubei.activity.base.pojo.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ActionHelperUtils {
    public static Result generateGrid(Integer count,List<Map<String,Object>> listMap){
        HashMap<String, Object> result = new HashMap<>();
        result.put("count",count);
        result.put("data",listMap);
        return Result.success(result);
    }

    public static void generateGridJson(Integer count, List<Map<String,Object>> listMap, HttpServletResponse response) throws IOException {
        HashMap<String, Object> result = new HashMap<>();
        result.put("count",count);
        result.put("data",listMap);
        response.setContentType("application/json;charset=utf-8");
        String str = JsonUtils.toJson(Result.success(result));
        response.getWriter().write(str);
    }

    public static void generateResult(Result result,HttpServletResponse response) throws IOException {
        String str = JsonUtils.toJson(result);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(str);
    }

    public static String getRequestParams(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> result = new HashMap<>();
        for(Map.Entry<String,String[]> entry:parameterMap.entrySet()){
            if(entry.getValue().length==1){
                result.put(entry.getKey(),entry.getValue()[0]);
            }else{
                result.put(entry.getKey(),entry.getValue());
            }
        }
        String json = null;
        try{
            json = JsonUtils.toJson(result);
        }catch(Exception e){
            log.error("解析成json失败:数据"+result.toString(),e);
        }
        return json;
    }
}
