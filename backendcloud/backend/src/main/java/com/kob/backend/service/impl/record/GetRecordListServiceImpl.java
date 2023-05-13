package com.kob.backend.service.impl.record;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetRecordListService;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname GetRecordListServiceImpl
 * @description: None
 * @author: Jeff Fong
 * @create: 2023/05/13 11:34
 **/
@Service
public class GetRecordListServiceImpl implements GetRecordListService {
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private UserMapper userMapper;
    
    /**
     * @author Jeff Fong
     * @description 获取所有的数据
     * @date 2023/5/13 11:34
     * @param: page
     * @return org.json.JSONObject
     **/
    @Override
    public JSONObject getList(Integer page) {
        // 默认是每页显示10条数据
        IPage<Record> recordIPage = new Page<>(page, 10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Record> records = recordMapper.selectPage(recordIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for(Record record: records){
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            if(null == userA || null == userB) continue;
            JSONObject item = new JSONObject();
            item.put("a_avatar", userA.getAvatar());
            item.put("a_username", userA.getUserName());
            item.put("b_avatar", userB.getAvatar());
            item.put("b_username", userB.getUserName());
            // 存储一下对战的结果
            String result = "平局";
            if("a".equals(record.getLoser())) result = "B胜";
            else if("b".equals(record.getLoser())) result = "A胜";
            item.put("result", result);

            item.put("record", record);
            items.add(item);
        }
        resp.put("records", items);
        // 返回总的条数
        resp.put("records_count", recordMapper.selectCount(null));


        return resp;
    }
}