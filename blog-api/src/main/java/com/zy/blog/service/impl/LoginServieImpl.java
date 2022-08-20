package com.zy.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.service.LoginServie;
import com.zy.blog.service.SysUserService;
import com.zy.blog.utils.JWTUtils;
import com.zy.blog.vo.ErrorCode;
import com.zy.blog.vo.Result;
import com.zy.blog.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 张岩
 * @version 1.0
 */
@Service
public class LoginServieImpl implements LoginServie {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String slat = "mszlu!@#";

    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 检查参数是否合法
         * 根据用户名和密码去user表中查询 是否存在
         * 如果不存在登录失败
         * 如果存在，使用jwt 生成token 返回给前端
         * token放入redis当中，redis,token;user信息设置过期时间
         * 登录认证的时候先认证token字符串是否合法，去redis认证是否存在
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password + slat);
        SysUser sysUser = sysUserService.findUser(account, password);
        if (sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }


        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString((sysUser)), 1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        //token为空的话
        if (StringUtils.isBlank(token)) {
            return null;
        }
        //解析token
        Map<String, Object> checkToken = JWTUtils.checkToken(token);
        //解析为空的话
        if (checkToken == null) {
            return null;
        }
        //判断redis存在token，不存在就是过期了
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (userJson == null) {
            return null;
        }
        //token解析成功
        //JSPN.parseObject将json对象转化为sysUser对象
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);

        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_" + token);
        return Result.success(null);
    }

    /**
     * 思路：
     * 1）判断参数是否合法;
     * 2）判断账户是否已经存在;
     * 3）若合法并且不存在，则创建新用户;
     * 4）生成token;
     * 5）token即用户信息存入redis;
     * 6）注意：在SysServiceImpl中设置事务，一旦出现问题，就回滚;
     * 7）返回给前端token.
     *
     * @param loginParam
     * @return
     */

    @Override
    public Result register(LoginParam loginParam) {
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        //用户参数为空
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(nickname)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser = sysUserService.findUserByAccount(account);
        //用户已经存在
        if (sysUser != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), "账号已经被注册");
        }
        //创建用户,ID默认为自增
        sysUser = new SysUser();
        sysUser.setAccount(account);                                   //账户名
        sysUser.setNickname(nickname);                                  //昵称
        sysUser.setPassword(DigestUtils.md5Hex(password + slat));  //密码加盐md5
        sysUser.setCreateDate(System.currentTimeMillis());              //创建时间
        sysUser.setLastLogin(System.currentTimeMillis());               //最后登录时间
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");              //头像
        sysUser.setAdmin(1);                                             //管理员权限
        sysUser.setDeleted(0);                                             //假删除
        sysUser.setSalt("");                                                //盐
        sysUser.setStatus("");                                              //状态
        sysUser.setEmail("");                                               //邮箱
        this.sysUserService.save(sysUser);
        //生成token
        String token = JWTUtils.createToken(sysUser.getId());
        //token存入redis
        redisTemplate.opsForValue().set("TOKEN_"+token,JSON.toJSONString(sysUser),1,TimeUnit.DAYS);
        return Result.success(token);
    }
}
