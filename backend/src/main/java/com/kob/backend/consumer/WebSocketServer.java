package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthenticationUtil;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @description: websocket的连接处理
 * @author: fangshaolei
 * @time: 2023/3/20 9:23
 */
@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    final public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>(); // 存储所有的链接

    final private  static CopyOnWriteArrayList<User> matchpool = new CopyOnWriteArrayList<>(); // 匹配池
    private Session session = null;
    private User user;

    // 注入数据库操作
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;

    private Game game = null;

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper = recordMapper;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        System.out.println("connected ! ");
        Integer userId = JwtAuthenticationUtil.getUserId(token);
        this.user = userMapper.selectById(userId);
        if(this.user != null){
            users.put(userId, this); // 所有对象放到map中
        }else{
            this.session.close();
        }

        System.out.println(users);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnnected");
        if(this.user != null){
            users.remove(this.user.getId());
            matchpool.remove(this.user);
        }
    }

    private void move(int direction){
        // 判断一下当前是哪条蛇
        if(game.getPlayerA().getId().equals(user.getId())){
            game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(user.getId())){
            game.setNextStepB(direction);
        }
    }
    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message : " + message);
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            startMatching();
        }else if("stop-matching".equals(event)){
            stopMatching();
        }else if("move".equals(event)){
            System.out.println(event);
            move(data.getInteger("direction"));
        }
    }

    private void startMatching(){
        System.out.println("start matching");
        matchpool.add(this.user);

        while(matchpool.size() >= 2){
            Iterator<User> it = matchpool.iterator();
            User a = it.next();
            User b = it.next();
            matchpool.remove(a);
            matchpool.remove(b);

            Game game = new Game(14, 13, 16, a.getId(), b.getId());
            game.createMap();
            users.get(a.getId()).game = game;
            users.get(b.getId()).game = game;

            game.start(); // 开启一个新的线程

            JSONObject respA = new JSONObject();
            respA.put("event", "start-matching");
            respA.put("opponent_username", b.getUserName());
            respA.put("opponent_avatar", b.getAvatar());
            respA.put("game", game);
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event", "start-matching");
            respB.put("opponent_username", a.getUserName());
            respB.put("opponent_avatar", a.getAvatar());
            respB.put("game", game);
            users.get(b.getId()).sendMessage(respB.toJSONString());

        }
    }
    private void stopMatching(){
        System.out.println("stop matching");
        matchpool.remove(this.user);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    
    /**
     * @description: 发送消息
     * @param message
     * @return: void
     * @author: fangshaolei
     * @time: 2023/3/20 9:41
     */
    public void sendMessage(String message){
        synchronized (this.session){
            try{
                this.session.getBasicRemote().sendText(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}