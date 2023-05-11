package com.fckey.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname Bot
 * @description: None
 * @author: Jeff Fong
 * @create: 2023/05/11 17:56
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot {
    private Integer userId;
    private String botCode;
    private String botInput;
}