package com.kob.backend.controller.record;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.service.ranklist.GetRanklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname GetRanklistController
 * @description: None
 * @author: Jeff Fong
 * @create: 2023/05/13 20:56
 **/
@RestController
public class GetRanklistController {
    @Autowired
    private GetRanklistService getRanklistService;
    
    /**
     * @author Jeff Fong
     * @description 获取排行榜
     * @date 2023/5/13 20:57
     * @param: data
     * @return com.alibaba.fastjson2.JSONObject
     **/
    @GetMapping("/ranklist/getlist/")
    public JSONObject getList(@RequestParam Map<String, String> data){
        Integer page = Integer.parseInt(data.get("page"));
        return getRanklistService.getList(page);
    }
}