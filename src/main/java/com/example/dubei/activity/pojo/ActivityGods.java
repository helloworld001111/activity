package com.example.dubei.activity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class ActivityGods {
    private Integer id;

    private String picUrl;

    private String godsName;

    private String godsDesc;

    private Float godsPrice;

    private Integer godsNumber;

    private Date effectStartTime;

    private Date effectEndTime;

    private Byte isEffective;

    private Byte isShareGroupGet;

}