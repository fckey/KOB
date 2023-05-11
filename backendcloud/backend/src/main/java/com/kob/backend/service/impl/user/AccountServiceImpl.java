package com.kob.backend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.AccountService;
import com.kob.backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: fangshaolei
 * @time: 2023/3/16 20:42
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    /**
     * @description: 
    ` * @param username
     * @param password
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/16 20:47
     */
    @Override
    public Map<String, String> getToken(String username, String password) {
        UsernamePasswordAuthenticationToken  authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authenticate = authenticationManager.authenticate(authenticationToken); // 登录失败会自动处理
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();
        // 创建jwt
        String jwt = JwtUtil.createJWT(user.getId().toString    ());

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("token", jwt);

        return map;
    }
    
    /**
     * @description: 
     * @param 
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/16 20:47
     */
    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder
                        .getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUserName());
        map.put("avatar", user.getAvatar());

        return map;
    }
    
    /**
     * @description: 注册用户
     * @param username
     * @param password
     * @param confirmedPassword
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/16 20:47
     */
    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        if(username == null){
            map.put("error_message", "用户名不能为空");
            return map;
        }
        if(password == null || confirmedPassword == null){
            map.put("error_message", "密码不能为空");
            return map;
        }

        username = username.trim();
        if(username.length() == 0){
            map.put("error_message", "用户名不能为空");
            return map;
        }

        if(password.length() == 0 || confirmedPassword.length() == 0){
            map.put("error_message", "密码长度不能为空");
            return map;
        }

        if(username.length() > 100 ){
            map.put("error_message", "用户名长度不能大于100");
            return map;
        }

        if(password.length() > 100 || confirmedPassword.length() > 100){
            map.put("error_message", "密码长度不能大于100");
            return map;
        }

        if(!password.equals(confirmedPassword)){
            map.put("error_message", "两次输入的密码不一直");
            return map;
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        List<User> users = userMapper.selectList(queryWrapper);
        if(!CollectionUtils.isEmpty(users)){
            map.put("error_message", "用户名已存在");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        String avatar = "https://img1.baidu.com/it/u=1629860200,3877744089&fm=253&fmt=auto&app=120&f=GIF?w=556&h=471";
        User user = new User(null,1500, username, encodedPassword, avatar);
        userMapper.insert(user);

        map.put("error_message", "success");
        return map;
    }
}
