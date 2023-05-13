package com.kob.backend.service.record;

import com.alibaba.fastjson2.JSONObject;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname GetRecordList
 * @description: 返回一个对局列表
 * @author: Jeff Fong
 * @create: 2023/05/13 11:32
 **/
public interface GetRecordListService {
    /**
     * @author Jeff Fong
     * @description 当前分页的编号
     * @date 2023/5/13 11:34
     * @param: page
     * @return org.json.JSONObject
     **/
    JSONObject getList(Integer page);
}