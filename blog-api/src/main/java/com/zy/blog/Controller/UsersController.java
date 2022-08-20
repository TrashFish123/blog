package com.zy.blog.Controller;

import com.zy.blog.service.SysUserService;
import com.zy.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张岩
 * @version 1.0
 */
@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        return sysUserService.findUserByToken(token);
    }
}
