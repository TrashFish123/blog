package com.zy.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zy.blog.dao.mapper.CommentMapper;
import com.zy.blog.dao.pojo.Comment;
import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.service.CommentService;
import com.zy.blog.service.SysUserService;
import com.zy.blog.utils.UserThreadLocal;
import com.zy.blog.vo.CommentVo;
import com.zy.blog.vo.Result;
import com.zy.blog.vo.UserVo;
import com.zy.blog.vo.params.CommentParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张岩
 * @version 1.0
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result commonsByArticleId(Long articleId) {
        /*
         * 1、根据文章id查询评论列表，在comment表中查询
         * 2、根据作者id查询作者信息
         * 3、如果level=1,查询是否有子评论
         * 4、如果有，根据评论id查询子评论
         * */
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, articleId)
                .eq(Comment::getLevel, 1);
        List<Comment> commentList = commentMapper.selectList(wrapper);
        List<CommentVo> commentVos = copyList(commentList);
        return Result.success(commentVos);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);


    }

    private List<CommentVo> copyList(List<Comment> commentList) {
        ArrayList<CommentVo> commentVos = new ArrayList<>();
        for (Comment comment : commentList) {
            commentVos.add(copy(comment));
        }
        return commentVos;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        //将相同属性进行copy
        BeanUtils.copyProperties(comment, commentVo);
        commentVo.setId(String.valueOf(comment.getId()));
        //时间格式化
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //子评论
        Integer level = comment.getLevel();
        if (1 == level) {
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        //to User 给谁评论
        if (level > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;

    }

    private List<CommentVo> findCommentByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id)
                .eq(Comment::getLevel, 2);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        return copyList(comments);
    }

}
