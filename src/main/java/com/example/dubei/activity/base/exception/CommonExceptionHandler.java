package com.example.dubei.activity.base.exception;

import com.example.dubei.activity.base.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result handler(Exception e){
        log.error("",e);
        if(e instanceof TokenException){
            return new Result(Result.TOKEN_ERROR,e.getMessage());
        }
        return Result.fail(e.getMessage());
    }
}
