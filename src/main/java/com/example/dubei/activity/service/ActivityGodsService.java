package com.example.dubei.activity.service;

import com.example.dubei.activity.pojo.ActivityGods;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ActivityGodsService {

    List<Map<String,Object>> queryListByCondition(Byte isEffective,String godsName);

    List<Map<String,Object>> queryListByCondition(Byte isEffective,String godsName,int start,int pageSize);

    Integer queryCountByCondition(Byte isEffective,String godsName);

    void save(ActivityGods activityGods);

    void upload(List<String> imgList,Integer godsId,String godsName,String godsDesc,Float godsPrice,Integer godsNumber,Byte isShareGroupGet) throws IOException;

//    void upload(List<MultipartFile> files, Integer godsId, String godsName, String godsDesc, Float godsPrice, Integer godsNumber, Byte isShareGroupGet) throws IOException;

    void update(ActivityGods activityGods);

    //让当前在用的商品失效
    void effectById(Integer godsId,Byte isEffective);

    void decreaseGodsNumber(Integer id);

    ActivityGods queryById(Integer id);

    void deleteById(Integer godsId);
}
