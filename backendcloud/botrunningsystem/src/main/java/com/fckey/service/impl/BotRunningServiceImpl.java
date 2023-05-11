package com.fckey.service.impl;

import com.fckey.service.BotRunningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname BotRunningServiceImpl
 * @description: None
 * @author: Jeff Fong
 * @create: 2023/05/11 14:48
 **/
@Service
public class BotRunningServiceImpl implements BotRunningService {
    private static final Logger log = LoggerFactory.getLogger(BotRunningServiceImpl.class);
    /** 
     * @author Jeff Fong
     * @description 添加bot代码
     * @date 2023/5/11 14:48
     * @param: userId 当前用户的id
     * @param: botCode 用户要执行的代码
     * @param: input 当前的输入，障碍物，墙和蛇
     * @return java.lang.String
     **/
    @Override
    public String addBot(Integer userId, String botCode, String input) {
        log.info("add bot user id = {}, bot code = {} , input = {}", userId, botCode, input);
        return "add bot success";
    }
}