package com.zy.blog.service;

import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.vo.Result;
import com.zy.blog.vo.UserVo;

/**
 * @author 张岩
 * @version 1.0
 */
public interface SysUserService {
    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

    UserVo findUserVoById(Long id);
}
