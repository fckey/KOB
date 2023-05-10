package com.kob.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @description: WebSocket配置类
 * @author: fangshaolei
 * @time: 2023/3/20 9:30
 */
@Configuration
public class WebSocketConfig {
    /**
     * @author Jeff Fong
     * @description 如果是在SpringBoot的内置容器中（嵌入式容器）运行，需要提供ServerEndpointExporter
     *              但是在Tomcat容器中运行，扫描工作交给容器处理，不需要Bean注入
     * @date 2023/5/10 16:58
     * @param: null
     * @return org.springframework.web.socket.server.standard.ServerEndpointExporter
     **/
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {

        return new ServerEndpointExporter();
    }
}