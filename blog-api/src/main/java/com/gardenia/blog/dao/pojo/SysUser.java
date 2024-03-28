package com.gardenia.blog.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysUser {

    //@TableId(type = IdType.ASSIGN_ID)      //默认Id类型就是雪花算法
    //就使用默认雪花算法即可，是分布式id，以后用户多了，要进行分表操作，id还是得用分布式id，可以保证在不同机器上你的id还是各不相同
    private Long id;

    private String account;

    private Integer admin;

    private String avatar;

    private Long createDate;

    private Integer deleted;

    private String email;

    private Long lastLogin;

    private String mobilePhoneNumber;

    private String nickname;

    private String password;

    private String salt;

    private String status;
}

