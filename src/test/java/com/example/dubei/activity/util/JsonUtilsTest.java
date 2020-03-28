package com.example.dubei.activity.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilsTest {

    @Test
   public void toJson() throws JsonProcessingException {
//        ArrayList<Map<String, Object>> list = new ArrayList<>();
//        HashMap<String, Object> params = new HashMap<String, Object>();
//        params.put("name","zhangsan");
//        params.put("age",null);
//        params.put("date",new Date());
//        list.add(params);
//        String s = JsonUtils.toJson(list);
//        System.out.println(s);
        String s = UUID.randomUUID().toString();
        System.out.println(s);
    }
}