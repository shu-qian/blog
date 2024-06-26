package com.gardenia.blog.handler;

import com.alibaba.fastjson.JSON;
import com.gardenia.blog.dao.pojo.SysUser;
import com.gardenia.blog.service.LoginService;
import com.gardenia.blog.utils.UserThreadLocal;
import com.gardenia.blog.vo.ErrorCode;
import com.gardenia.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/13 - 19:54
 */
@Component
@Slf4j  //lombok还可以做日志
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在执行controller方法(handler)之前执行
        /**
         * 1. 需要判断 请求的接口路径 是否为handlerMethod (controller方法)
         * 2. 判断token是否为空，如果为空 未登录
         * 3. 如果token不为空，登录验证loginService checkToken
         * 4. 认证成功则放行
         */
        if (!(handler instanceof HandlerMethod)){
            //handler 可能是 RequestResourceHandler 访问静态资源，这种直接放行
            //springboot 程序 访问静态资源 默认去classpath下的static目录去查询

            return true;
        }

        String token = request.getHeader("Authorization");

        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");


        if (StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        //登录验证成功，放行
        //我希望在controller中直接获取用户的信息，如何获取？
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果不删除 ThreadLocal中用完的信息 会有内存泄漏的风险！很有技术含量的！
        UserThreadLocal.remove();
    }
}
