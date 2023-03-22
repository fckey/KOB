package com.kob.matchingsystem.controller;

import com.kob.matchingsystem.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @description:
 * @author: fangshaolei
 * @time: 2023/3/22 21:04
 */
@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    /**
     * @description: 添加匹配的玩家
     * @param data
     * @return: java.lang.String
     * @author: fangshaolei
     * @time: 2023/3/22 21:10
     */
    @PostMapping("/add/")
    public String addPlayer(@RequestParam MultiValueMap<String, String> data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        Integer rating = Integer.parseInt(Objects.requireNonNull(data.getFirst("rating")));
        return matchingService.addPlayer(userId, rating);
    }

    /**
     * @description: 删除匹配系统的玩家
     * @param data
     * @return: java.lang.String
     * @author: fangshaolei
     * @time: 2023/3/22 21:11
     */
    @PostMapping("/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String, String> data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        return matchingService.removePlayer(userId);
    }
}
