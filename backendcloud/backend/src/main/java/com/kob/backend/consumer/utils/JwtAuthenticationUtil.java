package com.kob.backend.consumer.utils;

import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;

/**
 * @description: 和用户认证有关的jwt的工具类
 * @author: fangshaolei
 * @time: 2023/3/20 9:58
 */
public class JwtAuthenticationUtil {

    /**
     * @author Jeff Fong
     * @description 对用户的id进行提取
     * @date 2023/5/10 16:56
     * @param: token 唯一标识
     * @return java.lang.Integer
     **/
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
