package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 蛇的身体
 * @author: fangshaolei
 * @time: 2023/3/21 13:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cell {
    private Integer x;
    private Integer y;
}
