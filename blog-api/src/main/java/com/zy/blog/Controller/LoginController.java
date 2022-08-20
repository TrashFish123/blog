package com.zy.blog.Controller;

import com.zy.blog.service.LoginServie;
import com.zy.blog.vo.Result;
import com.zy.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张岩
 * @version 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginServie loginServie;
    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        return loginServie.login(loginParam);
    }
}
