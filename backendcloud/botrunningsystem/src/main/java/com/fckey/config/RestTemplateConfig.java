package com.fckey.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname RestTemplateConfig
 * @description: 注入RestTemplate
 * @author: Jeff Fong
 * @create: 2023/05/11 14:55
 **/
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}