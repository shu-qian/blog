package com.gardenia.blog.vo.params;

import lombok.Data;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/9 - 22:37
 */
@Data
public class PageParams {
    private int page = 1;

    private int pageSize = 10;

    private Long categoryId;

    private Long tagId;

    private String year;

    private String month;

    //传递6的话变成06
    public String getMonth() {
        if (this.month != null && this.month.length() == 1){
            return "0"+this.month;
        }

        return this.month;
    }
}
