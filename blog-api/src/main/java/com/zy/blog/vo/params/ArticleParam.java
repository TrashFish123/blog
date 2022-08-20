package com.zy.blog.vo.params;

import com.zy.blog.vo.CategoryVo;
import com.zy.blog.vo.TagVo;
import lombok.Data;

import java.util.List;

/**
 * @author 张岩
 * @version 1.0
 */
@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}
