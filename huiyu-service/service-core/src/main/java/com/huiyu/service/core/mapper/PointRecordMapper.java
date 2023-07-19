package com.huiyu.service.core.mapper;

import com.huiyu.service.core.entity.PointRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PointRecordMapper {

    int insertRecord(PointRecord pointRecord);

    List<PointRecord> getByUserId(Long userId);

}
