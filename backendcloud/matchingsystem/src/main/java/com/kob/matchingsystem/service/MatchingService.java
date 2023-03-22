package com.kob.matchingsystem.service;

/**
 * @description:
 * @author: fangshaolei
 * @time: 2023/3/22 21:00
 */
public interface MatchingService {
    /**
     * @description: 添加玩家
     * @param userId
     * @param rating
     * @return: java.lang.String
     * @author: fangshaolei
     * @time: 2023/3/22 21:11
     */
    String addPlayer(Integer userId, Integer rating);
    
    /**
     * @description: 移除玩家
     * @param userId
     * @return: java.lang.String
     * @author: fangshaolei
     * @time: 2023/3/22 21:11
     */
    String removePlayer(Integer userId);
}
