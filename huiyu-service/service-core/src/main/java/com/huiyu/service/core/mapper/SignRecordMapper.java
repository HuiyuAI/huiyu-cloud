package com.huiyu.service.core.mapper;

import com.huiyu.service.core.entity.SignRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SignRecordMapper {
    SignRecord getTodayByUserId(Long userId);

    int insert(SignRecord signRecord);

    int updateToday(SignRecord signRecord);

    int deleteTodayByUserId(Long userId);
}
