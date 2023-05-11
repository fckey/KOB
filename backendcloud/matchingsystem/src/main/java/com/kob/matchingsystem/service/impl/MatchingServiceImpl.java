package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: fangshaolei
 * @time: 2023/3/22 21:01
 */
@Service
public class MatchingServiceImpl implements MatchingService {
    private static final Logger log = LoggerFactory.getLogger(MatchingServiceImpl.class);
    public final static MatchingPool matchingPool = new MatchingPool(); // 定义一个全局的线程类

    /**
     * @author Jeff Fong
     * @description 添加玩家
     * @date 2023/5/11 16:13
     * @param: userId
     * @param: rating
     * @param: botId
     * @return java.lang.String
     **/
    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        log.info("add player userid = {}, and rating = {}", userId, rating);
        matchingPool.addPlayer(userId, rating, botId);
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
        log.info("remove player : userId = {}", userId);
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}
