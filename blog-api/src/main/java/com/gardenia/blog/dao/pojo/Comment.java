package com.gardenia.blog.dao.pojo;

import lombok.Data;

/**
 * 评论表
 * id评论id
 * content评论内容
 * create_date评论时间
 * article_id评论文章
 * author_id谁评论的
 * parent_id盖楼功能对评论的评论进行回复
 * to_uid给谁评论
 * level评论的是第几层（1级表示最上层的评论，2表示对评论的评论）
 */
@Data
public class Comment {

    private Long id;

    private String content;

    private Long createDate;

    private Long articleId;

    private Long authorId;

    private Long parentId;

    private Long toUid;

    private Integer level;
}

