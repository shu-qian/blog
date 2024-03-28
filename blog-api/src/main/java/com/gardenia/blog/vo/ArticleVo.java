package com.gardenia.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {

    //一定记得加。否则会出现精度损失
    //@JsonSerialize(using = ToStringSerializer.class)
    //private Long id;
    private String id;

    private String title;

    private String summary;

    /*
        之前Article中的commentCounts，viewCounts，weight 字段为int，会造成更新阅读次数的时候，将其余两个字段设为初始值0
        mybatisplus在更新文章阅读次数的时候虽然只设立了articleUpdate.setviewsCounts(viewCounts+1),
        但是int默认基本数据类型为0，
        mybatisplus但凡不是null就会生成到sql语句中进行更新。
        因此不能使用基本数据类型，而用包裹类型
    */
    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;
    /**
     * 创建时间
     */
    private String createDate;

    private String author;

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private CategoryVo category;

}

