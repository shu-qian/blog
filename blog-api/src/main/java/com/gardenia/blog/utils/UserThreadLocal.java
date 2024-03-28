package com.gardenia.blog.utils;

import com.gardenia.blog.dao.pojo.SysUser;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/13 - 23:42
 *
 * 使用ThreadLocal保存用户登录信息
 * 使用ThreadLocal替代Session完成保存用户登录信息功能
 * 使用ThreadLocal替代Session的好处：
 *    可以在同一线程中很方便的获取用户信息，不需要频繁的传递session对象。
 *
 * 具体实现流程：
 *    在登录业务代码中，当用户登录成功时，生成一个登录凭证存储到redis中，
 *    将凭证中的字符串保存在cookie中返回给客户端。
 *    使用一个拦截器拦截请求，从cookie中获取凭证字符串与redis中的凭证进行匹配，获取用户信息，
 *    将用户信息存储到ThreadLocal中，在本次请求中持有用户信息，即可在后续操作中使用到用户信息。
 */
//线程变量隔离，线程安全的一种解决方案
//ThreadlLocal就是用空间换时间，Synchronized就是时间换空间
public class UserThreadLocal {
    private UserThreadLocal(){}

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    public static SysUser get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }
}
