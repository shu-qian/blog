package com.gardenia.blog.controller;

import com.gardenia.blog.service.LoginService;
import com.gardenia.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("logout")
public class LogoutController {

    @Autowired
    private LoginService loginService;

    //登陆一个的对token进行认证，一个是在redis中进行注册，token字符串没法更改掉，只能由前端进行清除
    //后端能做的就是把redis进行清除
    @GetMapping
    public Result logout(@RequestHeader("Authorization") String token){
        return loginService.logout(token);
    }
}

