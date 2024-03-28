package com.gardenia.blog.service;

import com.gardenia.blog.vo.CategoryVo;
import com.gardenia.blog.vo.Result;

import java.util.List;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/15 - 13:50
 */
public interface CategoryService {

    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoryDetailById(Long id);
}
