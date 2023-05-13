package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.Record;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: Record表的数据库映射
 * @author: fangshaolei
 * @time: 2023/3/21 16:33
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {
}
