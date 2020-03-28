package com.example.dubei.activity.util;

import java.util.UUID;

public class RandomUtils {
    public static String random(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
