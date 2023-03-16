package com.kob.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @description: 实现用户密码的加密存储
 * @author: fangshaolei
 * @time: 2023/3/16 15:26
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * @description: BCryptPasswordEncoder 修改加密模式
     * @param
     * @return: org.springframework.security.crypto.password.PasswordEncoder
     * @author: fangshaolei
     * @time: 2023/3/16 15:27
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}