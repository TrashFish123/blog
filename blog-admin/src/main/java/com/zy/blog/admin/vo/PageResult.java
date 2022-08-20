package com.zy.blog.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 张岩
 * @version 1.0
 */
@Data
public class PageResult<T> {

    private List<T> list;

    private Long total;
}