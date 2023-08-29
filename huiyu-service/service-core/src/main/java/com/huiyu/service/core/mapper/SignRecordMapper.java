package com.huiyu.service.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huiyu.service.core.entity.SignRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface SignRecordMapper extends BaseMapper<SignRecord> {
    /**
     * 根据用户id和签到时间判断是否已签到
     *
     * @param userId   用户id
     * @param signDate 签到日期
     * @return 1/0
     */
    int countByUserIdAndSignTime(@Param("userId") Long userId, @Param("signDate") LocalDate signDate);
}
