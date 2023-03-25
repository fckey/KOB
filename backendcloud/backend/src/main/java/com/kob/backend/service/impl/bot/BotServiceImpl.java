package com.kob.backend.service.impl.bot;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.bot.BotService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: fangshaolei
 * @time: 2023/3/19 9:48
 */
@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {
    private final BotMapper botMapper;
    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();

        if(title == null || title.length() == 0){
            map.put("error_message", "标题不能为空");
            return map;
        }

        if(title.length() > 100){
            map.put("error_message", "标题长度不能超过100");
            return map;
        }
        if(description == null || description.length() == 0){
            description = "这个用户很懒，什么也没有写~";
        }
        if( description.length() > 300){
            map.put("error_message", "bot的描述长度不能大于300");
        }
        if(content == null || content.length() == 0 ){
            map.put("error_message", "代码不能为空");
            return map;
        }

        if(content.length() > 10000){
            map.put("error_message", "代码长度不能超过10000");
            return map;
        }

        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, content, now, now);
        int insert = botMapper.insert(bot);
        if(insert < 1) map.put("error_message", "插入数据失败");
        else map.put("error_message", "success");

        return map;
    }
    
    /**
     * @description: 通过指定信息删除对应的bot
     * @param data
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: fangshaolei
     * @time: 2023/3/19 11:31
     */
    @Override
    public Map<String, String> remove(Map<String, String> data) {
        Integer bot_id = Integer.parseInt(data.get("bot_id"));
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Bot bot = botMapper.selectById(bot_id);
        Map<String, String> map = new HashMap<>();

        if(bot == null) {
            map.put("error_message", "bot不存在");
            return map;
        }

        if(!bot.getUserId().equals(user.getId())){
            map.put("error_message", "没有权限删除bot");
            return map;
        }
        botMapper.deleteById(bot_id);
        map.put("error_message", "success");
        return map;
    }

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Integer bot_id = Integer.parseInt(data.get("bot_id"));
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();

        if(title == null || title.length() == 0){
            map.put("error_message", "标题不能为空");
            return map;
        }

        if(title.length() > 100){
            map.put("error_message", "标题长度不能超过100");
            return map;
        }
        if(description == null || description.length() == 0){
            description = "这个用户很懒，什么也没有写~";
        }
        if( description.length() > 300){
            map.put("error_message", "bot的描述长度不能大于300");
        }
        if(content == null || content.length() == 0 ){
            map.put("error_message", "代码不能为空");
            return map;
        }

        if(content.length() > 10000){
            map.put("error_message", "代码长度不能超过10000");
            return map;
        }

        Bot bot = botMapper.selectById(bot_id); // 从数据库取出值

        if(bot == null) {
            map.put("error_message", "bot不存在");
            return map;
        }

        if(!bot.getUserId().equals(user.getId())){
            map.put("error_message", "没有权限修改该bot");
            return map;
        }
        Bot new_bot = new Bot(
                bot.getId(),
                user.getId(),
                title,
                description,
                content,
                bot.getCreateTime(),
                new Date()
        );
        botMapper.updateById(new_bot);

        map.put("error_message", "success");

        return map;
    }

    @Override
    public List<Bot> getList() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        LambdaQueryWrapper<Bot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bot::getUserId, user.getId());
        List<Bot> bots = botMapper.selectList(queryWrapper);
        return bots;
    }
}
