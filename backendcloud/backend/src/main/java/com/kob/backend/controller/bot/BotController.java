package com.kob.backend.controller.bot;

import com.kob.backend.pojo.Bot;
import com.kob.backend.service.bot.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: fangshaolei
 * @time: 2023/3/19 10:11
 */
@RestController
@RequestMapping("/api/user/bot")
@RequiredArgsConstructor
public class BotController {
    private final BotService botService;
    
    /**
     * @description: 插入bot信息
     * @param data 插入的数据
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/19 10:26
     */
    @PostMapping("/add/")
    public Map<String, String> add(@RequestParam Map<String, String> data){
        return botService.add(data);
    }   
    
    /**
     * @description:  删除指定自己的bot
     * @param data
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/19 11:37
     */
    @PostMapping("/remove/")
    public Map<String, String> remove(@RequestParam Map<String, String> data){
        return botService.remove(data);
    }
    
    /**
     * @description: 更新bot信息
     * @param data
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/19 17:41
     */
    @PostMapping("/update/")
    public Map<String, String> update(@RequestParam Map<String, String> data){
        return botService.update(data);
    }

    /**
     * @description: 获取当前对象所有的bot
     * @param
     * @return: java.util.List<com.kob.backend.pojo.Bot>
     * @author: fangshaolei
     * @time: 2023/3/19 17:55
     */
    @GetMapping("/getlist/")
    public List<Bot> getList(){
        return botService.getList();
    }
}
