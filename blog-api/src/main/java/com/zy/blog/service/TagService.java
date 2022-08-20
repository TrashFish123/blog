package com.zy.blog.service;

import com.zy.blog.vo.Result;
import com.zy.blog.vo.TagVo;

import java.util.List;

/**
 * @author 张岩
 * @version 1.0
 */
public interface TagService {
    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);
/**
 * 查询所有的文章标签
 */
    Result findAll();

    Result findAllDetail();

    Result findADetailById(Long id);
}
