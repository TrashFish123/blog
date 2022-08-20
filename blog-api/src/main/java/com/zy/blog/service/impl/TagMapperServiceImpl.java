package com.zy.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import com.zy.blog.dao.mapper.TagMapper;
import com.zy.blog.dao.pojo.Tag;
import com.zy.blog.service.TagService;
import com.zy.blog.vo.Result;
import com.zy.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author 张岩
 * @version 1.0
 */
@Service
public class TagMapperServiceImpl implements TagService {
    @Autowired
    TagMapper tagMapper;
    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    @Override
    public Result hots(int limit) {
        List<Long> tagIds=tagMapper.findHotsTagIds(limit);
        if(CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
        List<TagVo> tagsList = tagMapper.findTagsByIds(tagIds);

        return Result.success(tagsList);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId, Tag::getTagName);
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findADetailById(Long id) {
        Tag tag = tagMapper.selectById(id);

        return Result.success(copy(tag));
    }

    //copyList实现
    private List<TagVo> copyList(List<Tag> tags) {
        ArrayList<TagVo> tagVos = new ArrayList<>();
        for(Tag tag : tags){
            tagVos.add(copy(tag));
        }
        return tagVos;
    }
    private TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        //BeanUtils,copyProperties用于类之间的复制，相同字段复制，不同字段为null
        BeanUtils.copyProperties(tag,tagVo);
        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }

}
