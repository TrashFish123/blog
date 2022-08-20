package com.zy.blog.service;

import com.zy.blog.vo.Result;
import com.zy.blog.vo.params.CommentParam;

/**
 * @author 张岩
 * @version 1.0
 */
public interface CommentService {
    Result commonsByArticleId(Long articleId);

    Result comment(CommentParam commentParam);
}
