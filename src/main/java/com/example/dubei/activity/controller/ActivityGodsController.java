package com.example.dubei.activity.controller;

import com.example.dubei.activity.base.pojo.Result;
import com.example.dubei.activity.interceptor.LoginInterceptor;
import com.example.dubei.activity.pojo.ActivityGods;
import com.example.dubei.activity.service.ActivityGodsService;
import com.example.dubei.activity.util.ActionHelperUtils;
import com.example.dubei.activity.util.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("activityGods")
@Slf4j
public class ActivityGodsController {

    @Autowired
    private ActivityGodsService activityGodsService;

    @RequestMapping("queryByCondition")
    public void queryByCondition(Byte isEffective,String godsName,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        Integer count = activityGodsService.queryCountByCondition(isEffective,godsName);
        List<Map<String, Object>> listMap = activityGodsService.queryListByCondition(isEffective,godsName);
        ActionHelperUtils.generateGridJson(count,listMap,response);
    }

    @RequestMapping("queryByConditionPage")
    public void queryByCondition(Byte isEffective,String godsName, int start,int pageSize, HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        Integer count = activityGodsService.queryCountByCondition(isEffective,godsName);
        List<Map<String, Object>> listMap = activityGodsService.queryListByCondition(isEffective,godsName,start,pageSize);
        ActionHelperUtils.generateGridJson(count,listMap,response);
    }

    //新建商品
    @RequestMapping("save")
    @LoginInterceptor
    public void save(@RequestBody Map<String,Object> map,HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();
        System.out.println("start time:"+System.currentTimeMillis());
        log.info(ActionHelperUtils.getRequestParams());
        List<String> imgList = (List<String>)map.get("imgList");
        String godsName = (String)map.get("godsName");
        String godsDesc = (String)map.get("godsDesc");
        Float godsPrice = Float.valueOf((String)map.get("godsPrice"));
        Integer godsNumber = Integer.valueOf((String)map.get("godsNumber"));
        Byte isShareGroupGet = Byte.valueOf((String)map.get("isShareGroupGet"));
        ValidateUtils.notEmpty(imgList,"上传图片不能为空");
        ValidateUtils.notEmpty(godsName,"商品名称不能为空");
        ValidateUtils.notEmpty(godsDesc,"商品描述不能为空");
        ValidateUtils.notNull(godsPrice,"商品价格不能为空");
        ValidateUtils.notNull(godsNumber,"库存数量不能为空");
        ValidateUtils.notNull(isShareGroupGet,"能否通过分享微信群获取字段不能为空");
        activityGodsService.upload(imgList,null,godsName,godsDesc,godsPrice,godsNumber,isShareGroupGet);
        long endTime = System.currentTimeMillis();
        System.out.println("end time:"+System.currentTimeMillis());
        System.out.println("upload base64 cost time:"+(endTime-startTime)); //376
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

//    @RequestMapping("save2")
//    @LoginInterceptor
//    public void save2(List<MultipartFile> files,String godsName,String godsDesc,Float godsPrice,
//                      Integer godsNumber,Byte isShareGroupGet,HttpServletResponse response) throws IOException {
//        long startTime = System.currentTimeMillis();
//        log.info(ActionHelperUtils.getRequestParams());
//        ValidateUtils.notEmpty(files,"上传图片不能为空");
//        ValidateUtils.notEmpty(godsName,"商品名称不能为空");
//        ValidateUtils.notEmpty(godsDesc,"商品描述不能为空");
//        ValidateUtils.notNull(godsPrice,"商品价格不能为空");
//        ValidateUtils.notNull(godsNumber,"库存数量不能为空");
//        ValidateUtils.notNull(isShareGroupGet,"能否通过分享微信群获取字段不能为空");
//        activityGodsService.upload(files,null,godsName,godsDesc,godsPrice,godsNumber,isShareGroupGet);
//        long endTime = System.currentTimeMillis();
//        System.out.println("file upload cost time:"+(endTime-startTime)); //255
//        ActionHelperUtils.generateResult(Result.success(null),response);
//    }

    //修改商品
    @RequestMapping("updateById")
    @LoginInterceptor
    public void updateById(@RequestBody Map<String,Object> map,HttpServletResponse response) throws IOException {
        List<String> imgList = (List<String>)map.get("imgList");
        Integer godsId = Integer.valueOf((String)map.get("godsId"));
        String godsName = (String)map.get("godsName");
        String godsDesc = (String)map.get("godsDesc");
        Float godsPrice = Float.valueOf((String)map.get("godsPrice"));
        Integer godsNumber = Integer.valueOf((String)map.get("godsNumber"));
        Byte isShareGroupGet = Byte.valueOf((String)map.get("isShareGroupGet"));
        ValidateUtils.notEmpty(imgList,"上传图片不能为空");
        ValidateUtils.notEmpty(godsName,"商品名称不能为空");
        ValidateUtils.notEmpty(godsDesc,"商品描述不能为空");
        ValidateUtils.notNull(godsPrice,"商品价格不能为空");
        ValidateUtils.notNull(godsNumber,"库存数量不能为空");
        ValidateUtils.notNull(isShareGroupGet,"能否通过分享微信群获取字段不能为空");
//        activityGodsService.upload(imgList,godsId,godsName,godsDesc,godsPrice,godsNumber,isShareGroupGet);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

    //商品上下架
    @RequestMapping("effectById")
    @LoginInterceptor
    public void loseEffectById(Integer godsId,Byte isEffective,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        ValidateUtils.notNull(godsId,"godsId不能为空");
        ValidateUtils.notNull(isEffective,"isEffective不能为空");
        activityGodsService.effectById(godsId,isEffective);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

    @RequestMapping("deleteById")
    @LoginInterceptor
    public void deleteById(Integer godsId,HttpServletResponse response) throws IOException {
        log.info(ActionHelperUtils.getRequestParams());
        ValidateUtils.notNull(godsId,"godsId不能为空");
        activityGodsService.deleteById(godsId);
        ActionHelperUtils.generateResult(Result.success(null),response);
    }

}
