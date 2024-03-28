package com.gardenia.blog.admin.model.params;

import lombok.Data;

/**
 * @author sq ♥ovo♥
 * @date 2024/3/5 - 16:05
 */
@Data
public class PageParam {
    private Integer currentPage;

    private Integer pageSize;

    private String queryString;

}
