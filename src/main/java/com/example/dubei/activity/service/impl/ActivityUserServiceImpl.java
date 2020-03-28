package com.example.dubei.activity.service.impl;

import com.example.dubei.activity.base.jdbc.DaoMyBatis;
import com.example.dubei.activity.pojo.ActivityGods;
import com.example.dubei.activity.pojo.ActivityUser;
import com.example.dubei.activity.service.ActivityCheckService;
import com.example.dubei.activity.service.ActivityGodsService;
import com.example.dubei.activity.service.ActivityUserService;
import com.example.dubei.activity.util.DateUtils;
import com.example.dubei.activity.util.ParameterHandlerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityUserServiceImpl implements ActivityUserService {

    @Autowired
    private DaoMyBatis daoMyBatis;

    @Autowired
    private ActivityGodsService activityGodsService;

    @Autowired
    private ActivityCheckService activityCheckService;

    @Override
    public List<Map<String, Object>> queryListByCondition(String wechatNickName, String realName, String phoneNumber) {
        HashMap<String, Object> params = new HashMap<>();
        ParameterHandlerUtils.notNullAndEmptyPut(params,"wechatNickName",wechatNickName);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"realName",realName);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"phoneNumber",phoneNumber);
        return daoMyBatis.queryList("com.example.dubei.activity.mapper.ActivityUserMapper.queryListByCondition",params);
    }

    @Override
    public List<Map<String, Object>> queryListByCondition(String wechatNickName, String realName, String phoneNumber,int start,int pageSize) {
        HashMap<String, Object> params = new HashMap<>();
        ParameterHandlerUtils.notNullAndEmptyPut(params,"wechatNickName",wechatNickName);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"realName",realName);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"phoneNumber",phoneNumber);
        return daoMyBatis.queryPageList("com.example.dubei.activity.mapper.ActivityUserMapper.queryListByCondition",params,start,pageSize);
    }

    @Override
    public Integer queryCountByCondition(String wechatNickName, String realName, String phoneNumber) {
        HashMap<String, Object> params = new HashMap<>();
        ParameterHandlerUtils.notNullAndEmptyPut(params,"wechatNickName",wechatNickName);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"realName",realName);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"phoneNumber",phoneNumber);
        return (Integer)daoMyBatis.queryObject("com.example.dubei.activity.mapper.ActivityUserMapper.queryCountByCondition",params);
    }

    @Override
    @Transactional
    public void save(ActivityUser user) {
        user.setLastGetTime(null);
        user.setLastGetGodsId(null);
        user.setUsableSignInCount((byte)0);
        user.setIsDishonest((byte)0);
        user.setRemark("");
        user.setRegisterTime(new Date());
        daoMyBatis.save("com.example.dubei.activity.mapper.ActivityUserMapper.insertSelective",user);
    }

    @Override
    @Transactional
    public void update(ActivityUser user) {
        daoMyBatis.update("com.example.dubei.activity.mapper.ActivityUserMapper.updateByPrimaryKeySelective",user);
    }

    @Override
    public boolean validateGetDods(Integer userId,Integer godsId) {
        ActivityUser user = queryUserById(userId);
        ActivityGods activityGods = activityGodsService.queryById(godsId);
        if(activityGods==null){
            throw new RuntimeException("找不到该商品");
        }
        Byte isShareGroupGet = activityGods.getIsShareGroupGet();
        //分享获取。用户通过则允许获取
        if(isShareGroupGet==1){
            List<Map<String,Object>> listMap = activityCheckService.queryListByCondition(null,userId,null,null,(byte)1);
            //没有处理通过的订单
            if(listMap==null|| listMap.size()==0){
                throw new RuntimeException("没有审核通过的分享截图");
            }else{
                return true;
            }
        }
        Date lastGetTime = user.getLastGetTime();
        //说明用户是第一次领取
        if(lastGetTime==null){
            return true;
        }else{
            Byte usableSignInCount = user.getUsableSignInCount();
            int days = DateUtils.getDays(lastGetTime, new Date());
            if(usableSignInCount+days>=7){
                //距离上一次领取超过7天，且商品还有数量。return true
                ActivityGods gods = activityGodsService.queryById(godsId);
                if(gods.getGodsNumber()<=0){
                     throw new RuntimeException("商品卖光啦");
                }
                return true;
            }else{
                throw new RuntimeException("不满足领取天数条件哦");
            }
        }
    }

    @Override
    @Transactional
    public void getGods(Integer userId,Integer godsId) {

    }

    @Override
    public Map<String,Object> queryById(Integer id) {
        Map<String,Object> user = (Map<String,Object>) daoMyBatis.queryObject("com.example.dubei.activity.mapper.ActivityUserMapper.queryById",id);
        return user;
    }

    @Override
    public ActivityUser queryUserById(Integer id) {
        ActivityUser user = (ActivityUser) daoMyBatis.queryObject("com.example.dubei.activity.mapper.ActivityUserMapper.selectByPrimaryKey",id);
        return user;
    }

    @Override
    public void saveOrUpdate(ActivityUser user) {
        update(user);
    }

    @Override
    public Map<String, Object> queryByOpenId(String openId) {
        HashMap<String, Object> params = new HashMap<>();
        ParameterHandlerUtils.notNullAndEmptyPut(params,"openId",openId);
        return (Map<String,Object>)daoMyBatis.queryObject("com.example.dubei.activity.mapper.ActivityUserMapper.queryByOpenId",params);
    }

    @Override
    public boolean signIn(Integer userId) {
        ActivityUser activityUser = queryUserById(userId);
        String lastSignInDate = activityUser.getLastSignInDate();
        if(DateUtils.getCurDate("yyyyMMdd").equalsIgnoreCase(lastSignInDate)){
            return false;
        }else{
            daoMyBatis.update("com.example.dubei.activity.mapper.ActivityUserMapper.signIn",userId);
            return true;
        }
    }

    //返回签到天数+剩余天数
    @Override
    public Map<String, Object> querySignInInfo(Integer userId) {
        HashMap<String, Object> result = new HashMap<>();
        ActivityUser user = queryUserById(userId);
        Date lastGetTime = user.getLastGetTime();
        Byte usableSignInCount = user.getUsableSignInCount();
        result.put("signInDays",usableSignInCount);
        //说明用户是第一次领取
        if(lastGetTime==null){
            result.put("leftDays",0);
            return result;
        }else{
            int days = DateUtils.getDays(lastGetTime, new Date());
            int leftDays = 7-usableSignInCount-days;
            result.put("leftDays",leftDays<=0?0:leftDays);
            return result;
        }
    }

    @Override
    public void deleteById(Integer userId) {
        daoMyBatis.delete("com.example.dubei.activity.mapper.ActivityUserMapper.deleteByPrimaryKey",userId);
    }
}
