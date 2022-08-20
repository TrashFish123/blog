package com.zy.blog.vo.params;

import lombok.Data;

/**
 * @author 张岩
 * @version 1.0
 */
@Data
public class CommentParam {
    private Long articleId;

    private String content;

    private Long parent;

    private Long toUserId;
}
