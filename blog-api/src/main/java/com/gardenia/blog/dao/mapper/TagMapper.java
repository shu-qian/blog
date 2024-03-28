package com.gardenia.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gardenia.blog.dao.pojo.Article;
import com.gardenia.blog.dao.pojo.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/9 - 22:29
 *
 * mybatisplus遇到多表查询怎么办:
 * TagMapper的建立中遇到这个问题了，办法是在建立TagMapper后需要建立xml文件进行读写
 * xml文件放到resource文件夹下
 * 文件夹名和xml文件名必须和TagMapper.java文件夹保持一致,注意要用/分隔创建层叠文件夹，用.分隔虽然展示出来一样其实是单个文件夹，会出错且难以排查
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);

    /**
     * 查询最热的标签 前n条
     * @param limit
     * @return
     */
    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
