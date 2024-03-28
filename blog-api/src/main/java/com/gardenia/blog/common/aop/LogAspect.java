package com.gardenia.blog.common.aop;

import com.alibaba.fastjson.JSON;
import com.gardenia.blog.utils.HttpContextUtils;
import com.gardenia.blog.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author sq ♥ovo♥
 * @date 2024/1/1 - 18:22
 */
@Component
@Aspect //切面 定义了通知和切点的关系
@Slf4j
public class LogAspect {

    //切点  在这里就是我们的切点，注解加在哪哪个地方就是切点
    @Pointcut("@annotation(com.gardenia.blog.common.aop.LogAnnotation)")
    public void pt(){}


    //通知类
    //环绕通知，对方法前后都能够增强
    @Around("pt()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行原来的方法
        Object result = joinPoint.proceed();
        //执行时长（毫秒）
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        recordLog(joinPoint,time);

        return result;
    }

    private void recordLog(ProceedingJoinPoint joinPoint, long time) {
        //反射得到方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        log.info("=====================log start================================");
        log.info("module:{}",logAnnotation.module());
        log.info("operator:{}",logAnnotation.operator());

        //请求的方法名
        String classname = joinPoint.getTarget().getClass().getName();
        String methodname = signature.getName();
        log.info("request method:{}",classname+"."+methodname+"()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);
        log.info("params:{}",params);

        //获取request,设置IP地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.info("ip:{}", IpUtils.getIpAddr(request));

        log.info("excute time : {} ms",time);
        log.info("=====================log end================================");
    }
}
