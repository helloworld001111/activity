package com.example.dubei.activity.service.impl;

import com.example.dubei.activity.base.jdbc.DaoMyBatis;
import com.example.dubei.activity.pojo.ActivityGods;
import com.example.dubei.activity.pojo.ActivityUser;
import com.example.dubei.activity.pojo.ActivityUserGetdGods;
import com.example.dubei.activity.service.ActivityCheckService;
import com.example.dubei.activity.service.ActivityGodsService;
import com.example.dubei.activity.service.ActivityUserGetdGodsService;
import com.example.dubei.activity.service.ActivityUserService;
import com.example.dubei.activity.util.ParameterHandlerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityUserGetdGodsServiceImpl implements ActivityUserGetdGodsService {

    @Autowired
    private DaoMyBatis daoMyBatis;

    @Autowired
    private ActivityGodsService activityGodsService;

    @Autowired
    private ActivityUserService activityUserService;

    @Autowired
    private ActivityCheckService activityCheckService;

    @Override
    public List<Map<String, Object>> queryListByCondition(Integer userId,String realName,String deliverStatus) {
        HashMap<String,Object> params = new HashMap<String,Object>();
        ParameterHandlerUtils.notNullPut(params,"userId",userId);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"realName",realName);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"deliverStatus",deliverStatus);
        return daoMyBatis.queryList("com.example.dubei.activity.mapper.ActivityUserGetdGodsMapper.queryListByCondition",params);
    }

    @Override
    public List<Map<String, Object>> queryListByCondition(Integer userId,String realName,String deliverStatus,int start,int pageSize) {
        HashMap<String,Object> params = new HashMap<String,Object>();
        ParameterHandlerUtils.notNullPut(params,"userId",userId);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"realName",realName);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"deliverStatus",deliverStatus);
        return daoMyBatis.queryPageList("com.example.dubei.activity.mapper.ActivityUserGetdGodsMapper.queryListByCondition",params,start,pageSize);
    }

    @Override
    public Integer queryCountByCondition(Integer userId,String realName,String deliverStatus) {
        HashMap<String,Object> params = new HashMap<String,Object>();
        ParameterHandlerUtils.notNullPut(params,"userId",userId);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"realName",realName);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"deliverStatus",deliverStatus);
        return (Integer)daoMyBatis.queryObject("com.example.dubei.activity.mapper.ActivityUserGetdGodsMapper.queryCountByCondition",params);
    }

    @Override
    public void save(ActivityUserGetdGods activityUserGetdGods) {
        activityUserGetdGods.setDeliverStatus("0");
        if(activityUserGetdGods.getRemark()==null){
            activityUserGetdGods.setRemark("");
        }
        activityUserGetdGods.setCreateTime(new Date());
        daoMyBatis.save("com.example.dubei.activity.mapper.ActivityUserGetdGodsMapper.insertSelective",activityUserGetdGods);
    }

    //2.保存ActivityUserGetdGods
    //1.商品表数量-1校验并更新
    //3.用户最后一次领取时间+领取商品id更新
    @Override
    @Transactional
    public void getGods(ActivityUserGetdGods activityUserGetdGods) {
        activityUserService.validateGetDods(activityUserGetdGods.getUserId(),activityUserGetdGods.getGodsId());
        activityGodsService.decreaseGodsNumber(activityUserGetdGods.getGodsId());
        save(activityUserGetdGods);
        //签到更新用户表。分享更新审核表
        ActivityGods gods = activityGodsService.queryById(activityUserGetdGods.getGodsId());
        if(gods.getIsShareGroupGet().byteValue()==1){
            activityCheckService.updateDealStatusGetd(activityUserGetdGods.getUserId());
        }else{
            ActivityUser user = new ActivityUser();
            user.setId(activityUserGetdGods.getUserId());
            user.setUsableSignInCount((byte)0);
            user.setLastGetTime(new Date());
            user.setLastGetGodsId(activityUserGetdGods.getGodsId());
            activityUserService.update(user);
        }
    }

    @Override
    public void updateById(Integer id, String receiverAddress, String receiverName, String receiverPhoneNumber,
                           String remark,String deliverStatus,String transportationId) {
        ActivityUserGetdGods activityUserGetdGods = new ActivityUserGetdGods();
        activityUserGetdGods.setId(id);
//        activityUserGetdGods.setReceiverAddress(receiverAddress);
//        activityUserGetdGods.setReceiverName(receiverName);
//        activityUserGetdGods.setReceiverPhoneNumber(receiverPhoneNumber);
//        activityUserGetdGods.setRemark(remark);
        if(StringUtils.isNotEmpty(deliverStatus)){
            activityUserGetdGods.setDeliverStatus(deliverStatus);
        }
        if(StringUtils.isNotEmpty(transportationId)){
            activityUserGetdGods.setTransportationId(transportationId);
        }
        if(StringUtils.isNotEmpty(receiverAddress)){
            activityUserGetdGods.setReceiverAddress(receiverAddress);
        }
        if(StringUtils.isNotEmpty(receiverName)){
            activityUserGetdGods.setReceiverName(receiverName);
        }
        if(StringUtils.isNotEmpty(receiverPhoneNumber)){
            activityUserGetdGods.setReceiverPhoneNumber(receiverPhoneNumber);
        }
        if(StringUtils.isNotEmpty(remark)){
            activityUserGetdGods.setRemark(remark);
        }
        daoMyBatis.update("com.example.dubei.activity.mapper.ActivityUserGetdGodsMapper.updateByPrimaryKeySelective",activityUserGetdGods);
    }

    @Override
    public void deleteById(Integer id) {
        daoMyBatis.delete("com.example.dubei.activity.mapper.ActivityUserGetdGodsMapper.deleteByPrimaryKey",id);
    }
}
