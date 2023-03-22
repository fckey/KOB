package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: fangshaolei
 * @time: 2023/3/22 21:01
 */
@Service
public class MatchingServiceImpl implements MatchingService {
    /**
     * @description: 添加玩家
     * @param userId
     * @param rating
     * @return: java.lang.String
     * @author: fangshaolei
     * @time: 2023/3/22 21:12
     */
    @Override
    public String addPlayer(Integer userId, Integer rating) {
        System.out.println("add player: " + userId + " " + rating );
        return "add player success";
    }
    
    /**
     * @description: 移除玩家
     * @param userId
     * @return: java.lang.String
     * @author: fangshaolei
     * @time: 2023/3/22 21:12
     */
    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player: " +userId);
        return "remove player success";
    }
}
