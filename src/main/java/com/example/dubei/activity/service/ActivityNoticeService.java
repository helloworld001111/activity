package com.example.dubei.activity.service;

import java.util.List;
import java.util.Map;

public interface ActivityNoticeService {

    Integer queryCountByCondition(String title);

    List<Map<String,Object>> queryListByCondition(String title);

    void save(String title,String content);

    void update(Integer id,String title,String content);

    void deleteById(Integer id);
}
