package com.example.dubei.activity.constant;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class Constant implements InitializingBean {

    @Value("${custome.domain.name}")
    private String domainName;

    @Value("${custome.protocol.name}")
    private String protocolName;

    public static final String SLIDE_SHOW_FOLDER = "slide_show";
    public static final String GODS_FOLDER = "gods";
    public static final String CHECK_FOLDER = "check";
    public static final String WECHAT_PHOTO_FOLDER = "wechat/photo";
    public static  String HTTPS_SLIDE_SHOW_URL;
    public static  String HTTPS_GODS_URL;
    public static String HTTPS_CHECK_URL;
    public static String HTTPS_WECHAT_PHOTO_URL;
    public static String AUTHENTICATION_CALLBACK;

    @Override
    public void afterPropertiesSet() throws Exception {
        HTTPS_SLIDE_SHOW_URL = protocolName+"://"+domainName+"/upload/slide_show/";
        HTTPS_GODS_URL = protocolName+"://"+domainName+"/upload/gods/";
        HTTPS_CHECK_URL = protocolName+"://"+domainName+"/upload/check/";
        HTTPS_WECHAT_PHOTO_URL = protocolName+"://"+domainName+"/upload/wechat/photo/";
        AUTHENTICATION_CALLBACK = protocolName+"://"+domainName+"/wx/callBack";
    }
}
