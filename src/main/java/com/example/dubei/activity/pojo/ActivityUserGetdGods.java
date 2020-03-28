package com.example.dubei.activity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class ActivityUserGetdGods {
    private Integer id;

    private Integer userId;

    private Integer godsId;

    private String deliverStatus;

    private String receiverAddress;

    private String receiverName;

    private String receiverPhoneNumber;

    private String remark;

    private Date createTime;

    private String transportationId;


}