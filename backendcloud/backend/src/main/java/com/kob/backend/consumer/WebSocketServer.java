package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthenticationUtil;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname WebSocketServer
 * @description: WebSocket的连接处理，每次出现一个新的连接，系统都会创建当前这个类的实例
 * @author: Jeff Fong
 * @create: 2023/05/10 17:18
 **/
@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    // 存储目前还在系统中的用户对应的WebSocket链接，需要使用静态的并发的Map来存储，防止并发问题，静态是每次来新的连接都会创建实例
    public static final ConcurrentHashMap<Integer, WebSocketServer> userPool = new ConcurrentHashMap<>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session = null;
    // 当前和服务端通信的用户
    private User user = null;

    // 注入数据库操作
    public static UserMapper userMapper;
    public static RecordMapper recordMapper;
    public static BotMapper botMapper;

    // 用作服务调用的类
    public static RestTemplate restTemplate;

    // 得到创建的地图
    public Game game = null;
    private final static String addPlayerUrl = "http://127.0.0.1:9001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:9001/player/remove/";
    @Autowired
    public void setUserMapper(UserMapper userMapper){WebSocketServer.userMapper = userMapper;}
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper = recordMapper;
    }
    @Autowired
    public void setBotMapper(BotMapper botMapper) {WebSocketServer.botMapper = botMapper;}
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate = restTemplate;
    }
    
    /**
     * @author Jeff Fong
     * @description WebSocket连接成功建立后回调函数，作初始化方法和发送数据
     * @date 2023/5/10 17:00
     * @param: session 上下文
     * @param: token WebSocket建立发送的参数
     * @return void
     **/
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 1. 客户端建立连接
        this.session = session;
        // 2. 获取用户的信息
        Integer userId = JwtAuthenticationUtil.getUserId(token);
        this.user = userMapper.selectById(userId);
        log.info("{} connected server ..... ", user.toString());
        // 将
        if(this.user != null){
            userPool.put(userId, this); // 所有对象放到map中
        }else{
            this.session.close();
        }
        log.info("the current userPool is {}", userPool);
    }

    /**
     * @author Jeff Fong
     * @description WebSocket断开连接后的回调函数
     * @date 2023/5/10 17:03
     * @param:
     * @return void
     **/
    @OnClose
    public void onClose() {
        // 关闭链接
        log.info("{} disconnected ..... ", user.toString());
        if(this.user != null){
            userPool.remove(this.user.getId());
        }
    }

    /**
     * @author Jeff Fong
     * @description 根据方向来设置移动的方向
     * @date 2023/5/10 21:31
     * @param: direction
     * @return void
     **/
    private void move(int direction){
        // 判断当前是哪条蛇
        if(game.getPlayerA().getId().equals(user.getId())){
            if(game.getPlayerA().getBotId() != null &&
                    "-1".equals(game.getPlayerA().getBotId().toString())){ // 自己人工操作走的流程
                // 设置a的方向值
                game.setNextStepA(direction);
            }
        }else if(game.getPlayerB().getId().equals(user.getId())){
            if(game.getPlayerB().getBotId() != null &&
                    "-1".equals(game.getPlayerB().getBotId().toString())){ // 自己人工操作走的流程
                // 设置b的方向值
                game.setNextStepB(direction);
            }
        }
    }

    /**
     * @author Jeff Fong
     * @description 从客户端接受到消息的回调函数，写处理消息的逻辑
     * @date 2023/5/10 17:04
     * @param: message
     * @param: session
     * @return void
     **/
    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        log.info("{} receive message : {}", user.toString(), message);
        // 将前端的信息解析出来
        JSONObject data = JSONObject.parseObject(message);
        // 判断匹配的状态
        String event = data.getString("event");
        if("start-matching".equals(event)){
            // 将前端传过来的bot_id放到后面去
            startMatching(data.getInteger("bot_id"));
        }else if("stop-matching".equals(event)){
            stopMatching();
        }else if("move".equals(event)){
            log.info("当前前端发送过来的是{}", event);
            // 移动
            move(data.getInteger("direction"));
        }
    }

    /**
     * @author Jeff Fong
     * @description 开始游戏，进行地图的创建和响应客户端
     * @date 2023/5/11 09:30
     * @param: aId
     * @param: bId
     * @return void
     **/
    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId){
        User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);

        Game game = new Game(
                13,
                14,
                20,
                a.getId(),
                botA,
                b.getId(),
                botB);
        game.createMap();
        if(userPool.get(a.getId()) != null){
            userPool.get(a.getId()).game = game;
        }
        if(userPool.get(b.getId()) != null){
            userPool.get(b.getId()).game = game;
        }
        // 对游戏开启一个新的线程来处理地图的同步问题
        game.start();
        // 将a,b的坐标和地图发送给前端，进行同步
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("rows", game.getRows());
        respGame.put("cols", game.getCols());
        respGame.put("innerwallcount", game.getInnerWallsCount());
        respGame.put("map", game.getMap());

        JSONObject respA = new JSONObject();
        respA.put("event", "start-matching");
        respA.put("opponent_username", b.getUserName());
        respA.put("opponent_avatar", b.getAvatar());
        respA.put("game", respGame);
        if(userPool.get(a.getId()) != null){
            userPool.get(a.getId()).sendMessage(respA.toJSONString());
        }
        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUserName());
        respB.put("opponent_avatar", a.getAvatar());
        respB.put("game", respGame);
        if(userPool.get(b.getId()) != null){
            userPool.get(b.getId()).sendMessage(respB.toJSONString());
        }
    }

    /**
     * @author Jeff Fong
     * @description 开始匹配游戏，需要通过匹配系统来实现
     * @date 2023/5/11 15:49
     * @param: botId 前端穿过来要对战的botid
     * @return void
     **/
    private void startMatching(Integer botId){
        log.info("start matching ..... ");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        // 需要用户的id
        data.add("user_id", this.user.getId().toString());
        // 当前用户的积分是多少
        data.add("rating", this.user.getRating().toString());
        data.add("bot_id", botId.toString());
        // 开始匹配之后，将需要匹配的参数信息发送给匹配系统
        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    /**
     * @author Jeff Fong
     * @description 停止匹配游戏，需要将停止匹配的消息发送给匹配系统
     * @date 2023/5/11 09:32
     * @param:
     * @return void
     **/
    private void stopMatching(){
        log.info("stop matching ..... ");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        // 将停止匹配的消息发送给匹配系统
        restTemplate.postForObject(removePlayerUrl, data, String.class);
    }

    /**
     * @author Jeff Fong
     * @description WebSocket连接过程中发生错误的回调函数
     * @date 2023/5/10 17:05
     * @param: session
     * @param: error
     * @return void
     **/
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    
    /**
     * @description: 后端向前端发送信息
     * @param message
     * @return: void
     * @author: fangshaolei
     * @time: 2023/3/20 9:41
     */
    public void sendMessage(String message){
        synchronized (this.session){
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}