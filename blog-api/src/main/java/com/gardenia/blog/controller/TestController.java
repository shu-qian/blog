package com.gardenia.blog.controller;

import com.gardenia.blog.dao.pojo.SysUser;
import com.gardenia.blog.utils.UserThreadLocal;
import com.gardenia.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}

