package com.kob.matchingsystem.service.impl.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 所有匹配的操作都在这里做
 * @author: fangshaolei
 * @time: 2023/3/25 11:27
 */
@Component
public class MatchingPool extends Thread{

    private static final Logger log = LoggerFactory.getLogger(MatchingPool.class);
    // 记录当前所有正在匹配的用户
    private static List<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();
    private static RestTemplate restTemplate;
    private final static String startGameUrl = "http://127.0.0.1:9000/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        MatchingPool.restTemplate = restTemplate;
    }


    /**
     * @author Jeff Fong
     * @description 添加一个用户
     * @date 2023/5/11 10:01
     * @param: userId
     * @param: rating
     * @return void
     **/
    public void addPlayer(Integer userId, Integer rating, Integer botId){
        lock.lock();
        try{
            // 添加一个用户
            players.add(new Player(userId, rating, botId, 0));
        }finally{
            lock.unlock();
        }
    }

    /**
     * @author Jeff Fong
     * @description 删除一名玩家
     * @date 2023/5/11 10:02
     * @param: userId
     * @return void
     **/
    public void removePlayer(Integer userId){
        lock.lock();
        try{
            List<Player> newPlayers = new ArrayList<>();
            for (Player player : players) {
                if(!player.getUserId().equals(userId)){
                    newPlayers.add(player);
                }
            }
            players = newPlayers;
        }finally{
            lock.unlock();
        }
    }
    /**
     * @description: 所有玩家的匹配时间加上1
     * @param
     * @return: void
     * @author: fangshaolei
     * @time: 2023/3/25 11:52
     */
    private void increasingWaitingTime(){
        for(Player player: players){
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }
    /**
     * @description: 判断两个玩家是否是匹配的 , 策略是如果是分差小于等待时间乘10，就会进行匹配
     * @param a
     * @param b
     * @return: boolean
     * @author: fangshaolei
     * @time: 2023/3/25 12:00
     */
    private boolean checkMatched(Player a, Player b){
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        // 表示的是需要两个的等待时间都满足这个策略，才会进行匹配
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }
    
    /**
     * @description: 返回a和b的匹配结果,直接开始游戏
     * @param a
     * @param b
     * @return: void
     * @author: fangshaolei
     * @time: 2023/3/25 12:01
     */
    private void sendResult(Player a, Player b){
        log.info("send result : {} and {}", a, b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getUserId().toString());
        data.add("a_bot_id", a.getBotId().toString());
        data.add("b_id", b.getUserId().toString());
        data.add("b_bot_id", b.getBotId().toString());
        // 向后端系统发送请求
        restTemplate.postForObject(startGameUrl, data, String.class);
    }
    /**
     * @description: 尝试匹配所有玩家
     * @param
     * @return: void
     * @author: fangshaolei
     * @time: 2023/3/25 11:53
     */
    private void matchingPlayers(){
        log.info("match players : {}", players.toString());
        boolean[] used = new boolean[players.size()]; // 表示的是哪些玩家已经匹配过了
        // 表示的是最先加入到队列中的玩家会有更高的优先级来进行匹配
        for(int i = 0;i < players.size(); i ++){
            if(used[i]) continue;
            // 找出第二个人
            for(int j = i + 1; j < players.size(); j ++){
                if(used[j]) continue;
                Player a = players.get(i), b = players.get(j);
                // 判断两个是否可以进行匹配
                if(checkMatched(a,b)){
                    used[i] = used[j] = true; // 表示已经用过了
                    log.info("matching player -------- {} and {} success .......", a, b);
                    sendResult(a, b);
                    break;
                }
            }
        }
        // 将已经匹配的玩家从匹配池中除去
        List<Player> newPlayers = new ArrayList<>();
        for(int i = 0; i < players.size(); i ++){
            if(!used[i]){
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try{
                    // 所有玩家等待1s之后，时间就会自动的加上1
                    increasingWaitingTime();
                    matchingPlayers();
                }finally{
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
