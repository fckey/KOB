package com.kob.backend.consumer;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: websocket的连接处理
 * @author: fangshaolei
 * @time: 2023/3/20 9:23
 */
@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    private static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>(); // 存储所有的链接
    private Session session = null;
    private User user;

    // 注入数据库操作
    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 建立连接
        this.session = session;
        System.out.println("connected ! ");
        Integer userId = Integer.parseInt(token);
        this.user = userMapper.selectById(userId);
        users.put(userId, this); // 所有对象放到map中
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnnected");
        if(this.user != null){
            users.remove(this.user.getId());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message : " + message);
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
                this.session.getBasicRemote().sendText("test");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}