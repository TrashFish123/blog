package com.zy.blog.service;

import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.vo.Result;
import com.zy.blog.vo.params.LoginParam;

/**
 * @author 张岩
 * @version 1.0
 */
public interface LoginServie {
    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParam loginParam);
}
