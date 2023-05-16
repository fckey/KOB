package com.kob.backend.controller.user;

import com.kob.backend.service.user.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description: 有关于用户的操作
 * @author: fangshaolei
 * @time: 2023/3/17 9:19
 */
@RestController
@RequestMapping("/api/user/account")
@RequiredArgsConstructor
public class AccountController {
    private static final Logger LOG = LogManager.getLogger(AccountController.class);
    private final AccountService accountService;

    /**
     * @description:  获取token
     * @param map
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/17 9:44
     */
    @PostMapping("/token/")
    public Map<String, String> getToken(@RequestParam Map<String, String> map){
        String username = map.get("username");
        String password = map.get("password");
        LOG.info("username = {}, password = {}", username, password);
        return accountService.getToken(username, password);
    }
    
    /**
     * @description: 获取已经登录用户的信息
     * @param
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/17 10:39
     */
    @GetMapping("/info/")
    public Map<String, String> getInfo(){
        return accountService.getInfo();
    }
    
    /**
     * @description: 注册
     * @param map
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/17 15:09
     */
    @PostMapping("/register/")
    public Map<String, String> register(@RequestParam Map<String, String> map){
        String username = map.get("username");
        String password = map.get("password");
        String confirmedPassword = map.get("confirmedPassword");
        return accountService.register(username, password, confirmedPassword);
    }
}
