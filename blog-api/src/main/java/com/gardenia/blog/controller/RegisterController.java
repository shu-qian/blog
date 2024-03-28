package com.gardenia.blog.controller;


import com.gardenia.blog.service.LoginService;
import com.gardenia.blog.vo.Result;
import com.gardenia.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private LoginService loginService;

    //后端传递多个参数，前端只选用其需要的参数就可以了
    @PostMapping
    public Result register(@RequestBody LoginParam loginParam){
        //sso 单点登录，后期可以把登录注册功能 提出去（作为单独的服务，可以独立提供接口服务）
        return loginService.register(loginParam);
    }
}

