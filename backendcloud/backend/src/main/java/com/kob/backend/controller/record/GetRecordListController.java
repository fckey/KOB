package com.kob.backend.controller.record;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname GetRecordListController
 * @description: None
 * @author: Jeff Fong
 * @create: 2023/05/13 11:36
 **/
@RestController
@RequestMapping("/api")
public class GetRecordListController {
    @Autowired
    private GetRecordListService getRecordListService;

    /**
     * @author Jeff Fong
     * @description 返回当前页所有的数据
     * @date 2023/5/13 11:38
     * @param: data
     * @return com.alibaba.fastjson2.JSONObject
     **/
    @GetMapping("/record/getlist/")
    public JSONObject getList(@RequestParam Map<String, String> data){
        Integer page = Integer.parseInt(data.get("page"));
        return getRecordListService.getList(page);
    }
}