package com.example.dubei.activity.service.impl;

import com.example.dubei.activity.base.jdbc.DaoMyBatis;
import com.example.dubei.activity.constant.Constant;
import com.example.dubei.activity.pojo.ActivitySlideshow;
import com.example.dubei.activity.service.ActivitySlideshowService;
import com.example.dubei.activity.util.FileUtils;
import com.example.dubei.activity.util.ParameterHandlerUtils;
import com.example.dubei.activity.util.RandomUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ActivitySlideshowServiceImpl implements ActivitySlideshowService {

    @Autowired
    private DaoMyBatis daoMyBatis;

    @Value("${custome.pic.save.folder}")
    private String picUploadFolder;

    @Override
    public List<Map<String, Object>> queryListByCondition() {
        return daoMyBatis.queryList("com.example.dubei.activity.mapper.ActivitySlideshowMapper.queryAll",null);
    }

    @Override
    public Integer queryCountByCondition() {
        return (Integer)daoMyBatis.queryObject("com.example.dubei.activity.mapper.ActivitySlideshowMapper.queryCount",null);
    }

    @Override
    @Transactional
    public void save(List<String> imgList, String href) throws IOException {
        List<String> urlList = FileUtils.base64UpLoadPng(imgList,new File(picUploadFolder+File.separator+Constant.SLIDE_SHOW_FOLDER),Constant.HTTPS_SLIDE_SHOW_URL);
//        String newFileName = FileUtils.copyFiles(file,new File(picUploadFolder+File.separator+Constant.SLIDE_SHOW_FOLDER));
        save(urlList.get(0),href);
    }

    public void deleteAll(){
        daoMyBatis.delete("com.example.dubei.activity.mapper.ActivitySlideshowMapper.deleteAll",null);
    }

    public void save(String picUrl,String hrefLink){
        HashMap<String, Object> params = new HashMap<>();
        ParameterHandlerUtils.notNullAndEmptyPut(params,"picUrl",picUrl);
        ParameterHandlerUtils.notNullAndEmptyPut(params,"hrefLink",hrefLink);
        daoMyBatis.save("com.example.dubei.activity.mapper.ActivitySlideshowMapper.insertSelective",params);
    }

    @Override
    public void deleteById(Integer id) {
        daoMyBatis.deleteById("com.example.dubei.activity.mapper.ActivitySlideshowMapper.deleteByPrimaryKey",id);
    }

    @Override
    public void updateById(Integer id,List<String> imgList,String href) throws IOException {
//        String newFileName = FileUtils.copyFiles(file,new File(picUploadFolder+File.separator+Constant.SLIDE_SHOW_FOLDER));
        List<String> urlList = FileUtils.base64UpLoadPng(imgList,new File(picUploadFolder+File.separator+Constant.SLIDE_SHOW_FOLDER),Constant.HTTPS_SLIDE_SHOW_URL);
        ActivitySlideshow activitySlideshow = new ActivitySlideshow();
        activitySlideshow.setPicUrl(urlList.get(0));
        activitySlideshow.setHrefLink(href);
        activitySlideshow.setId(id);
        daoMyBatis.update("com.example.dubei.activity.mapper.ActivitySlideshowMapper.updateByPrimaryKeySelective",activitySlideshow);
    }
}
