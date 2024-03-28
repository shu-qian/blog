package com.gardenia.blog.service;

import com.gardenia.blog.vo.Result;
import com.gardenia.blog.vo.params.CommentParam;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/15 - 19:54
 */
public interface CommentsService {

    /**
     * 根据文章id 查询所有的评论列表
     * @param articleId
     * @return
     */
    Result commentsByArticleId(Long articleId);

    Result comment(CommentParam commentParam);
}
