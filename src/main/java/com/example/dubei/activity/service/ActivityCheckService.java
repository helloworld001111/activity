package com.example.dubei.activity.service;

import com.example.dubei.activity.pojo.ActivityCheck;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ActivityCheckService {

    List<Map<String,Object>> queryListByCondition(String realName,Integer userId, String beginUploadTime, String endUploadTime, Byte dealStatus);

    List<Map<String,Object>> queryListByCondition(String realName,Integer userId, String beginUploadTime, String endUploadTime, Byte dealStatus,int start,int pageSize);

    Integer queryCountByCondition(String realName,Integer userId,String beginUploadTime,String endUploadTime,Byte dealStatus);

    void save(List<String> imgList,Integer userId) throws IOException;

    void update(Integer id,Byte dealStatus,String dealCause);

    void updateDealStatusGetd(Integer userId);


}
