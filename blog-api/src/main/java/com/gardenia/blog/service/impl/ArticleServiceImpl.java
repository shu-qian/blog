package com.gardenia.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gardenia.blog.dao.dos.Archives;
import com.gardenia.blog.dao.mapper.ArticleBodyMapper;
import com.gardenia.blog.dao.mapper.ArticleMapper;
import com.gardenia.blog.dao.mapper.ArticleTagMapper;
import com.gardenia.blog.dao.pojo.Article;
import com.gardenia.blog.dao.pojo.ArticleBody;
import com.gardenia.blog.dao.pojo.ArticleTag;
import com.gardenia.blog.dao.pojo.SysUser;
import com.gardenia.blog.service.*;
import com.gardenia.blog.utils.UserThreadLocal;
import com.gardenia.blog.vo.ArticleBodyVo;
import com.gardenia.blog.vo.ArticleVo;
import com.gardenia.blog.vo.Result;
import com.gardenia.blog.vo.TagVo;
import com.gardenia.blog.vo.params.ArticleParam;
import com.gardenia.blog.vo.params.PageParams;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/9 - 22:48
 */
@Service
@Accessors(chain = true)    //支持链式写法
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
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());

        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());

        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }

    /*@Override
    public Result listArticle(PageParams pageParams) {
        *//**
         * 1. 分页查询 article数据库表
         *//*
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        if (pageParams.getCategoryId() != null){
            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
        }

        List<Long> articleIdList = new ArrayList<>();
        if (pageParams.getTagId() != null){
            //加入标签条件查询
            //article表中并没有tag字段 一篇文章有多个标签
            //articie_tag article_id 1：n tag_id
            //我们需要利用一个全新的属于文章标签的queWrapper将标签所有的article_Tag查出来，保存到一个list当中。
            // 然后再根据queryWrapper的in方法选择我们需要的文章即可。
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);

            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getArticleId());
            }

            if (articleIdList.size() > 0){
                queryWrapper.in(Article::getId,articleIdList);
            }
        }

        //是否置顶进行排序
        //order by create_date desc
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);   //按时间倒序排列

        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);

        List<Article> records = articlePage.getRecords();

        List<ArticleVo> articleVoList = copyList(records,true,true);
        return Result.success(articleVoList);
    }*/

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        //select id,title from article order by view_counts desc limit 5
        queryWrapper.orderByDesc(Article::getWeight,Article::getViewCounts)    //按浏览量从高到低
                    .select(Article::getId,Article::getTitle)
                    .last("limit "+limit);

        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        //select id,title from article order by creat_date desc limit 5
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate)    //按浏览量从高到低
                .select(Article::getId,Article::getTitle)
                .last("limit "+limit);

        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Autowired
    private ThreadService threadService;

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1. 根据id查询 文章信息
         * 2. 根据bodyId和categoryid 去做关联查询
         */
        Article article = this.articleMapper.selectById(articleId);

        ArticleVo articleVo = copy(article, true, true,true,true);

        //查看完文章后，新增阅读数
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁，阻塞其他的读操作，性能就会比较低（没办法解决，增加阅读数必然要加锁）
        //更新增加了此次接口的耗时（考虑减少耗时）如果一旦更新出问题，不能影响查看操作————想到了一个技术 线程池
        //可以把更新操作扔到 线程池中去执行 和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        //注意想要拿到数据必须将接口加入拦截器
        SysUser sysUser = UserThreadLocal.get();
        /**
         * 1. 发布文章 目的 构建Article对象
         * 2. 作者id  当前的登录用户
         * 3. 标签  要将标签加入到 关联列表article-tag当中
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
        //利用主键自增，mp的insert操作后id值会回到参数对象中
        //https://blog.csdn.net/HSJ0170/article/details/107982866
        this.articleMapper.insert(article);

        //tag
        List<TagVo> tags = articleParam.getTags();
        if (tags != null){
            Long articleId = article.getId();

            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);

                //插入完之后再给一个id
                articleTagMapper.insert(articleTag);
            }
        }

        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);  //先插入才会产生id

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);

        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }

    //源码里常用到的重载
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        return copyList(records, isTag, isAuthor,false,false);
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();

        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setId(String.valueOf(article.getId()));

        //如果传的时间是空的会默认给出当前的时间
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

        //并不是所有接口都需要标签，作者信息
        if (isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }

        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }

        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }

        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
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
