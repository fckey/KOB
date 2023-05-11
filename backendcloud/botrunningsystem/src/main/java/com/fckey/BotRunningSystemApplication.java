package com.fckey;

import com.fckey.service.impl.BotRunningServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname BotRunningSystemApplication
 * @description: None
 * @author: Jeff Fong
 * @create: 2023/05/11 14:42
 **/
@SpringBootApplication
public class BotRunningSystemApplication {
    public static void main(String[] args) {
        // 启动一个线程
        BotRunningServiceImpl.botPool.start();
        SpringApplication.run(BotRunningSystemApplication.class, args);
    }
}