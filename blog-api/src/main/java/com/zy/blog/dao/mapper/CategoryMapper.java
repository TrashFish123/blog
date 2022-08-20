package com.zy.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zy.blog.dao.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 张岩
 * @version 1.0
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
