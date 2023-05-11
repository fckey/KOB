package com.kob.backend.service.pk;

/**
 * @description: 是匹配玩家的service
 * @author: fangshaolei
 * @time: 2023/3/25 14:04
 */
public interface PkService {
    /**
     * @description: 接受匹配成功的玩家
     * @param a
     * @param b
     * @return: java.lang.String
     * @author: fangshaolei
     * @time: 2023/3/25 14:04
     */
    String startGame(Integer a,Integer aBotId, Integer b, Integer bBotId);
}
