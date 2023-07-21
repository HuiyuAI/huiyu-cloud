package com.huiyu.service.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huiyu.service.core.entity.RequestLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RequestLogMapper extends BaseMapper<RequestLog> {

}
