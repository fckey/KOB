package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description: bot有关的实体类
 * @author: fangshaolei
 * @time: 2023/3/19 9:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "bot")
public class Bot {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * bot标题
     */
    private String title;

    /**
     * bot的描述
     */
    private String description;
    /**
     * bot的内容
     */
    private String content;
    /**
     *  bot的分数
     */
    private Integer rating;

    /**
     * bot创建的时间
     */

    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    /**
     * bot修改的时间
     */
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date modifyTime;
}
