package com.example.dubei.activity.util;

import com.example.dubei.activity.constant.Constant;

import java.io.File;
import java.util.List;

public class PicUtils {
    //判断图片是否采用base64上传
    public static boolean validatePicUpload(List<String> imgList){
        if(imgList.get(0).contains("base64,")){
            return true;
            //说明是图片的路径,不需要更新该字段
        }else if(imgList.get(0).contains("https:")){
            return false;
        }else{
            throw new RuntimeException("图片上传格式错误");
        }
    }
}
