package com.zy.blog.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zy.blog.dao.mapper.ArticleMapper;
import com.zy.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author 张岩
 * @version 1.0
 */
@Component
public class ThreadService {
    //期望此操作在线程池 不会影响原来的执行不会影响原来的主线程
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        Integer viewCounts = article.getViewCounts();
        Article article1update = new Article();
        article1update.setViewCounts(viewCounts+1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId())
                     .eq(Article::getViewCounts,article.getViewCounts());
        articleMapper.update(article1update,updateWrapper);

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
