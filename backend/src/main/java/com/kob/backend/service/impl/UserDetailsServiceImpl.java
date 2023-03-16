package com.kob.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @description: 用来接入数据库信息,springsecurity
 * @author: fangshaolei
 * @time: 2023/3/16 15:25
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    /**
     * @description: 对数据库的信息进行查询操作
     * @param username 传入的用户名
     * @return: org.springframework.security.core.userdetails.UserDetails
     * @author: fangshaolei
     * @time: 2023/3/16 15:27
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        if(ObjectUtils.isEmpty(user)){
            throw new RuntimeException("用户不存在");
        }
        return new UserDetailsImpl(user);
    }
}
