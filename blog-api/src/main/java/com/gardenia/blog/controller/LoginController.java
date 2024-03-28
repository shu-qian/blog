package com.gardenia.blog.controller;

import com.gardenia.blog.service.LoginService;
import com.gardenia.blog.vo.Result;
import com.gardenia.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/13 - 14:03
 */
@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;


    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        //登录 验证用户 访问用户表，但是直接把SysUserService搞进来不好，不是User的业务
        return loginService.login(loginParam);
    }
}
