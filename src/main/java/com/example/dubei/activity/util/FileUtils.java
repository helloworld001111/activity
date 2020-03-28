package com.example.dubei.activity.util;

import com.example.dubei.activity.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileUtils {
    //复制文件到指定目录，生成随机文件名并返回
    public static List<String> copyFiles(List<MultipartFile> files, File folder) throws IOException {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        ArrayList<String> list = new ArrayList<>();
        for (MultipartFile file : files) {
            String random = RandomUtils.random();
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String newFileName = random + "." + extension;
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(new File(folder, newFileName)));
            list.add(newFileName);
        }
        return list;
    }

    public static List<String> copyFiles(List<MultipartFile> files, File folder,String urlPrefix) throws IOException {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        ArrayList<String> list = new ArrayList<>();
        for (MultipartFile file : files) {
            String random = RandomUtils.random();
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String newFileName = random + "." + extension;
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(new File(folder, newFileName)));
            list.add(urlPrefix+newFileName);
        }
        return list;
    }

    public static String copyFiles(MultipartFile file, File folder) throws IOException {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String random = RandomUtils.random();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName = random + "." + extension;
        FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(new File(folder, newFileName)));
        return newFileName;
    }

    public static List<String> base64UpLoad(String base64Data, File folder, String urlPrefix) {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        List<String> result = new ArrayList<String>();
        ArrayList<String> tempDataPrixList = new ArrayList<>();
        ArrayList<String> tempDataList = new ArrayList<>();
        try {
            String dataPrix = "";
            String data = "";
            if (base64Data == null || "".equals(base64Data)) {
                throw new Exception("上传失败，上传图片数据为空");
            } else {
                String[] d = base64Data.split("base64,");
                for (int i = 0; i < d.length; i++) {
                    if (d != null && d.length % 2 == 0) {
                        dataPrix = d[i++];
                        tempDataPrixList.add(dataPrix);
                        data = d[i++];
                        tempDataList.add(data);
                    } else {
                        throw new Exception("上传失败，数据不合法");
                    }
                }
            }
            for (int i = 0; i < tempDataPrixList.size(); i++) {
                String suffix = tempDataPrixList.get(i);
                String picData = tempDataList.get(i);
                if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {//base64编码的jpeg图片数据
                    suffix = ".jpg";
                } else if ("data:image/x-icon;".equalsIgnoreCase(dataPrix)) {//base64编码的icon图片数据
                    suffix = ".ico";
                } else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {//base64编码的gif图片数据
                    suffix = ".gif";
                } else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {//base64编码的png图片数据
                    suffix = ".png";
                } else {
                    throw new Exception("上传图片格式不合法");
                }
                String tempFileName = RandomUtils.random() + "." + suffix;
                result.add(urlPrefix + tempFileName);
                //因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
                byte[] bs = Base64Utils.decodeFromString(picData);
                try {
                    //使用apache提供的工具类操作流
                    FileUtils.writeByteArrayToFile(new File(folder, tempFileName), bs);
                } catch (Exception ee) {
                    throw new Exception("上传失败，写入文件失败，" + ee.getMessage());
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //全部是png文件
    public static List<String> base64UpLoadPng(List<String> imgList, File folder, String urlPrefix) {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < imgList.size(); i++) {
            try {
                String base64Data = imgList.get(i);
                String dataPrix = "";
                String picData = "";
                if (base64Data == null || "".equals(base64Data)) {
                    throw new Exception("上传失败，上传图片数据为空");
                } else {
                    String[] d = base64Data.split("base64,");
                    if (d != null && d.length == 2) {
                        dataPrix = d[0];
                        picData = d[1];
                        String suffix = null;
                        if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {//base64编码的jpeg图片数据
                            suffix = ".jpg";
                        } else if ("data:image/x-icon;".equalsIgnoreCase(dataPrix)) {//base64编码的icon图片数据
                            suffix = ".ico";
                        } else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {//base64编码的gif图片数据
                            suffix = ".gif";
                        } else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {//base64编码的png图片数据
                            suffix = ".png";
                        } else {
                            throw new Exception("上传图片格式不合法");
                        }
                        String tempFileName = RandomUtils.random() + suffix;
                        byte[] bs = Base64Utils.decodeFromString(picData);
                        try {
                            //使用apache提供的工具类操作流
                            FileUtils.writeByteArrayToFile(new File(folder, tempFileName), bs);
                            result.add(urlPrefix + tempFileName);
                        } catch (Exception ee) {
                            throw new Exception("上传失败，写入文件失败，" + ee.getMessage());
                        }
                    } else {
                        throw new Exception("上传失败，数据不合法");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return result;
    }

    public static void writeByteArrayToFile(File file, byte[] bys) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bys);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
