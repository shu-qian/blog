package com.gardenia.blog.vo;

import lombok.Data;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/9 - 23:32
 */
//vo:和页面交互的对象
@Data
public class TagVo {
    private String id;

    private String tagName;

    private String avatar;
}
