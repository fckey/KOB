package com.kob.backend.service.bot;

import com.kob.backend.pojo.Bot;

import java.util.List;
import java.util.Map;

/**
 * @description: 对bot有关的操作
 * @author: fangshaolei
 * @time: 2023/3/19 9:42
 */
public interface BotService {
    
    /**
     * @description: 
     * @param data
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/19 9:48
     */
    Map<String, String> add(Map<String, String> data);
    /**
     * @description: 
     * @param data
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/19 9:48
     */
    Map<String, String> remove(Map<String, String> data);
    /**
     * @description: 
     * @param data
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/19 9:48
     */
    Map<String, String> update(Map<String, String> data);
    /**
     * @description: 
     * @param 
     * @return: java.util.List<com.kob.backend.pojo.Bot>
     * @author: fangshaolei
     * @time: 2023/3/19 9:48
     */
    List<Bot> getList();
}
