package com.example.dubei.activity.base.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    public static final int SUCCESS = 200;
    public static final int FAIL = 500;
    public static final int TOKEN_ERROR = 400;
    private int code;
    private Object data;
    public static Result success(Object data){
        return new Result(SUCCESS,data);
    }

    public static Result fail(Object data){
        return new Result(FAIL,data);
    }
}
