package com.kob.matchingsystem.service;

/**
 * @description:
 * @author: fangshaolei
 * @time: 2023/3/22 21:00
 */
public interface MatchingService {

    /**
     * @author Jeff Fong
     * @description 添加玩家
     * @date 2023/5/11 16:13
     * @param: userId 用户的id
     * @param: rating 用户的积分是多少
     * @param: botId 当前的botid
     * @return java.lang.String
     **/
    String addPlayer(Integer userId, Integer rating, Integer botId);
    
    /**
     * @description: 移除玩家
     * @param userId
     * @return: java.lang.String
     * @author: fangshaolei
     * @time: 2023/3/22 21:11
     */
    String removePlayer(Integer userId);
}
