package com.kob.backend.service.impl.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.ranklist.GetRanklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname GetRanklistServiceImpl
 * @description: 排行榜页面数据
 * @author: Jeff Fong
 * @create: 2023/05/13 20:55
 **/
@Service
public class GetRanklistServiceImpl implements GetRanklistService {

    @Autowired
    private UserMapper userMapper;
    
    /**
     * @author Jeff Fong
     * @description 分页查询出页面中的数据
     * @date 2023/5/13 21:01
     * @param: page
     * @return com.alibaba.fastjson2.JSONObject
     **/
    @Override
    public JSONObject getList(Integer page) {
        IPage<User> userIPage = new Page<>(page, 7);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        List<User> users = userMapper.selectPage(userIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        resp.put("users", users);
        // 设置密码为空操作
        for (User user : users) {
            user.setPassWord("");
        }
        // 查询所有总数
        resp.put("users_count", userMapper.selectCount(null));
        return resp;
    }
}