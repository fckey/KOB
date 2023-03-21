package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description: 保存的是对局的记录
 * @author: fangshaolei
 * @time: 2023/3/21 16:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer aId;
    private Integer aSx;
    private Integer aSy;
    private Integer bId;
    private Integer bSx;
    private Integer bSy;
    private String aSteps;
    private String bSteps;
    private String map;
    private String loser;
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;
}
