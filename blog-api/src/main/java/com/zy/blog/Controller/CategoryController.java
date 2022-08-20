package com.zy.blog.Controller;

import com.zy.blog.service.CategoryService;
import com.zy.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张岩
 * @version 1.0
 */
@RestController
@RequestMapping("/categorys")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    //查询所有文章分类
    @GetMapping
    public Result findAll(){
        return categoryService.findAll();
    }

    /**
     * 查询所有文章分类的那个标签
     * @return
     */
    @GetMapping("/detail")
    public Result categoriesDetail(){
        return categoryService.findAllDetail();
    }
    @GetMapping("detail/{id}")
    public Result categoriesDetailById(@PathVariable("id") Long id){
        return categoryService.categoriesDetailById(id);
    }



}
