package com.gardenia.blog.dao.pojo;

import lombok.Data;
//类别表
//avata分类图标路径
//category_name图标分类的名称
//description分类的描述
@Data
public class Category {

    private Long id;

    private String avatar;

    private String categoryName;

    private String description;
}

