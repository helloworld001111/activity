package com.example.dubei.activity.service;

import com.example.dubei.activity.pojo.ActivityUser;

import java.util.List;
import java.util.Map;

public interface ActivityUserService {

    List<Map<String,Object>> queryListByCondition(String wechatNickName,String realName,String phoneNumber);

    List<Map<String,Object>> queryListByCondition(String wechatNickName,String realName,String phoneNumber,int start,int pageSize);

    Integer queryCountByCondition(String wechatNickName,String realName,String phoneNumber);

    void save(ActivityUser user);

    void update(ActivityUser user);

    //判断是否有权限获取物品
    boolean validateGetDods(Integer userId,Integer godsId);

    void getGods(Integer userId,Integer godsId);

    Map<String,Object> queryById(Integer id);

    ActivityUser queryUserById(Integer id);

    void saveOrUpdate(ActivityUser user);

    Map<String,Object> queryByOpenId(String openId);

    boolean signIn(Integer userId);

    Map<String,Object> querySignInInfo(Integer userId);

    void deleteById(Integer userId);
}
