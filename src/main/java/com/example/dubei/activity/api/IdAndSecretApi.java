package com.example.dubei.activity.api;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IdAndSecretApi implements InitializingBean {
    public static String appID = "wx6899bb0fdf258608";
    public static String appSecret = "a5a593157c04d0da60761432ff83d0b6";
    public static String TOKEN = "123456";

    @Value("${custome.platform.app-id}")
    private String app_id;

    @Value("${custome.platform.app-secret}")
    private String app_secret;

    @Override
    public void afterPropertiesSet() throws Exception {
        appID = app_id;
        appSecret = app_secret;
    }
}
