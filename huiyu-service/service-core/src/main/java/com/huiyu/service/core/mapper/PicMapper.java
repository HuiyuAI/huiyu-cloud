package com.huiyu.service.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.model.dto.UserPicCountDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PicMapper extends BaseMapper<Pic> {
    List<Pic> getByUserId(@Param("userId") Long userId);

    Long getParentPicIdById(Long id);

    int deleteByUuid(String uuid);

    Pic getByTaskId(Long taskId);

    int delete(Long id);

    List<UserPicCountDto> countByUserIdList(@Param("userIdList") List<Long> userIdList);
}
