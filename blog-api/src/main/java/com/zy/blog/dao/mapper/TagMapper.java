package com.zy.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zy.blog.dao.pojo.Tag;
import com.zy.blog.vo.TagVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 张岩
 * @version 1.0
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id查找标签列表
     * @param
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);


    List<Long> findHotsTagIds(int limit);

    List<TagVo> findTagsByIds(List<Long> tagIds);
}
