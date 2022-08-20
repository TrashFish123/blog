package com.zy.blog.service;

import com.zy.blog.vo.CategoryVo;
import com.zy.blog.vo.Result;

/**
 * @author 张岩
 * @version 1.0
 */
public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoriesDetailById(Long id);
}
