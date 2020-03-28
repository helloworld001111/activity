package com.example.dubei.activity.service.impl;

import com.example.dubei.activity.base.jdbc.DaoMyBatis;
import com.example.dubei.activity.constant.Constant;
import com.example.dubei.activity.pojo.ActivityCheck;
import com.example.dubei.activity.service.ActivityCheckService;
import com.example.dubei.activity.util.ConcatUtils;
import com.example.dubei.activity.util.FileUtils;
import com.example.dubei.activity.util.ParameterHandlerUtils;
import com.example.dubei.activity.util.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ActivityCheckServiceImpl implements ActivityCheckService {
    @Autowired
    private DaoMyBatis daoMyBatis;

    @Value("${custome.pic.save.folder}")
    private String picUploadFolder;

    @Override
    public List<Map<String,Object>> queryListByCondition(String realName,Integer userId,String beginUploadTime,String endUploadTime,Byte dealStatus){
        HashMap<String, Object> params = new HashMap<>();
        ParameterHandlerUtils.notNullAndEmptyPutWithChar(params,"realName",realName,'%');
        ParameterHandlerUtils.notNullPut(params,"userId",userId);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"beginUploadTime",beginUploadTime);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"endUploadTime",endUploadTime);
        ParameterHandlerUtils.notNullPut(params,"dealStatus",dealStatus);
        return daoMyBatis.queryList("com.example.dubei.activity.mapper.ActivityCheckMapper.queryListByCondition",params);
    }

    @Override
    public List<Map<String,Object>> queryListByCondition(String realName,Integer userId,String beginUploadTime,String endUploadTime,Byte dealStatus,int start,int pageSize){
        HashMap<String, Object> params = new HashMap<>();
        ParameterHandlerUtils.notNullAndEmptyPutWithChar(params,"realName",realName,'%');
        ParameterHandlerUtils.notNullPut(params,"userId",userId);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"beginUploadTime",beginUploadTime);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"endUploadTime",endUploadTime);
        ParameterHandlerUtils.notNullPut(params,"dealStatus",dealStatus);
        return daoMyBatis.queryPageList("com.example.dubei.activity.mapper.ActivityCheckMapper.queryListByCondition",params,start,pageSize);
    }

    @Override
    public Integer queryCountByCondition(String realName,Integer userId,String beginUploadTime,String endUploadTime,Byte dealStatus){
        HashMap<String, Object> params = new HashMap<>();
        ParameterHandlerUtils.notNullAndEmptyPutWithChar(params,"realName",realName,'%');
        ParameterHandlerUtils.notNullPut(params,"userId",userId);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"beginUploadTime",beginUploadTime);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"endUploadTime",endUploadTime);
        ParameterHandlerUtils.notNullPut(params,"dealStatus",dealStatus);
        return (Integer)daoMyBatis.queryObject("com.example.dubei.activity.mapper.ActivityCheckMapper.queryCountByCondition",params);
    }

    @Override
    @Transactional
    public void save(List<String> imgList, Integer userId) throws IOException {
        List<String> filenameList = FileUtils.base64UpLoadPng(imgList,new File(picUploadFolder+File.separator+ Constant.CHECK_FOLDER),Constant.HTTPS_CHECK_URL);
        ActivityCheck check = new ActivityCheck();
        check.setUploadTime(new Date());
        check.setUserId(userId);
        for(int i = 0;i<filenameList.size();i++){
            filenameList.set(i,filenameList.get(i));
        }
        check.setPicUrl(ConcatUtils.concat(filenameList,','));
        //默认0待处理
        check.setDealStatus((byte)0);
        daoMyBatis.save("com.example.dubei.activity.mapper.ActivityCheckMapper.insertSelective",check);
    }

    //dealStatus 0待处理，1通过，2不通过
    @Override
    @Transactional
    public void update(Integer id,Byte dealStatus,String dealCause){
        HashMap<String, Object> params = new HashMap<>();
        params.put("id",id);
        params.put("dealStatus",dealStatus);
        ParameterHandlerUtils.put(params,"dealCause",dealCause,"");
        params.put("dealTime",new Date());
        ParameterHandlerUtils.put(params,"dealUserId",null,"");
        daoMyBatis.save("com.example.dubei.activity.mapper.ActivityCheckMapper.updateByPrimaryKeySelective",params);
    }

    @Override
    public void updateDealStatusGetd(Integer userId) {
        ValidateUtils.notNull(userId,"userId can not be null");
        daoMyBatis.update("com.example.dubei.activity.mapper.ActivityCheckMapper.updateDealStatusGetd",userId);
    }
}
