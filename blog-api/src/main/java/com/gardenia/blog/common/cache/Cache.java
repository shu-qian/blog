package com.gardenia.blog.common.cache;

import java.lang.annotation.*;

/**
 * @author sq ♥ovo♥
 * @date 2024/3/4 - 20:50
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    long expire() default 1 * 60 * 1000;
    //缓存标识 key
    String name() default "";
}
