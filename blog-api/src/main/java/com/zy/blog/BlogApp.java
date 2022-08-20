package com.zy.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 张岩
 * @version 1.0
 */
@SpringBootApplication
@MapperScan("com.zy.blog.dao.mapper")
public class BlogApp {
    public static void main(String[] args) {
        SpringApplication.run(BlogApp.class,args);
    }
}
