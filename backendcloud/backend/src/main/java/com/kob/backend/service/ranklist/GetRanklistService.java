package com.kob.backend.service.ranklist;

import com.alibaba.fastjson2.JSONObject;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname GetRanklistService
 * @description: 获取用户对战的排行榜
 * @author: Jeff Fong
 * @create: 2023/05/13 20:54
 **/
public interface GetRanklistService {
    JSONObject getList(Integer page);
}