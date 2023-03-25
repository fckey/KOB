package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.PkService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: fangshaolei
 * @time: 2023/3/25 14:06
 */
@Service
public class PkServiceImpl implements PkService {
    /**
     * @description: 通过两个用户的id来进行开启游戏
     * @param aId
     * @param bId
     * @return: java.lang.String
     * @author: fangshaolei
     * @time: 2023/3/25 14:08
     */
    @Override
    public String startGame(Integer aId, Integer bId) {
        System.out.println("start game: " + aId + " - " + bId);
        WebSocketServer.startGame(aId, bId);
        return "start game success";
    }
}
