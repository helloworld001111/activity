package com.example.dubei.activity.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class URLUtils {

    public static String downloadUrlAsPic(String url, File destFolder){
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        String saveName = RandomUtils.random()+".png";
        try{
            URL url1 = new URL(url);
            urlConnection = (HttpURLConnection)url1.openConnection();
            is = urlConnection.getInputStream();
            FileUtils.copyInputStreamToFile(is,new File(destFolder,saveName));
        }catch(Exception e){
            throw new RuntimeException("",e);
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return saveName;
    }
}
