package com.example.dubei.activity.util;

import java.util.List;

public class ConcatUtils {
    public static String concat(List<String> list, char separator){
        StringBuilder sb = new StringBuilder();
        for(String str:list){
            sb.append(str).append(separator);
        }
        sb.deleteCharAt(sb.toString().length()-1);
        return sb.toString();
    }
}
