package com.example.dubei.activity.service.impl;

import com.example.dubei.activity.base.jdbc.DaoMyBatis;
import com.example.dubei.activity.constant.Constant;
import com.example.dubei.activity.pojo.ActivityGods;
import com.example.dubei.activity.service.ActivityGodsService;
import com.example.dubei.activity.util.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ActivityGodsServiceImpl implements ActivityGodsService {

    @Autowired
    private DaoMyBatis daoMyBatis;

    @Value("${custome.pic.save.folder}")
    private String picUploadFolder;

    @Override
    public List<Map<String, Object>> queryListByCondition(Byte isEffective,String godsName) {
        Map<String,Object> params = new HashMap<String,Object>();
        ParameterHandlerUtils.put(params,"isEffective",isEffective,1);
        ParameterHandlerUtils.notNullAndEmptyPutWithChar(params,"godsName",godsName,'%');
        return daoMyBatis.queryList("com.example.dubei.activity.mapper.ActivityGodsMapper.queryListByCondition",params);
    }

    @Override
    public List<Map<String, Object>> queryListByCondition(Byte isEffective,String godsName,int start,int pageSize) {
        Map<String,Object> params = new HashMap<String,Object>();
        ParameterHandlerUtils.notNullPut(params,"isEffective",isEffective);
        ParameterHandlerUtils.notNullAndEmptyPutWithChar(params,"godsName",godsName,'%');
        return daoMyBatis.queryPageList("com.example.dubei.activity.mapper.ActivityGodsMapper.queryListByCondition",params,start,pageSize);
    }

    @Override
    public Integer queryCountByCondition(Byte isEffective,String godsName) {
        Map<String,Object> params = new HashMap<String,Object>();
        ParameterHandlerUtils.notNullPut(params,"isEffective",isEffective);
        ParameterHandlerUtils.notNullAndEmptyPutWithChar(params,"godsName",godsName,'%');
        return (Integer)daoMyBatis.queryObject("com.example.dubei.activity.mapper.ActivityGodsMapper.queryCountByCondition",params);
    }

    @Override
    @Transactional
    public void save(ActivityGods activityGods) {
        daoMyBatis.save("com.example.dubei.activity.mapper.ActivityGodsMapper.insertSelective",activityGods);
    }

    //将商品图片保存到服务器，生成访问链接保存到picUrl中。生成商品记录并保存（或修改）
    @Override
    @Transactional
    public void upload(List<String> imgList,Integer godsId, String godsName, String godsDesc, Float godsPrice, Integer godsNumber, Byte isShareGroupGet) throws IOException {
        ActivityGods activityGods = new ActivityGods();
        //包含base64说明是上传的图片
        boolean upload = PicUtils.validatePicUpload(imgList);
        if(upload) {
            List<String> urlList = FileUtils.base64UpLoadPng(imgList, new File(picUploadFolder + File.separator + Constant.GODS_FOLDER), Constant.HTTPS_GODS_URL);
            activityGods.setPicUrl(ConcatUtils.concat(urlList, ','));
        }
        activityGods.setGodsName(godsName);
        activityGods.setGodsDesc(godsDesc);
        activityGods.setGodsPrice(godsPrice);
        activityGods.setGodsNumber(godsNumber);
        activityGods.setEffectStartTime(new Date());
        activityGods.setIsEffective((byte)1);
        activityGods.setIsShareGroupGet(isShareGroupGet);
        if(godsId==null){
            save(activityGods);
        }else{
            activityGods.setId(godsId);
            update(activityGods);
        }
    }

//    @Override
//    @Transactional
//    public void upload(List<MultipartFile> files, Integer godsId, String godsName, String godsDesc, Float godsPrice, Integer godsNumber, Byte isShareGroupGet) throws IOException {
////        List<String> urlList = FileUtils.base64UpLoadPng(imgList,new File(picUploadFolder+File.separator+ Constant.GODS_FOLDER),Constant.HTTPS_GODS_URL);
//        List<String> urlList = FileUtils.copyFiles(files,new File(picUploadFolder+File.separator+ Constant.GODS_FOLDER),Constant.HTTPS_GODS_URL);
//        ActivityGods activityGods = new ActivityGods();
//        activityGods.setPicUrl(ConcatUtils.concat(urlList,','));
//        activityGods.setGodsName(godsName);
//        activityGods.setGodsDesc(godsDesc);
//        activityGods.setGodsPrice(godsPrice);
//        activityGods.setGodsNumber(godsNumber);
//        activityGods.setEffectStartTime(new Date());
//        activityGods.setIsEffective((byte)1);
//        activityGods.setIsShareGroupGet(isShareGroupGet);
//        if(godsId==null){
//            save(activityGods);
//        }else{
//            activityGods.setId(godsId);
//            update(activityGods);
//        }
//    }


    @Override
    @Transactional
    public void update(ActivityGods activityGods) {
        daoMyBatis.update("com.example.dubei.activity.mapper.ActivityGodsMapper.updateByPrimaryKeySelective",activityGods);
    }

    @Override
    @Transactional
    public void effectById(Integer godsId,Byte isEffective) {
        ActivityGods activityGods = new ActivityGods();
        activityGods.setId(godsId);
        activityGods.setIsEffective(isEffective);
        activityGods.setEffectEndTime(new Date());
        daoMyBatis.update("com.example.dubei.activity.mapper.ActivityGodsMapper.updateByPrimaryKeySelective",activityGods);
    }

    @Override
    public void decreaseGodsNumber(Integer id) {
        ActivityGods gods = queryById(id);
        if(gods==null||gods.getGodsNumber()<=0){
            throw new RuntimeException("商品未找到或商品数量已为0");
        }
        daoMyBatis.update("com.example.dubei.activity.mapper.ActivityGodsMapper.decreaseGodsNumber",id);
    }

    @Override
    public ActivityGods queryById(Integer id) {
        return (ActivityGods) daoMyBatis.queryObject("com.example.dubei.activity.mapper.ActivityGodsMapper.selectByPrimaryKey",id);
    }

    @Override
    public void deleteById(Integer id) {
        daoMyBatis.deleteById("com.example.dubei.activity.mapper.ActivityGodsMapper.deleteByPrimaryKey",id);
    }
}
