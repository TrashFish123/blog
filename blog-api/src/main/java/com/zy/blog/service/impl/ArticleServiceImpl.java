package com.zy.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zy.blog.dao.dos.Archives;
import com.zy.blog.dao.mapper.ArticleBodyMapper;
import com.zy.blog.dao.mapper.ArticleMapper;

import com.zy.blog.dao.mapper.ArticleTagMapper;
import com.zy.blog.dao.pojo.Article;
import com.zy.blog.dao.pojo.ArticleBody;
import com.zy.blog.dao.pojo.ArticleTag;
import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.service.*;
import com.zy.blog.utils.UserThreadLocal;
import com.zy.blog.vo.ArticleBodyVo;
import com.zy.blog.vo.ArticleVo;
import com.zy.blog.vo.Result;
import com.zy.blog.vo.TagVo;
import com.zy.blog.vo.params.ArticleParam;
import com.zy.blog.vo.params.PageParams;
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
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result listArticle(PageParams pageParams) {

        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = this.articleMapper.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));
    }
        //分页查询
//    @Override
//    public Result listArticle(PageParams pageParams) {
//        //分页
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
//        if (pageParams.getCategoryId()!=null) {
//            //and category_id=#{categoryId}
//            wrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if(pageParams.getTagId()!=null){
//            //加入标签条件查询
//            //article表中并没有tag字段 一篇文章有多个标签
//            //articie_tog article_id 1：n tag_id
//            //我们需要利用一个全新的属于文章标签的queryWrapper将这篇文章的article_Tag查出来，保存到一个list当中。
//            // 然后再根据queryWrapper的in方法选择我们需要的标签即可。
//
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleTags.size() > 0) {
//                // and id in(1,2,3)
//                wrapper.in(Article::getId,articleIdList);
//            }
//
//        }
//        wrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, wrapper);
//        List<Article> records = articlePage.getRecords();
//        List<ArticleVo> articleVo = copyList(records, true, true);
//
//        return Result.success(articleVo);
//
//    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false, false));
    }

    /**
     * 最新文章
     *
     * @param limit
     * @return
     */
    @Override
    public Result newsArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle, Article::getCreateDate);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result findArticleById(Long id) {
        /*
         * 1、根据id获得article对象
         * 2、根据bodyId和categoryId去做关联查询
         * */
        Article article = this.articleMapper.selectById(id);
        /**
         * 查完文章了，新增阅读数，有没有问题呢？
         * 答案是是有的，本应该直接返回数据，这时候做了一个更新操作，更新时间时加写锁，阻塞其他的读操作，新能就会比较低，
         * 而且更新增加了此次接口的耗时，一旦更新出问题，不能影响我们其他的如：看文章呀什么的
         * 那要怎么样去优化呢？，---->想到了线程池
         * 可以把更新操作扔到线程池里面，就不会影响了,和主线程就不相关了
         */
        threadService.updateArticleViewCount(articleMapper, article);
        return Result.success(copy(article, true, true, true, true));
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        //注意想要拿到数据必须将接口加入拦截器
        SysUser sysUser = UserThreadLocal.get();
        /**
         * 1. 发布文章 目的 构建Article对象
         * 2. 作者id  当前的登录用户
         * 3. 标签  要将标签加入到 关联列表当中
         * 4. body 内容存储 article bodyId
         */
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        //插入之后 会生成一个文章id（因为新建的文章没有文章id所以要insert一下
        //官网解释："insart后主键会自动'set到实体的ID字段。所以你只需要"getid()就好
//        利用主键自增，mp的insert操作后id值会回到参数对象中
        //https://blog.csdn.net/HSJ0170/article/details/107982866
        this.articleMapper.insert(article);
        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(Long.parseLong(tag.getId()));
                this.articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);
        //插入完之后再给一个id
        article.setBodyId(articleBody.getId());
        //MybatisPlus中的save方法什么时候执行insert，什么时候执行update
        // https://www.cxyzjd.com/article/Horse7/103868144
        //只有当更改数据库时才插入或者更新，一般查询就可以了
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        return Result.success(articleVo);
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        ArrayList<ArticleVo> articleVos = new ArrayList<>();
        for (Article article : records) {
            articleVos.add(copy(article, isTag, isAuthor, false, false));
        }
        return articleVos;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArrayList<ArticleVo> articleVos = new ArrayList<>();
        for (Article article : records) {
            articleVos.add(copy(article, isTag, isAuthor, isBody, isCategory));
        }
        return articleVos;
    }


    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article, articleVo);
        //joda包中的DataTime.toString方法将Article的Long日期属性转为ArticleVo中的字符串日期属性
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //是否显示标签和作者
        if (isTag) {
            articleVo.setTags(tagService.findTagsByArticleId(article.getId()));
        }
        if (isAuthor) {
            articleVo.setAuthor(sysUserService.findUserById(article.getAuthorId()).getNickname());
        }
        if (isBody) {
            articleVo.setBody(findArticleBodyById(article.getBodyId()));
        }
        if (isCategory) {
            articleVo.setCategory(categoryService.findCategoryById(article.getCategoryId()));
        }
        return articleVo;
    }

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }


}


