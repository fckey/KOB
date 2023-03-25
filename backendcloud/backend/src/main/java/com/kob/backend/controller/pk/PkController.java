package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.PkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @description:
 * @author: fangshaolei
 * @time: 2023/3/25 14:08
 */
@RestController
public class PkController {

    @Autowired
    private PkService pkService;
    
    /**
     * @description: 给匹配模块进行调用的
     * @param data
     * @return: java.lang.String
     * @author: fangshaolei
     * @time: 2023/3/25 14:10
     */
    @PostMapping("/pk/start/game/")
    public String startGame(@RequestParam MultiValueMap<String, String> data){
        Integer aId = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_id")));
        Integer bId = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_id")));
        return pkService.startGame(aId, bId);
    }
}
