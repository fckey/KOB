package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname ReceiveBotMoveServiceImpl
 * @description: None
 * @author: Jeff Fong
 * @create: 2023/05/11 20:02
 **/
@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {

    private static final Logger log = LoggerFactory.getLogger(ReceiveBotMoveServiceImpl.class);

    /**
     * @return java.lang.String
     * @author Jeff Fong
     * @description 接受从botrunningsystem收到的下一步操作
     * @date 2023/5/11 20:02
     * @param: userId
     * @param: direction
     **/
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        log.info("receive bot {} move {}  !", userId, direction);

        if (WebSocketServer.userPool.get(userId) != null) {
            Game game = WebSocketServer.userPool.get(userId).game;
            if (game != null) {
                if (game.getPlayerA().getId().equals(userId)) {
                    game.setNextStepA(direction);
                } else if (game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                }
            }
        }

        return "receive bot move success";
    }
}