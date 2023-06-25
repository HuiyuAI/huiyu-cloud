package com.huiyu.service.core.mapper;

import com.huiyu.service.core.entity.RequestLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RequestLogMapper {
    int insert(RequestLog requestLog);
}
