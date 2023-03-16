package com.kob.backend.controller.user;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 对用户的操作
 * @author: fangshaolei
 * @time: 2023/3/16 14:33
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserMapper userMapper;
    
    /**
     * @description: 查询所有用户
     * @param
     * @return: java.util.List<com.kob.backend.pojo.User>
     * @author: fangshaolei
     * @time: 2023/3/16 14:58
     */
    @GetMapping("/all")
    public List<User> getAll(){
        List<User> users = userMapper.selectList(null);
        return users;
    }
    
    /**
     * @description: 根据用户id查询用户
     * @param userId 用户id
     * @return: com.kob.backend.pojo.User
     * @author: fangshaolei
     * @time: 2023/3/16 15:01
     */
    @GetMapping("/{userId}")
    public User getUser(@PathVariable Integer userId){
        return userMapper.selectById(userId);
    }

    /**
     * @description: 插入用户操作
     * @param userName 用户名成
     * @param passWord 用户密码
     * @return: java.lang.String
     * @author: fangshaolei
     * @time: 2023/3/16 15:11
     */
    @GetMapping("/add/{userName}/{passWord}")
    public String addUser(
            @PathVariable String userName,
            @PathVariable String passWord
    ){
        User user = new User();
        user.setUserName(userName);
        // 加密密码
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePassword = encoder.encode(passWord);
        user.setPassWord(encodePassword);
        int insert = userMapper.insert(user);
        if(insert > 0) return "success";
        return "fail";
    }
    
    /**
     * @description: 
     * @param userId 用户的id
     * @return: java.lang.String
     * @author: fangshaolei
     * @time: 2023/3/16 15:15
     */
    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable Integer userId){
        userMapper.deleteById(userId);
        return "Delete Successfully!";
    }
}
