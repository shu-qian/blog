package com.gardenia.blog.controller;

import com.gardenia.blog.common.aop.LogAnnotation;
import com.gardenia.blog.common.cache.Cache;
import com.gardenia.blog.service.ArticleService;
import com.gardenia.blog.vo.Result;
import com.gardenia.blog.vo.params.ArticleParam;
import com.gardenia.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/9 - 22:35
 */

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    //加上此注解，代表要对此接口记录日志
    @LogAnnotation(module="文章",operator="获取文章列表")
    //@Cache(expire = 5 * 60 * 1000,name = "listArticle")若缓存，关于文章的相关信息不能更新，如浏览量和新发布的文章
    public Result listArticle(@RequestBody PageParams pageParams){
//        int i = 10/0;
        return articleService.listArticle(pageParams);
    }

    /**
     * 首页 最热文章
     * @return
     */
    @PostMapping("hot")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticle(){
        int limit =5;
        return articleService.hotArticle(limit);
    }

    /**
     * 首页 最新文章
     * @return
     */
    @PostMapping("new")
    @Cache(expire = 5 * 60 * 1000,name = "new_article")
    public Result newArticles(){
        int limit =5;
        return articleService.newArticles(limit);
    }

    /**
     * 首页 文章归档
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    @PostMapping("/view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }

}
