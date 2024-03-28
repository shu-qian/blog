package com.gardenia.blog.service;

import com.gardenia.blog.vo.Result;
import com.gardenia.blog.vo.TagVo;
import com.gardenia.blog.vo.params.PageParams;

import java.util.List;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/9 - 22:47
 */
public interface TagService {
    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    /**
     * 查询所有文章标签
     * @return
     */
    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}
