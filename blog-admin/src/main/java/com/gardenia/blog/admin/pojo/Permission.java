package com.gardenia.blog.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Permission {

    @TableId(type = IdType.AUTO)    //后台管理使用自增id方便一些
    private Long id;

    private String name;

    private String path;

    private String description;
}

