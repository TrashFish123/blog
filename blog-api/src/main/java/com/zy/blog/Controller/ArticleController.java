package com.zy.blog.Controller;

import com.zy.blog.common.aop.LogAnnotation;
import com.zy.blog.common.cache.Cache;
import com.zy.blog.service.ArticleService;
import com.zy.blog.vo.Result;
import com.zy.blog.vo.params.ArticleParam;
import com.zy.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 张岩
 * @version 1.0
 */
@RestController//JSON数据交互
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    //分页显示文章列表
    @PostMapping
    @LogAnnotation(module = "文章",operation = "获取文章列表")
    @Cache(expire = 5 * 60 * 1000,name = "listArticle")
    public Result listArticle(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }
    //显示最热文章
    @PostMapping("/hot")
    @Cache(expire = 5 * 60 * 1000,name = "hotArticle")
    public Result hotArticle(){
        int limit = 3;
        return articleService.hotArticle(limit);
    }
    //显示最新文章
    @Cache(expire = 5 * 60 * 1000,name = "newsArticle")
    @PostMapping("/new")
    public Result newsArticle(){
        int limit = 3;
        return articleService.newsArticle(limit);
    }
    //显示文章归档
    @PostMapping("/listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }
    //显示文章详情
    @PostMapping("/view/{id}")
    public Result findArticleById(@PathVariable("id") Long id){
        return articleService.findArticleById(id);
    }
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }

}
