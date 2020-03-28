package com.example.dubei.activity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class ActivityUser {
    private Integer id;

    private String openId;

    private String wechatNickName;

    private String realName;

    private String phoneNumber;

    private String address;

    private Date lastGetTime;

    private Integer lastGetGodsId;

    private Byte usableSignInCount;

    private Byte isDishonest;

    private String remark;

    private Date registerTime;

    private String wechatPhotoUrl;

    private String lastSignInDate;


}