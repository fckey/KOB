package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: bot的有关控制
 * @author: fangshaolei
 * @time: 2023/3/15 20:00
 */
@RestController
@RequestMapping("/pk")
public class BotInfoController {

    /*
     * @description: 返回当前bot的所有信息
     * @param
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/15 20:02
     */
    @RequestMapping("getbotinfo")
    public Map<String, String> getBotInfo(){
        Map<String, String> bot1 = new HashMap<>();
        bot1.put("name", "fangshaolei");
        bot1.put("rating", "15000");
        return bot1;
    }
}
