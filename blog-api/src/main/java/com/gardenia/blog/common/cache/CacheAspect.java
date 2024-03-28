package com.gardenia.blog.common.cache;

import com.alibaba.fastjson.JSON;
import com.gardenia.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author sq ♥ovo♥
 * @date 2024/3/4 - 20:53
 */
//可当作工具类使用
//统一缓存处理，从内存获取比从磁盘获取快1000倍
//aop 定义一个切面，切面定义了切点和通知的关系
//aop编程，就是用反射实现
@Aspect
@Component
@Slf4j
public class CacheAspect {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //切点，这个注解放在哪个方法就是切点
    @Pointcut("@annotation(com.gardenia.blog.common.cache.Cache)")
    public void pt(){}

    //环绕通知,即在方法的前后进行增强，关联切点
    //相当于实现了简单的springCache
    //找到加了@cache注解的方法，把返回值和相关参数属性放到redis缓存里
    //为了获取到reids中的缓存数据<key, value>。所以必须知道请求的是key是什么，根据缓存名称、类名、方法名、参数来生成唯一的key，将结果存入就行
    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp){
        try {
            Signature signature = pjp.getSignature();
            //类名
            String className = pjp.getTarget().getClass().getSimpleName();
            //调用的方法名
            String methodName = signature.getName();

            Class[] paramterTypes = new Class[pjp.getArgs().length];
            Object[] args = pjp.getArgs();

            //参数
            String params = "";
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null){
                    params += JSON.toJSONString(args[i]);
                    paramterTypes[i] = args[i].getClass();
                }else {
                    paramterTypes[i] = null;
                }
            }

            if (StringUtils.isNotEmpty(params)){
                //加密，以防出现key过长以及字符转义获取不到的情况
                params = DigestUtils.md5Hex(params);
            }

            Method method = pjp.getSignature().getDeclaringType().getMethod(methodName, paramterTypes);
            //获取Cache注解
            Cache annotation = method.getAnnotation(Cache.class);
            //缓存过期时间
            long expire = annotation.expire();
            //缓存名称
            String name = annotation.name();
            //先用key尝试从redis获取
            String redisKey = name + "::" + className + "::" + methodName + "::" + params;
            //获取到的值
            String redisValue = redisTemplate.opsForValue().get(redisKey);

            //如果不是空值
            if (StringUtils.isNotEmpty(redisValue)){
                log.info("走了缓存~~~,{},{}",className,methodName);
                return JSON.parseObject(redisValue, Result.class);  //导致了精度损失问题，Result-data-Object不能像Long一样加ToStringSerializer
            }
            //如果是空值,让它继续执行然后把结果存入redis
            Object proceed = pjp.proceed();
            redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expire));
            log.info("存入缓存~~~,{},{}",className,methodName);
            return proceed;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return Result.fail(-999,"系统错误");
    }
}
