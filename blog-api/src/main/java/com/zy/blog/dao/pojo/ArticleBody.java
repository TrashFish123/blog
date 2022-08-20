package com.zy.blog.dao.pojo;

import lombok.Data;


/**
 * @author 张岩
 * @version 1.0
 */
@Data
public class ArticleBody {
    private Long id;
    private String content;
    private String contentHtml;
    private Long articleId;
}
