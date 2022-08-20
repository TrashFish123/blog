package com.zy.blog.Controller;

import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.utils.UserThreadLocal;
import com.zy.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}

