package com.gardenia.blog.controller;

import com.gardenia.blog.service.SysUserService;
import com.gardenia.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/13 - 15:22
 */
@RestController
@RequestMapping("users")
public class UsersController {
    @Autowired
    private SysUserService sysUserService;

    /**
     * token前端获取到之后，会存储在 storage中 h5 ，本地存储，存储好后，拿到storage中的token去获取用户信息
     * 如果这个接口没实现，他就会一直请求陷入死循环
     *
     * 得从http的head里面拿到这个参数token，这样传参相对来说安全一些
     * @return
     */
    //@RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)；而最常用的使用请求体传参的无疑是POST请求了，所以使用@RequestBody接收数据时，一般都用POST方式进行提交。
    //@RequestHeader获取头部信息
    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        return sysUserService.findUserByToken(token);
    }
}
