package com.gardenia.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gardenia.blog.dao.mapper.SysUserMapper;
import com.gardenia.blog.dao.pojo.SysUser;
import com.gardenia.blog.service.LoginService;
import com.gardenia.blog.service.SysUserService;
import com.gardenia.blog.utils.JWTUtils;
import com.gardenia.blog.vo.ErrorCode;
import com.gardenia.blog.vo.LoginUserVo;
import com.gardenia.blog.vo.Result;
import com.gardenia.blog.vo.UserVo;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/10 - 13:23
 */
@Service
@Accessors(chain = true)
public class SysUserServiceImpl implements SysUserService {
   @Autowired
   private SysUserMapper sysUserMapper;

   @Autowired
   private RedisTemplate<String,String> redisTemplate;

   @Autowired
   private LoginService loginService;

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("数据库还没设置对应作者呢！");
        }

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));

        return userVo;
    }

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("数据库还没设置对应作者呢！");
        }

        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(SysUser::getAccount,account)
                .eq(SysUser::getPassword,password)
                .select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname)
                .last("limit 1");   //只查询一条就停止往下查,增加查询效率，

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1. token合法性校验
         *    是否为空，解析是否成功，redis是否存在
         * 2. 如果校验失败，返回错误
         * 3. 如果成功，返回对应的结果 LoginUserVo
         */
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }

        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setId(String.valueOf(sysUser.getId()));
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setNickname(sysUser.getNickname());

        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account).last("limit 1");   //只查询一条就停止往下查,增加查询效率，

        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        //保存用户的id会自动生成
        //这个地方 默认生成的id是 分布式id 雪花算法
        //mybatis-plus
        sysUserMapper.insert(sysUser);
    }
}
