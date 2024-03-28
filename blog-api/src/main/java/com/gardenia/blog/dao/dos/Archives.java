package com.gardenia.blog.dao.dos;

import lombok.Data;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/10 - 15:45
 *
 * 没有get,set方法，mapper就不好使了！给不出数据
 */
//dt 也是数据库查出来的对象，但不需要持久化
//由于do是关键字所以加了个s成为dos
@Data
public class Archives {
    private Integer year;

    private Integer month;

    private Long count;
}
