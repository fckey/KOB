package com.kob.backend.service.pk;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname ReceiveBotMove
 * @description: None
 * @author: Jeff Fong
 * @create: 2023/05/11 19:59
 **/
public interface ReceiveBotMoveService  {
    /**
     * @author Jeff Fong
     * @description 从botrunningsystem传过来的命令
     * @date 2023/5/11 20:00
     * @param: userId
     * @param: direction
     * @return java.lang.String
     **/
    String receiveBotMove(Integer userId, Integer direction);
}