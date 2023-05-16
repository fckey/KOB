package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.PkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname PkController
 * @description: 进行pK的controller
 * @author: Jeff Fong
 * @create: 2023/05/11 10:22
 **/
@RestController
public class PkController {
    private static final Logger log = LoggerFactory.getLogger(PkController.class);
    @Autowired
    private PkService pkService;

    /**
     * @author Jeff Fong
     * @description 调用开始的接口，这是给匹配系统调用的接口
     * @date 2023/5/11 10:25
     * @param: data
     * @return java.lang.String
     **/
    @PostMapping("/pk/start/game")
    public String startGame(@RequestParam MultiValueMap<String, String> data){
        Integer aId = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_id")));
        Integer aBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_bot_id")));
        Integer bId = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_id")));
        Integer bBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_bot_id")));

        log.info("匹配系统回传匹配成功数据，匹配成功的用户id是{} 和 {}", aId, bId);
        return pkService.startGame(aId, aBotId, bId, bBotId);
    }
}