package com.zy.blog.dao.pojo;

import lombok.Data;

/**
 * @author 张岩
 * @version 1.0
 */
@Data
public class ArticleTag {

    private Long id;

    private Long articleId;

    private Long tagId;
}