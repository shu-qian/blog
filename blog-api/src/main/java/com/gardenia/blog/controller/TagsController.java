package com.gardenia.blog.controller;

import com.gardenia.blog.dao.pojo.Tag;
import com.gardenia.blog.service.ArticleService;
import com.gardenia.blog.service.TagService;
import com.gardenia.blog.vo.Result;
import com.gardenia.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/9 - 22:35
 */

@RestController
@RequestMapping("tags")
public class TagsController {

    @Autowired
    private TagService tagService;

    // /tags/hot
    @GetMapping("hot")
    public Result hot(){
        int limit = 6;
        return tagService.hots(limit);
    }

    @GetMapping
    public Result findAll(){
        return tagService.findAll();
    }

    @GetMapping("detail")
    public Result findAllDetail(){
        return tagService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id){
        return tagService.findDetailById(id);
    }
}
