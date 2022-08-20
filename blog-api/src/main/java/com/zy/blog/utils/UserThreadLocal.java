package com.zy.blog.utils;

import com.zy.blog.dao.pojo.SysUser;

/**
 * @author 张岩
 * @version 1.0
 */
public class UserThreadLocal {
    //声明为私有，即每个线程有自己的threadLocal
    private UserThreadLocal(){}
    //实例化一个threadlocal的类，使用
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();
    //存入用户信息
    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }
    //得到线程里面得信息
    public static SysUser get(){
       return LOCAL.get();
    }
    //删除用户信息
    public static void remove(){
        LOCAL.remove();
    }
}
