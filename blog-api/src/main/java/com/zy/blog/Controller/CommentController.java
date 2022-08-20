package com.zy.blog.Controller;

import com.zy.blog.service.CommentService;
import com.zy.blog.vo.Result;
import com.zy.blog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 张岩
 * @version 1.0
 */
@RestController
@RequestMapping("comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id")Long articleId){
        return commentService.commonsByArticleId(articleId);
    }
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentService.comment(commentParam);
    }



}
