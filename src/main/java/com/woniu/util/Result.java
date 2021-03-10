package com.woniu.util;

import lombok.Data;

@Data
public class Result<T> {
    private String message;
    private Integer code;
    private boolean flag;
    private T data;

    public Result(){
        this.message="请求成功";
        this.flag=true;
        this.code=2000;
    }

    public Result(T data){
        this.message="请求成功";
        this.flag=true;
        this.code=2000;
        this.data=data;
    }
    public Result(String message){
        this.message=message;
        this.flag=true;
        this.code=2000;
        this.data=null;
    }

    public Result(boolean flag){
        this.flag=flag;
    }

    public Result(String message, boolean flag){
        this.message=message;
        this.flag=flag;
        this.code=5000;
    }
    public Result(String message, boolean flag,Integer code){
        this.message=message;
        this.flag=flag;
        this.code=code;
        this.data=null;
    }
    public Result(String message, boolean flag, Integer code, T data){
        this.message=message;
        this.flag=flag;
        this.code=code;
        this.data=data;
    }
}
