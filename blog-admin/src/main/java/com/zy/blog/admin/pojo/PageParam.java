package com.zy.blog.admin.pojo;

import lombok.Data;

/**
 * @author 张岩
 * @version 1.0
 */
@Data
public class PageParam {

    private Integer currentPage;

    private Integer pageSize;

    private String queryString;
}