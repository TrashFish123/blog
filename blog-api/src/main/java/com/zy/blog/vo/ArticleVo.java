package com.zy.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {
    //@JsonSerialize(using = ToStringSerializer.class)
    private String id;

    private String title;

    private String summary;  //简介

    private Integer commentCounts;

    private Integer ViewCounts;

    private Integer weight;   //置顶

    private String createDate;  //创建时间

    private String author;


    private ArticleBodyVo body;

    private List<TagVo> tags;


    private CategoryVo category;

}

