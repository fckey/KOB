package com.fckey.controller;

import com.fckey.service.BotRunningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname BotRunningController
 * @description: None
 * @author: Jeff Fong
 * @create: 2023/05/11 14:50
 **/
@RestController
public class BotRunningController {
    @Autowired
    private BotRunningService botRunningService;
    
    /**
     * @author Jeff Fong
     * @description 加上bot
     * @date 2023/5/11 14:53
     * @param: data
     * @return java.lang.String
     **/
    @PostMapping("/bot/add")
    public String addBot(@RequestParam MultiValueMap<String, String> data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        String botCode = data.getFirst("bot_code");
        String input = data.getFirst("input");
        return botRunningService.addBot(userId, botCode, input);
    }
}