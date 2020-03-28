package com.example.dubei.activity.service;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ActivitySlideshowService {

    List<Map<String,Object>> queryListByCondition();

    Integer queryCountByCondition();

    void save(List<String> imgList,String href) throws IOException;

    void deleteById(Integer id);

    void updateById(Integer id,List<String> imgList,String href)  throws IOException;

}
