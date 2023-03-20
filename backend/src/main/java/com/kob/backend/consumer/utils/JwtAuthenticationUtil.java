package com.kob.backend.consumer.utils;

import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;

/**
 * @description: 对jwt中的id进行提取
 * @author: fangshaolei
 * @time: 2023/3/20 9:58
 */
public class JwtAuthenticationUtil {

    /**
     * @description: 从jwt中提取出id
     * @param token
     * @return: java.lang.Integer
     * @author: fangshaolei
     * @time: 2023/3/20 10:00
     */
    public static Integer getUserId(String token){
        Integer userId = -1;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Integer.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
}
