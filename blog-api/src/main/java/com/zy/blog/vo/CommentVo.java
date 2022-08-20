package com.zy.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @author 张岩
 * @version 1.0
 */
@Data
public class CommentVo {
   //@JsonSerialize(using = ToStringSerializer.class)
    private String id;
    //评论作者
    private UserVo author;
    private String content;
    private List<CommentVo> childrens;
    private String createDate;
    private Integer level;
    //当为二级评论时，表示被评论人
    private UserVo toUser;

}
