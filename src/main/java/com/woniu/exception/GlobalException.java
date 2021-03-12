package com.woniu.exception;

import com.woniu.util.Result;
import com.woniu.util.StatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {




//    //兜底的全局异常   其余的遇到异常后将其添加进来
//    @ExceptionHandler(Exception.class)
//    public Result exception(){
//        return new Result("未知异常！",false,20500,null);
//    }
//

}
