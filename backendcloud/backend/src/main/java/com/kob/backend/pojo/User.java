package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 用户有关的实体类
 * @author: fangshaolei
 * @time: 2023/3/16 14:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

@TableName(value = "user")
public class User {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     *  bot的分数
     */
    private Integer rating;
    /**
     * 用户名
     */
    @TableField("username")
    private String userName;

    /**
     * 密码
     */
    @TableField("password")
    private String passWord;

    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;
}
