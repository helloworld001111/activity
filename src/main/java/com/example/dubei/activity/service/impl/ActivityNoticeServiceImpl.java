package com.example.dubei.activity.service.impl;

import com.example.dubei.activity.base.jdbc.DaoMyBatis;
import com.example.dubei.activity.pojo.ActivityNotice;
import com.example.dubei.activity.service.ActivityNoticeService;
import com.example.dubei.activity.util.ParameterHandlerUtils;
import com.example.dubei.activity.util.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityNoticeServiceImpl implements ActivityNoticeService {
    @Autowired
    private DaoMyBatis daoMyBatis;

    @Override
    public Integer queryCountByCondition(String title){
        HashMap<String, Object> params = new HashMap<String, Object>();
        ParameterHandlerUtils.notNullAndEmptyPutWithChar(params,"title",title,'%');
        return (Integer)daoMyBatis.queryObject("com.example.dubei.activity.mapper.ActivityNoticeMapper.queryCountByCondition",params);
    }

    @Override
    public List<Map<String,Object>> queryListByCondition(String title){
        HashMap<String, Object> params = new HashMap<String, Object>();
        ParameterHandlerUtils.notNullAndEmptyPutWithChar(params,"title",title,'%');
        return daoMyBatis.queryList("com.example.dubei.activity.mapper.ActivityNoticeMapper.queryListByCondition",params);
    }

    @Override
    @Transactional
    public void save(String title,String content){
        ActivityNotice activityNotice = new ActivityNotice();
        activityNotice.setTitle(title);
        activityNotice.setContent(content);
        daoMyBatis.save("com.example.dubei.activity.mapper.ActivityNoticeMapper.insertSelective",activityNotice);
    }

    @Override
    public void update(Integer id, String title, String content) {
        ActivityNotice activityNotice = new ActivityNotice();
        activityNotice.setId(id);
        activityNotice.setTitle(title);
        activityNotice.setContent(content);
        daoMyBatis.update("com.example.dubei.activity.mapper.ActivityNoticeMapper.updateByPrimaryKeySelective",activityNotice);
    }

    @Override
    public void deleteById(Integer id) {
        daoMyBatis.deleteById("com.example.dubei.activity.mapper.ActivityNoticeMapper.deleteByPrimaryKey",id);
    }
}
