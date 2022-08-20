package com.zy.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zy.blog.dao.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 张岩
 * @version 1.0
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
