package com.kob.backend.service.user;

import java.util.Map;

/**
 * @description: 对账户有关的操作
 * @author: fangshaolei
 * @time: 2023/3/16 20:31
 */
public interface AccountService {

    /**
     * @description: 登录
     * @param username 用户名
     * @param password 密码
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/16 20:40
     */
    Map<String, String> getToken(String username, String password);
    
    /**
     * @description: 获取用户信息
     * @param
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/16 20:40
     */
    Map<String, String> getInfo();
    
    /**
     * @description: 
     * @param username 用户名
     * @param password 密码
     * @param confirmedPassword 确认的密码
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/16 20:39
     */
    Map<String, String> register(String username, String password, String confirmedPassword);
}
