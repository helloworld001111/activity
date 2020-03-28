package com.example.dubei.activity.util;

import com.alibaba.druid.util.StringUtils;

import java.util.Map;

public class ParameterHandlerUtils {
    public static void notNullPut(Map<String,Object> params, String keyName, Object value){
        if(value==null){
            return;
        }
        params.put(keyName,value);
    }

    public static void notNullAndEmptyPut(Map<String,Object> params, String keyName, String value){
        if(StringUtils.isEmpty(value)){
            return;
        }
        params.put(keyName,value);
    }

    public static void notNullAndEmptyPutWithChar(Map<String,Object> params, String keyName, String value,char c){
        if(StringUtils.isEmpty(value)){
            return;
        }
        params.put(keyName,c+value+c);
    }

    public static void put(Map<String,Object> params,String keyName,String value,String defaultValue){
        if(StringUtils.isEmpty(value)){
            params.put(keyName,defaultValue);
        }else{
            params.put(keyName,value);
        }
    }

    public static void put(Map<String,Object> params,String keyName,Object value,Object defaultValue){
        if(value==null){
            params.put(keyName,defaultValue);
        }else{
            params.put(keyName,value);
        }
    }
}
