package com.zy.blog.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.service.LoginServie;
import com.zy.blog.utils.UserThreadLocal;
import com.zy.blog.vo.ErrorCode;
import com.zy.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 张岩
 * @version 1.0
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginServie loginService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*1、需要判断请求的接口上是否是HandleMethod即controller方法
         * 2、判断token是否为空，为空未登录
         * 3、不为空，登陆验证（通过LoginServiceImpl中的checkToken方法）
         * 4、如果认证成功，则放行
         * */
        if(!(handler instanceof HandlerMethod)){
            //拦截器是拦截的controller中的方法，controller的方法其实就是一个Handler
            //handler可能是RequestResourceHandle(访问资源handle)，即可能是访问静态资源的方法
            //解释：controller对应HandlerMethod，所以拦截器只拦截HandlerMethod
            return true;
        }
        //获取token
        String token = request.getHeader("Authorization");
        //日志问题,需要导入lombok下的@slf4
        log.info("=============request start=================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}",token);
        log.info("=============request end===================");

        //token为空，不放行
        if(StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            //设置返回消息格式
            response.setContentType("application/json;charset=utf8");
            //返回json信息
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //token不为空，去做认证

        SysUser sysUser = loginService.checkToken(token);
        //用户不存在，即认证失败
        if(sysUser == null){
            System.out.println("没有该用户");
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //登陆验证成功，放行
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果不删除会有内存泄露的风险
        UserThreadLocal.remove();
    }
}
