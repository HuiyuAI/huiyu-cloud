package com.huiyu.service.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huiyu.service.core.entity.SignRecord;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface SignRecordMapper extends BaseMapper<SignRecord> {
    /**
     * 根据用户id和签到时间判断是否已签到
     *
     * @param userId 用户id
     * @param start  开始时间
     * @param end    结束时间
     * @return 1/0
     */
    int countByUserIdAndSignTime(Long userId, LocalDateTime start, LocalDateTime end);
}
