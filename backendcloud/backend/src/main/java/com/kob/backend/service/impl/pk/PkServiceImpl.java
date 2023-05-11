package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.PkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: fangshaolei
 * @time: 2023/3/25 14:06
 */
@Service
public class PkServiceImpl implements PkService {
    private static final Logger log = LoggerFactory.getLogger(PkServiceImpl.class);

    /**
     * @author Jeff Fong
     * @description 通过两个用户的id来进行开启游戏
     * @date 2023/5/11 16:18
     * @param: aId
     * @param: aBotId a的ai对战
     * @param: bId
     * @param: bBotId b的ai对战id
     * @return java.lang.String
     **/
    @Override
    public String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        System.out.println("start game: " + aId + " - " + bId);
        // 直接调用开始游戏
        WebSocketServer.startGame(aId, aBotId, bId, bBotId);
        return "start game success";
    }
}
