package com.example.dubei.activity.service;

import com.example.dubei.activity.pojo.ActivityUserGetdGods;

import java.util.List;
import java.util.Map;

public interface ActivityUserGetdGodsService {

    List<Map<String,Object>> queryListByCondition(Integer userId,String realName,String deliverStatus);

    List<Map<String,Object>> queryListByCondition(Integer userId,String realName,String deliverStatus,int start,int pageSize);

    Integer queryCountByCondition(Integer userId,String realName,String deliverStatus);


    void save(ActivityUserGetdGods activityUserGetdGods);

    //提交领取信息
    void getGods(ActivityUserGetdGods activityUserGetdGods);

    void updateById(Integer id,String receiverAddress,String receiverName,String receiverPhoneNumber,String remark,String deliverStatus,String transportationId);

    void deleteById(Integer id);
}
