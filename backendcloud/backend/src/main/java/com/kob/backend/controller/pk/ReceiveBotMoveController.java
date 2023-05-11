package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname ReceiveBotMoveControlle
 * @description: None
 * @author: Jeff Fong
 * @create: 2023/05/11 20:06
 **/
@RestController
public class ReceiveBotMoveController {
    @Autowired
    private ReceiveBotMoveService receiveBotMoveService;
    
    /**
     * @author Jeff Fong
     * @description 接受从botrunningsystem来的下一步操作返回到前端去渲染
     * @date 2023/5/11 20:10
     * @param: data
     * @return java.lang.String
     **/
    @PostMapping("/pk/receive/bot/move/")
    public String receiveBotMove(@RequestParam MultiValueMap<String, String> data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        Integer direction = Integer.parseInt(Objects.requireNonNull(data.getFirst("direction")));
        return receiveBotMoveService.receiveBotMove(userId, direction);
    }
}