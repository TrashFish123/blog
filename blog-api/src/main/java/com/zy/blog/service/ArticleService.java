package com.zy.blog.service;

import com.zy.blog.dao.pojo.Article;
import com.zy.blog.vo.Result;
import com.zy.blog.vo.params.ArticleParam;
import com.zy.blog.vo.params.PageParams;

import java.util.List;

/**
 * @author 张岩
 * @version 1.0
 */
public interface ArticleService {
    Result listArticle(PageParams pageParams);

    Result hotArticle(int limit);

    Result newsArticle(int limit);

    Result listArchives();

    Result findArticleById(Long id);
    /**
     * 文章发布服务
     * @param articleParam
     * @return
     */

    Result publish(ArticleParam articleParam);
}
