package com.zy.blog.handler;

import com.zy.blog.vo.Result;
import org.apache.ibatis.executor.ResultExtractor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 张岩
 * @version 1.0
 */
//对加了Controller进行拦截处理
@ControllerAdvice
public class AllExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result doException(Exception ex){
        ex.printStackTrace();
        return Result.fail(-999,"系统异常");

    }
}
