package com.gardenia.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gardenia.blog.dao.dos.Archives;
import com.gardenia.blog.dao.pojo.Article;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/9 - 22:29
 */
@Repository //dao层的，也可以用万能的component被spring接管
public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArchives();

    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);

}
