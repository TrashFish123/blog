package com.zy.blog.dao.pojo;

import lombok.Data;

/**
 * @author 张岩
 * @version 1.0
 */
@Data
public class Category {
    private Long id;

    private String avatar;

    private String categoryName;

    private String description;
}
