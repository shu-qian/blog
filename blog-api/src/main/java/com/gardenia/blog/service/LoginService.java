package com.gardenia.blog.service;

import com.gardenia.blog.dao.pojo.SysUser;
import com.gardenia.blog.vo.Result;
import com.gardenia.blog.vo.params.LoginParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/13 - 14:05
 */
@Transactional  //不加事务redis出错注册还是能插入
public interface LoginService {
    /**
     * 登录功能
     * 登录成功会生成token传回前端
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    /**
     * 退出登录
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册
     * @param loginParam
     * @return
     */
    Result register(LoginParam loginParam);
}
