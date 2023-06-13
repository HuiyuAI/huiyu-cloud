package com.huiyu.service.core.mapper;

import com.huiyu.service.core.entity.IntegralRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IntegralRecordMapper {

    int insertRecord(@Param("integralRecord") IntegralRecord integralRecord);

    List<IntegralRecord> getByUserId(@Param("userId") String userId);

    int update(IntegralRecord integralRecord);

}
