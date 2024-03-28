package com.gardenia.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo  {
    //分布式id 比较长，传到前端 会有精度损失，再传回来pid就错了，必须转为string类型 进行传输，就不会有问题了
    //防止前端 精度损失 把id转为string
    //这里别用错了!!!!用alibaba的不兼容
    //@JsonSerialize(using = ToStringSerializer.class)
    //private Long id;
    private String id;

    private UserVo author;

    private String content;

    private List<CommentVo> childrens;

    private String createDate;

    private Integer level;

    private UserVo toUser;
}

