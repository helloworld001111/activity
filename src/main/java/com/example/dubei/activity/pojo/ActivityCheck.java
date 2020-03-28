package com.example.dubei.activity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class ActivityCheck {
    private Integer id;

    private Integer userId;

    private String openId;

    private String picUrl;

    private Date uploadTime;

    private Byte dealStatus;

    private String dealCause;

    private Date dealTime;

    private String dealUserId;

    }