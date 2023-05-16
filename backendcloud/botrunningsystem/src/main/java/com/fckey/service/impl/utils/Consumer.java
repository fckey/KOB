package com.fckey.service.impl.utils;

import org.joor.Reflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname Consumer
 * @description: 对穿过来的每一个代码都开一个线程单独的进行执行
 * @author: Jeff Fong
 * @create: 2023/05/11 19:11
 **/
@Component
public class Consumer extends Thread{
    
    private static final Logger log = LoggerFactory.getLogger(Consumer.class);
    private Bot bot;
    private static RestTemplate restTemplate;
    private static final String receiveBotMoveUrl = "http://127.0.0.1:9000/pk/receive/bot/move/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {Consumer.restTemplate = restTemplate;}
    /**
     * @author Jeff Fong
     * @description 在规定时间内一定要执行完当前的bot代码
     * @date 2023/5/11 19:16
     * @param: timeout
     * @param: bot
     * @return void
     **/
    public void startTimeout(long timeout, Bot bot){
        this.bot = bot;
        this.start(); // 启动当前线程

        // 1. 等待timeout 或者 被别人打断
        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            this.interrupt(); // 中断当前线程
        }
    }
    
    /**
     * @author Jeff Fong
     * @description code中的类名后面添加uid
     * @date 2023/5/11 19:23
     * @param: code
     * @param: uid
     * @return java.lang.String
     **/
    private String addUid(String code, String uid){
        int k = code.indexOf(" implements java.util.function.Supplier<Integer>");
        return code.substring(0, k) + uid + code.substring(k);

    }
    @Override
    public void run() {
        String uid = UUID.randomUUID().toString().substring(0, 8);
        // 如果是重名的类是只会编译一次
        Supplier<Integer> botInterface = Reflect.compile(
                "com.fckey.utils.Bot" + uid,
                addUid(bot.getBotCode(), uid)
        ).create().get();

        // 将内容转变成文件
        File file = new File("input.txt");
        try (PrintWriter fout = new PrintWriter(file)){
            fout.println(bot.getBotInput());
            fout.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Integer direction = botInterface.get();
        log.info("当前bot{} , 的下一步操作是: {}",bot.getUserId(), direction);

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction", direction.toString());

        restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
    }
}