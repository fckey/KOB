package com.fckey.service;

/**
 * @program: backendcloud
 * @className BotRunningService
 * @description: None
 * @author: Jeff Fong
 * @create: 2023/05/11 14:46
 * @Version 1.0
 **/
public interface BotRunningService {
    String addBot(Integer userId, String botCode, String input);
}