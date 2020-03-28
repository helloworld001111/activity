package com.example.dubei.activity.util;

import com.alibaba.druid.util.StringUtils;

import java.util.List;

public class ValidateUtils {

    public static void notNull(Object obj,String msg){
        if(obj==null){
            throw new IllegalArgumentException(msg==null?"params can not be null":msg);
        }
    }

    public static void notEmpty(String str,String msg){
        if(StringUtils.isEmpty(str)){
            throw new IllegalArgumentException(msg==null?"params can not be null":msg);
        }
    }

    public static void notEmpty(List list,String msg){
        if(list==null||list.size()==0){
            throw new IllegalArgumentException(msg==null?"params can not be null":msg);
        }
    }
}
